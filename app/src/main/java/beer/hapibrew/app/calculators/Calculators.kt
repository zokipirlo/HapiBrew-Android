package beer.hapibrew.app.calculators

import beer.hapibrew.app.data.RecipeAbv
import beer.hapibrew.app.extensions.*
import beer.hapibrew.app.gfPrefs
import beer.hapibrew.beerxml2proto.proto.*
import com.squareup.wire.Wire

fun calculateBoilSize(batchSize:Gallons, boilTime:Int):Gallons = (batchSize + (gfPrefs.gfEvaporation / 60.0) * boilTime + gfPrefs.gfTrubLoss)

fun calculateIbu(batchSize:Gallons, efficiency:Double, boilTime:Int, fermentables: List<Fermentable>, hops:List<Hop>):Double {
    val og = calculateOriginalGravity(batchSize, efficiency, fermentables)
    return calculateIbu(batchSize, calculateBoilSize(batchSize, boilTime), og, hops)
}

fun calculateIbu(batchSize:Gallons, boilSize:Gallons, originalGravity:Double, hops:List<Hop>):Double {
    val boilgravity:Double = (batchSize / boilSize) * (originalGravity - 1)
    val bfactor: Double = 1.65 * Math.pow(0.000125, boilgravity)

    var totalIBU:Double = 0.0
    for (hop in hops) {
        val hopUse = Wire.get(hop.use, Hop.DEFAULT_USE)
        if (hopUse === HopUse.BOIL || hopUse === HopUse.FIRST_WORT) {
            val hopTime: Double = Wire.get(hop.time, Hop.DEFAULT_TIME)!!
            if (hopTime <= 0.0) {
                continue
            }
            val hopTimeCorected: Double = -0.04 * hopTime
            val tfactor: Double = (1 - Math.pow(Math.E, hopTimeCorected)) / 4.15
            var util = bfactor * tfactor
            if (Wire.get(hop.form, Hop.DEFAULT_FORM) == HopForm.PELLET) {
                util *= 1.1
            }
            val hopAlpha = Wire.get(hop.alpha, Hop.DEFAULT_ALPHA)!! / 100.0
            val hopAmount = kilogramsToOunces(Wire.get(hop.amount, Hop.DEFAULT_AMOUNT))
            var bitternes = util * ((hopAlpha * hopAmount * 7490) / batchSize)
            //TODO: check if need to uncomment this
//            if (hopUse === HopUse.FIRST_WORT) {
//                bitternes *= 1.1
//            }
            totalIBU += bitternes
        }
    }

    return totalIBU
}

fun calculateOriginalGravity(batchSize:Gallons, efficiency:Double, fermentables: List<Fermentable>):Double {
    var totalPoints = 0.0
    for (fermentable in fermentables) {
        val potential = Wire.get(fermentable.potential, Fermentable.DEFAULT_POTENTIAL)!!
        val ppg:Double
        if (potential != Fermentable.DEFAULT_POTENTIAL) {
            ppg = (potential - 1) * 1000.0
        } else {
            val fyield = Wire.get(fermentable.yield, Fermentable.DEFAULT_YIELD)!!
            ppg = (fyield * 0.01) * 46.214
        }
        val amount = kilogramsToPounds(Wire.get(fermentable.amount, Fermentable.DEFAULT_AMOUNT))
        totalPoints += ppg * amount
    }
    val points = totalPoints * (efficiency / 100.0)
    //val preboilOg = ((points / litersToGallons(boilSize)) * 0.001) + 1;
    return ((points / batchSize) * 0.001) + 1
}

fun calculateRecipeAbv(batchSize:Gallons, efficiency:Double, fermentables: List<Fermentable>, yeasts: List<Yeast>): RecipeAbv {
    val og:Double = calculateOriginalGravity(batchSize, efficiency, fermentables)
    val fg:Double
    val abv:Double
    if (yeasts.isNotEmpty()) {
        val yeast = yeasts[0]
        val attenuation: Double = Wire.get(yeast.attenuation, Yeast.DEFAULT_ATTENUATION)!!
        fg = ((og - 1) * (1 - (attenuation / 100.0))) + 1
        abv = (og - fg) * (125 * 1.05)
    } else {
        fg = Recipe.DEFAULT_EST_FG
        abv = Recipe.DEFAULT_EST_ABV
    }
    return RecipeAbv(og, fg, abv)
}

fun calculateSrm(batchSize:Gallons, fermentables: List<Fermentable>):Double {
    var totalMcu = 0.0
    for (fermentable in fermentables) {
        val color = Wire.get(fermentable.color, Fermentable.DEFAULT_COLOR)!!
        val amount = kilogramsToPounds(Wire.get(fermentable.amount, Fermentable.DEFAULT_AMOUNT))
        val mcu = color * (amount / batchSize)
        totalMcu += mcu
    }
    val srm = 1.4922 * Math.pow(totalMcu, 0.6859)
    return Math.min(srm, 80.0)
}

fun calculateAbv(og:Sg, fg:Sg):Double = (76.08 * (og - fg) / (1.775 - og)) * (fg / 0.794)
/**
 * return corrected Og, corrected Fg, Abv
 */
fun fixRefractometer(og:Sg, fg:Sg, wcf:Sg):Triple<Sg, Sg, Double> {
    fun fgFromBrixOg(og:Brix, fg:Brix):Sg {
        return 1.0000 - 0.0044993 * og + 0.011774 * fg + 0.00027581 * Math.pow(og, 2.0) - 0.0012717 * Math.pow(fg, 2.0) - 0.0000072800 * Math.pow(og, 3.0) + 0.000063293 * Math.pow(fg, 3.0)
    }
    val wcfOg = platoToSg(sgToPlato(og) / wcf)
    val fgFromOg = fgFromBrixOg(og.sgToBx(), fg.sgToBx())
    val abv = calculateAbv(wcfOg, fgFromOg)
    return Triple(wcfOg, fgFromOg, abv)
}

/**
 * return psi
 */
fun kegPressure(temp:Fahrenheit, volumes:Double):Psi =
        (-16.6999 - (0.0101059 * temp) + (0.00116512 * temp * temp) + (0.173354 * temp * volumes) + (4.24267 * volumes) - (0.0684226 * volumes * volumes))

/**
 * return sucrose(table sugar), dextrose(corn sugar), dme in grams
 */
fun priming(temp:Fahrenheit, batchSize: Liters, volumes:Double):Triple<Grams,Grams,Grams> {
    val beerCO2 = 3.0378 - (0.050062 * temp) + (0.00026555 * temp * temp)
    val sucrose = ((volumes * 2) - (beerCO2 * 2)) * 2 * batchSize
    val dextrose = sucrose / 0.91
    val dme = sucrose / 0.68
    return Triple(sucrose, dextrose, dme)
}

fun calculateMashWater(fermentables: List<Fermentable>):Liters {
    val grainsWeight = fermentables.sumByDouble { Wire.get(it.amount, Fermentable.DEFAULT_AMOUNT)!! }
    val ratio = if (gfPrefs.gfUseBigWTGRatio && grainsWeight >= gfPrefs.gfWTGBigFactor)
        gfPrefs.gfWTGBigRatio
    else
        gfPrefs.gfWTGRatio

    return (grainsWeight * ratio + gfPrefs.gfMashWater)
}

fun calculateSpargeWater(batchSize: Liters, boilTime: Int, mashWater:Liters, fermentables: List<Fermentable>):Liters {
    val preboil = batchSize + gfPrefs.gfTrubLoss + (gfPrefs.gfEvaporation / 60.0) * boilTime
    val grainsWeight = fermentables.sumByDouble { Wire.get(it.amount, Fermentable.DEFAULT_AMOUNT)!! }
    return (preboil - mashWater) + (grainsWeight * gfPrefs.gfSpargeFactor)
}