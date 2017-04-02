package beer.hapibrew.app.extensions

import beer.hapibrew.app.R
import android.content.Context
import android.view.View
import android.widget.Button
import beer.hapibrew.app.data.RecipeData
import beer.hapibrew.app.data.SessionData
import beer.hapibrew.app.prefs
import beer.hapibrew.app.view.CountDownTextView
import beer.hapibrew.beerxml2proto.proto.*
import com.squareup.wire.Wire
import org.jetbrains.anko.enabled
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

fun unitNoDecimals(value: Int, unit: String): String {
    return "%d %s".format(Locale.US, value, unit)
}

fun unitOneDecimal(value: Double, unit: String): String {
    return "%.1f %s".format(Locale.US, value, unit)
}

fun unitTwoDecimals(value: Double, unit: String): String {
    return "%.2f %s".format(Locale.US, value, unit)
}

fun unitAsText(value: String, unit: String): String {
    return "%s %s".format(Locale.US, value, unit)
}

fun minutesToTime(context: Context, min: Double, showUnit: Boolean = true, forceLarge: Boolean = false, forceSmall: Boolean = false): String {
    if (forceLarge || (min >= 1440 && !forceSmall)) {
        val days = (min / 1440).toInt()
        if (showUnit)
            return context.resources.getQuantityString(R.plurals.days, days, days)
        else
            return days.toString()
    }/* else if (min >= 120 && !forceSmall) {
        val unit = if (showUnit) context.getString(R.string.unit_short_hours) else ""
        return context.getString(R.string.unit_no_decimal, (min / 60).toInt(), unit).trim()
    }*/ else {
        val unit = if (showUnit) context.getString(R.string.unit_short_minutes) else ""
        return unitNoDecimals(min.toInt(), unit).trim()
    }
}

fun displayTemp(context: Context, temp: Celsius, showUnit: Boolean): String {
    if (prefs.useMetric) {
        val unit = if (showUnit) context.getString(R.string.unit_celsius) else ""
        return unitOneDecimal(temp, unit).trim()
    } else {
        val unit = if (showUnit) context.getString(R.string.unit_fahrenheit) else ""
        return unitOneDecimal(temp.cToF(), unit).trim()
    }
}

fun displayWeight(context: Context, amount: Kilograms, showUnit: Boolean, forceLarge: Boolean = false, forceSmall: Boolean = false): String {
    if (prefs.useMetric) {
        if (forceLarge || (amount >= 1.0f && !forceSmall)) {
            val unit = if (showUnit) context.getString(R.string.unit_short_kilograms) else ""
            return unitTwoDecimals(amount, unit).trim()
        } else {
            val unit = if (showUnit) context.getString(R.string.unit_short_grams) else ""
            return unitOneDecimal(amount.kgToG(), unit).trim()
        }
    } else {
        if (forceLarge || (amount >= 1.0f && !forceSmall)) {
            val unit = if (showUnit) context.getString(R.string.unit_short_pounds) else ""
            return unitTwoDecimals(amount.kgToLb(), unit).trim()
        } else {
            val unit = if (showUnit) context.getString(R.string.unit_short_ounces) else ""
            return unitOneDecimal(amount.kgToOz(), unit).trim()
        }
    }
}

fun displayVolume(context: Context, amount: Liters, showUnit: Boolean, forceLarge: Boolean = false, forceSmall: Boolean = false): String {
    if (prefs.useMetric) {
        if (forceLarge || (amount >= 1.0f && !forceSmall)) {
            val unit = if (showUnit) context.getString(R.string.unit_short_liters) else ""
            return unitTwoDecimals(amount, unit).trim()
        } else {
            val unit = if (showUnit) context.getString(R.string.unit_short_milliliters) else ""
            return unitOneDecimal(amount.lToMl(), unit).trim()
        }
    } else {
        if (forceLarge || (amount >= 1.0f && !forceSmall)) {
            val unit = if (showUnit) context.getString(R.string.unit_short_gallons) else ""
            return unitTwoDecimals(amount.lToGal(), unit).trim()
        } else {
            val unit = if (showUnit) context.getString(R.string.unit_short_teaspoons) else ""
            return unitOneDecimal(amount.lToTsp(), unit).trim()
        }
    }
}

/* Fermentable */
fun Fermentable.displayAmount(context: Context): String {
    val amount: Kilograms = Wire.get(this.amount, Hop.DEFAULT_AMOUNT)!!
    return displayWeight(context, amount, false, forceLarge = true)
}

/* Fermentation */
fun Fermentation.displayTemperature(context: Context, showUnit: Boolean = false): String {
    return displayTemp(context, Wire.get(this.temperature, Fermentation.DEFAULT_TEMPERATURE)!!, showUnit)
}

fun Fermentation.displayTime(context: Context, showUnit: Boolean = false): String {
    val days = Wire.get(this.days, Fermentation.DEFAULT_DAYS)!!.toInt()
    if (!showUnit)
        return days.toString()
    else
        return unitAsText(days.toString(), context.getString(R.string.unit_short_days))
}

fun Fermentation.displayName(context: Context): String {
    if (Wire.get(this.is_aging, Fermentation.DEFAULT_IS_AGING)!!) {
        return context.getString(R.string.fermentation_aging)
    } else {
        var step = Wire.get(this.step, Fermentation.DEFAULT_STEP)!!
        step = minOf(3, maxOf(0, step - 1))
        return context.resources.getStringArray(R.array.fermentation_step)[step]
    }
}

/* Hop */
fun Hop.displayAmount(context: Context): String {
    val amount: Kilograms = Wire.get(this.amount, Hop.DEFAULT_AMOUNT)!!
    return displayWeight(context, amount, false, forceSmall = true)
}

fun Hop.displayTime(context: Context): String {
    val min = Wire.get(this.time, Hop.DEFAULT_TIME)!!
    val useShortUnits = use?.forceShortUnit() ?: false
    return minutesToTime(context, min, false, forceSmall = useShortUnits, forceLarge = !useShortUnits)
}

fun Hop.displayForm(context: Context): String {
    return context.resources.getStringArray(R.array.hop_form)[Wire.get(this.form, Hop.DEFAULT_FORM)!!.ordinal]
}

fun Hop.displayUse(context: Context): String {
    return context.resources.getStringArray(R.array.hop_use)[Wire.get(this.use, Hop.DEFAULT_USE)!!.ordinal]
}

fun HopUse.forceShortUnit(): Boolean {
    return this != HopUse.DRY_HOP
}

fun HopUse.displayUse(context: Context): String {
    return context.resources.getStringArray(R.array.hop_use)[Wire.get(this, Hop.DEFAULT_USE)!!.ordinal]
}

/* Mash */
fun Mash.displaySpargeTemperature(context: Context): String {
    val spargeTemp = Wire.get(this.sparge_temp, Mash.DEFAULT_SPARGE_TEMP)!!
    if (spargeTemp != Mash.DEFAULT_SPARGE_TEMP)
        return displayTemp(context, spargeTemp, true)
    else
        return context.getString(R.string.mash_no_sparge)
}

fun Mash.displayPh(context: Context): String {
    val ph = Wire.get(this.ph, Mash.DEFAULT_PH)!!
    if (ph != Mash.DEFAULT_PH)
        return ph.toOneDecimalFormat()
    else
        return context.getString(R.string.mash_no_ph)
}

/* Mash Step */
fun MashStep.displayTime(context: Context, showUnit: Boolean = false): String {
    val min = Wire.get(this.step_time, MashStep.DEFAULT_STEP_TIME)!!
    return minutesToTime(context, min, showUnit, forceSmall = true)
}

fun MashStep.displayTemperature(context: Context, showUnit: Boolean = false): String {
    return displayTemp(context, Wire.get(this.step_temp, MashStep.DEFAULT_STEP_TEMP)!!, showUnit)
}

/* Misc */
fun Misc.displayAmount(context: Context): String {
    val isKilograms = Wire.get(this.amount_is_weight, Misc.DEFAULT_AMOUNT_IS_WEIGHT)!!
    val amount = Wire.get(this.amount, Misc.DEFAULT_AMOUNT)!!
    if (isKilograms) {
        return displayWeight(context, amount, false, forceSmall = true)
    } else {
        return displayVolume(context, amount, false, forceSmall = true)
    }
}

fun Misc.displayTime(context: Context): String {
    val min = Wire.get(this.time, Hop.DEFAULT_TIME)!!
    val useShortUnits = use?.forceShortUnit() ?: false
    return minutesToTime(context, min, false, forceSmall = useShortUnits, forceLarge = !useShortUnits)
}

fun Misc.displayUse(context: Context): String {
    return context.resources.getStringArray(R.array.misc_use)[Wire.get(this.use, Misc.DEFAULT_USE)!!.ordinal]
}

fun Misc.displayType(context: Context): String {
    return context.resources.getStringArray(R.array.misc_type)[Wire.get(this.type, Misc.DEFAULT_TYPE)!!.ordinal]
}

fun MiscUse.displayUse(context: Context): String {
    return context.resources.getStringArray(R.array.misc_use)[Wire.get(this, Misc.DEFAULT_USE)!!.ordinal]
}

fun MiscUse.forceShortUnit(): Boolean {
    return this == MiscUse.BOIL || this == MiscUse.MASH || this == MiscUse.BOTTLING
}

/* Recipe */
fun Recipe.displayBatchVolume(context: Context): String {
    val liters = Wire.get(this.batch_size, Recipe.DEFAULT_BATCH_SIZE)!!
    if (liters != Recipe.DEFAULT_BATCH_SIZE)
        return displayVolume(context, liters, true, forceLarge = true)
    else
        return context.getString(R.string.recipe_no_volume)
}

fun Recipe.displayBoilTime(context: Context): String {
    val min = Wire.get(this.boil_time, Recipe.DEFAULT_BOIL_TIME)!!
    return minutesToTime(context, min.toDouble(), true, forceSmall = true)
}

/* Yeast */
fun Yeast.fullName(): String {
    return (Wire.get(this.laboratory, Yeast.DEFAULT_LABORATORY)
            + " "
            + Wire.get(this.name, Yeast.DEFAULT_NAME)
            + " "
            + Wire.get(this.product_id, Yeast.DEFAULT_PRODUCT_ID)
            ).trim()
}

fun Yeast.displayType(context: Context): String {
    return context.resources.getStringArray(R.array.yeast_type)[Wire.get(this.type, Yeast.DEFAULT_TYPE)!!.ordinal]
}

fun Yeast.displayForm(context: Context): String {
    return context.resources.getStringArray(R.array.yeast_form)[Wire.get(this.form, Yeast.DEFAULT_FORM)!!.ordinal]
}

fun View.setAlternateBackground(position: Int) {
    setBackgroundResource(
            if (position % 2 == 0)
                R.color.table_background_even
            else
                R.color.table_background_odd
    )
}

fun RecipeData.getSessionName():String {
    return "%s (%d)".format(Locale.US, name, sessions)
}

fun SessionData.setTimer(context: Context, isStepCompleted:Boolean, countDownTextView: CountDownTextView, button: Button?) {
    fun showTime(duration:Long) {
        countDownTextView.text =  when (phase) {
            SessionData.Phase.FERMENTATION -> millisecondsAsTime(context, duration)
            SessionData.Phase.MASH, SessionData.Phase.BOIL -> millisecondsAsMinSec(duration)
            else ->  context.getString(R.string.session_step_time_waiting)
        }
    }
    if (isStepCompleted) {
        countDownTextView.text = context.getString(R.string.session_step_time_done)
        countDownTextView.stop()
    } else {
        if (stepStartEpoch == 0L) {
            showTime(getStepDuration())
        } else {
            if (stepPauseEpoch != 0L) {
                showTime(stepEndEpoch - stepPauseEpoch)
            } else {
                startTimer(stepEndEpoch - currentEpoch(), countDownTextView, button)
            }
        }
    }
}

fun SessionData.startTimer(duration:Long, countDownTextView: CountDownTextView, button: Button?) {
    countDownTextView.start(duration, forceMinSec = (phase != SessionData.Phase.FERMENTATION), onFinish={
        button?.enabled = false
    })
}

fun SessionData.getStepDuration():Long {
    if (phase == SessionData.Phase.MASH) {
//        return 15*1000
        recipe.mash?.let {
            val mashStep = it.steps[step]
            return Wire.get(mashStep.step_time, MashStep.DEFAULT_STEP_TIME)!!.toLong() * 60 * 1000L
        }
    } else if (phase == SessionData.Phase.BOIL) {
//        return 15*1000
        return Wire.get(recipe.boil_time, Recipe.DEFAULT_BOIL_TIME)!! * 60 * 1000L
    } else if (phase == SessionData.Phase.FERMENTATION) {
//        return 15*1000
        val fermentation = recipe.fermentations[step]
        return Wire.get(fermentation.days, Fermentation.DEFAULT_DAYS)!!.toLong() * 24 * 60 * 60 * 1000L
    }

    return -1L
}

fun SessionData.getSessionDurationString(context: Context):String {
    return context.getString(R.string.session_step_completed_subtitle,
            getDateAsString(startEpoch), getDateAsString(endEpoch))
}
//    val dateFormatter = DateTimeFormatterBuilder()
//            .appendDayOfMonth(1)
//            .appendMonthOfYearText()
//            .appendLiteral(' ')
//            .appendYear(4, 4)
//            .toFormatter()
val dateFormatter: DateTimeFormatter = DateTimeFormat.mediumDate()
private fun getDateAsString(epoch:Long):String {
    if (epoch == 0L)
        return ""
    return dateFormatter.print(epoch)
}