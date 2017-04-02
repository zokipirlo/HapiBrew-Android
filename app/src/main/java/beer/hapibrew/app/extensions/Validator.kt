package beer.hapibrew.app.extensions

import beer.hapibrew.beerxml2proto.proto.Style
import beer.hapibrew.app.R
import beer.hapibrew.app.calculators.sgToBrix
import android.content.Context
import android.support.design.widget.TextInputLayout

fun isValidGravity(context:Context, gravity:Double, isSg:Boolean, wrapperView: TextInputLayout? = null):Boolean {
    if (isSg) {
        val min = Style.DEFAULT_OG_MIN
        val max = Style.DEFAULT_OG_MAX
        if (gravity < min || gravity > max) {
            wrapperView?.error = context.getString(R.string.error_between_value, min.toSgFormat(), max.toSgFormat())
            return false
        }
    } else {
        val min = sgToBrix(Style.DEFAULT_OG_MIN.toDouble())
        val max = sgToBrix(Style.DEFAULT_OG_MAX.toDouble())
        if (gravity < min || gravity > max) {
            wrapperView?.error = context.getString(R.string.error_between_value, min.toBrixFormat(), max.toBrixFormat())
            return false
        }
    }
    wrapperView?.error = ""
    return true
}

fun isValidFactor(context:Context, factor:Double, wrapperView: TextInputLayout? = null):Boolean {
    val min = 1.00
    val max = 1.11
    if (factor < min || factor > max) {
        wrapperView?.error = context.getString(R.string.error_between_value,
                min.toTwoDecimalsFormat(), max.toTwoDecimalsFormat())
        return false
    }
    wrapperView?.error = ""
    return true
}

fun isValidTemperature(context:Context, temp:Double, isMetric:Boolean, wrapperView: TextInputLayout? = null):Boolean {
    val min = if (isMetric) 0 else 32
    val max = if (isMetric) 26 else 70
    if (temp < min || temp > max) {
        wrapperView?.error = context.getString(R.string.error_between_value,
                min.toRoundedFormat(), max.toRoundedFormat())
        return false
    }
    wrapperView?.error = ""
    return true
}

fun isValidAmount(context:Context, amount:Double, isMetric:Boolean, wrapperView: TextInputLayout? = null):Boolean {
    val min = if (isMetric) 3.0 else 0.8
    val max = if (isMetric) 300.0 else 80.0
    if (amount < min || amount > max) {
        wrapperView?.error = context.getString(R.string.error_between_value,
                min.toRoundedFormat(), max.toRoundedFormat())
        return false
    }
    wrapperView?.error = ""
    return true
}

fun isValidVolume(context:Context, volume:Double, wrapperView: TextInputLayout? = null):Boolean {
    val min = 1.4
    val max = 3.2
    if (volume < min || volume > max) {
        wrapperView?.error = context.getString(R.string.error_between_value,
                min.toOneDecimalFormat(), max.toOneDecimalFormat())
        return false
    }
    wrapperView?.error = ""
    return true
}