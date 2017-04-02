package beer.hapibrew.app.extensions

import beer.hapibrew.app.R
import android.content.Context
import android.support.design.widget.TextInputEditText
import org.joda.time.Period
import org.joda.time.PeriodType
import java.util.*

fun TextInputEditText.toDouble():Double {
    if (text.isEmpty())
        return 0.0
    return text.toString().replace(",",".").toDouble()
}

fun Number.toSgFormat():String = "%.3f".format(Locale.US, this)
fun Number.toBrixFormat():String = "%.1f".format(Locale.US, this)
fun Number.toPlatoFormat():String = "%.1f".format(Locale.US, this)

fun Number.toNoDecimalsFormat():String = "%d".format(Locale.US, this)
fun Number.toOneDecimalFormat():String = "%.1f".format(Locale.US, this)
fun Number.toTwoDecimalsFormat():String = "%.2f".format(Locale.US, this)

fun Number.toRoundedFormat():String = "%d".format(Locale.US, this.toInt())

fun Double.toBarFormat():String {
    if (this < 0.0) {
        return ""
    }
    return "%.2f".format(Locale.US, this)
}
fun Double.toPsiFormat():String {
    if (this < 0.0) {
        return ""
    }
    return "%.1f".format(Locale.US, this)
}

fun currentEpoch():Long {
    return System.currentTimeMillis()
}

fun millisecondsAsTime(context: Context, millis: Long):String {
    return millisecondsAsTime(context, Period(millis).normalizedStandard(PeriodType.dayTime()))
}
fun millisecondsAsTime(context: Context, period: Period):String {
    if (period.days > 0) {
        return unitAsText(period.days.toString(), context.getString(R.string.unit_short_days))
    } else if (period.hours > 0) {
        return "%02dh %02dm".format(Locale.US, period.hours, period.minutes)
    } else {
        return "%02dm %02ds".format(Locale.US, period.minutes, period.seconds)
    }
}

fun millisecondsAsMinSec(millis:Long):String {
    val sec = (millis / 1000).toInt()
    val minutes:Int = sec / 60
    val seconds:Int = sec % 60
    return "%02d:%02d".format(Locale.US, minutes, seconds)
}