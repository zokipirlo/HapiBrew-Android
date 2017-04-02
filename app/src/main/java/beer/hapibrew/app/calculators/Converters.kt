package beer.hapibrew.app.calculators

import beer.hapibrew.app.extensions.*

fun litersToGallons(liters:Liters?):Gallons =  (liters ?: 0.0) * 0.264172052
fun litersToMilliliters(liters:Liters?):Milliliters =  (liters ?: 0.0) * 1000.0
fun litersToTeaspoon(liters:Liters?):Teaspoons =  (liters ?: 0.0) * 202.8841362
fun gallonsToLiters(gallons:Gallons?):Liters = (gallons ?: 0.0) / 0.264172052

fun gramsToKilograms(grams:Grams?):Kilograms = (grams ?: 0.0) / 1000.0
fun gramsToOunces(grams:Grams?):Ounces = (grams ?: 0.0) / 28.349523125
fun gramsToPounds(grams:Grams?):Pounds = kilogramsToPounds(gramsToKilograms(grams))

fun kilogramsToGrams(kilograms:Kilograms?):Grams = (kilograms ?: 0.0) * 1000.0
fun kilogramsToOunces(kilograms:Kilograms?):Ounces = gramsToOunces(kilogramsToGrams(kilograms))
fun kilogramsToPounds(kilograms:Kilograms?):Pounds = (kilograms ?: 0.0) * 2.20462262

fun ouncesToPounds(ounces:Ounces?):Pounds = (ounces ?: 0.0) / 16.0
fun ouncesToGrams(ounces:Ounces?):Grams = (ounces ?: 0.0) * 28.349523125
fun ouncesToKilograms(ounces:Ounces?):Kilograms = poundsToKilograms(ouncesToPounds(ounces))

fun poundsToOunces(pounds:Pounds?):Ounces = (pounds ?: 0.0) * 16.0
fun poundsToGrams(pounds:Pounds?):Grams = ouncesToGrams(poundsToOunces(pounds))
fun poundsToKilograms(pounds:Pounds?):Kilograms = (pounds ?: 0.0) / 2.20462262

fun lovibondToSrm(lovibond:Double):Double = (1.3546 * lovibond) - 0.76

val COLOR_MAP = arrayOf(
    "#FFE699", "#FFD878", "#FFCA5A", "#FFBF42", "#FBB123", "#F8A600", "#F39C00", "#EA8F00",
    "#E58500", "#DE7C00", "#D77200", "#CF6900", "#CB6200", "#C35900", "#BB5100", "#B54C00",
    "#B04500", "#A63E00", "#A13700", "#9B3200", "#952D00", "#8E2900", "#882300", "#821E00",
    "#7B1A00", "#771900", "#701400", "#6A0E00", "#660D00", "#5E0B00", "#5A0A02", "#600903",
    "#520907", "#4C0505", "#470606", "#440607", "#3F0708", "#3B0607", "#3A070B", "#36080A"
)

fun srmToHex(srm:Double):String {
    val colorIndex = maxOf(minOf(srm.toInt(), COLOR_MAP.size), 1) // 1 - 40
    return COLOR_MAP[colorIndex - 1]
}
fun lovibondToHex(lovibond:Double):String = srmToHex(lovibondToSrm(lovibond))


fun sgToPlato(sg:Sg):Plato = maxOf((-1.0 * 616.868) + (1111.14 * sg) - (630.272 * Math.pow(sg, 2.0)) + (135.997 * Math.pow(sg, 3.0)), 0.0)
fun sgToBrix(sg:Sg):Brix = maxOf((((182.4601 * sg - 775.6821) * sg + 1262.7794) * sg - 669.5622), 0.0)
fun platoToSg(plato:Plato):Sg = 1.0 + (plato / (258.6 - ((plato / 258.2) * 227.1)))
fun platoToBrix(plato: Plato):Brix = sgToBrix(platoToSg(plato))
fun brixToSg(brix:Brix):Sg = (brix / (258.6 - ((brix / 258.2) * 227.1))) + 1.0
fun brixToPlato(brix: Brix):Plato = sgToPlato(brixToSg(brix))

fun celsiusToFahrenheit(value:Celsius):Fahrenheit = value * (9.0 / 5.0) + 32.0
fun fahrenheitToCelsius(value:Fahrenheit):Celsius = (value - 32.0) * (5.0 / 9.0)

fun psiToBar(pressure:Psi):Bar {
    return pressure * 0.0689475729
}
fun barToPsi(pressure:Bar):Psi {
    return pressure / 0.0689475729
}