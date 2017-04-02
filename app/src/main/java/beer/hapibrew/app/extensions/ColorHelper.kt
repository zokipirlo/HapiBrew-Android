package beer.hapibrew.app.extensions

import android.graphics.Color

/**
 * A utility class for darkening and lightening colors in the same way as
 * material design color palettes
 * Created by Ammar Mardawi on 12/4/16.
 *
 * http://stackoverflow.com/questions/30870167/convert-colorprimary-to-colorprimarydark-how-much-darker
 */


/**
 * Darkens a given color
 * @param base base color
 * *
 * @param amount amount between 0 and 100
 * *
 * @return darken color
 */
fun darken(base: Int, amount: Int): Int {
    var hsv = FloatArray(3)
    Color.colorToHSV(base, hsv)
    val hsl = hsv2hsl(hsv)
    hsl[2] -= amount / 100f
    if (hsl[2] < 0)
        hsl[2] = 0f
    hsv = hsl2hsv(hsl)
    return Color.HSVToColor(hsv)
}

/**
 * lightens a given color
 * @param base base color
 * *
 * @param amount amount between 0 and 100
 * *
 * @return lightened
 */
fun lighten(base: Int, amount: Int): Int {
    var hsv = FloatArray(3)
    Color.colorToHSV(base, hsv)
    val hsl = hsv2hsl(hsv)
    hsl[2] += amount / 100f
    if (hsl[2] > 1)
        hsl[2] = 1f
    hsv = hsl2hsv(hsl)
    return Color.HSVToColor(hsv)
}


/**
 * Converts HSV (Hue, Saturation, Value) color to HSL (Hue, Saturation, Lightness)
 * Credit goes to xpansive
 * https://gist.github.com/xpansive/1337890
 * @param hsv HSV color array
 * *
 * @return hsl
 */
private fun hsv2hsl(hsv: FloatArray): FloatArray {
    val hue = hsv[0]
    val sat = hsv[1]
    val `val` = hsv[2]

    //Saturation is very different between the two color spaces
    //If (2-sat)*val < 1 set it to sat*val/((2-sat)*val)
    //Otherwise sat*val/(2-(2-sat)*val)
    //Conditional is not operating with hue, it is reassigned!
    // sat*val/((hue=(2-sat)*val)<1?hue:2-hue)
    val nhue = (2f - sat) * `val`
    var nsat = sat * `val` / if (nhue < 1f) nhue else 2f - nhue
    if (nsat > 1f)
        nsat = 1f

    return floatArrayOf(
            //[hue, saturation, lightness]
            //Range should be between 0 - 1
            hue, //Hue stays the same

            // check nhue and nsat logic
            nsat,

            nhue / 2f //Lightness is (2-sat)*val/2
    )//See reassignment of hue above
}

/**
 * Reverses hsv2hsl
 * Credit goes to xpansive
 * https://gist.github.com/xpansive/1337890
 * @param hsl HSL color array
 * *
 * @return hsv color array
 */
private fun hsl2hsv(hsl: FloatArray): FloatArray {
    val hue = hsl[0]
    var sat = hsl[1]
    val light = hsl[2]

    sat *= if (light < .5) light else 1 - light

    return floatArrayOf(
            //[hue, saturation, value]
            //Range should be between 0 - 1

            hue, //Hue stays the same
            2f * sat / (light + sat), //Saturation
            light + sat //Value
    )
}

fun getDarkVersion(color: Int): Int {
    return darken(color, 12)
}

fun getLightVersion(color:Int): Int {
    return lighten(color, 12)
}


/**
setColor("50", ColorUtil.lighten(color, 52), mTv50);
setColor("100", ColorUtil.lighten(color, 37), mTv100);
setColor("200", ColorUtil.lighten(color, 26), mTv200);
setColor("300", ColorUtil.lighten(color, 12), mTv300);
setColor("400", ColorUtil.lighten(color, 6), mTv400);

setColor("500", ColorUtil.lighten(color, 0), mTv500);

setColor("600", ColorUtil.darken(color, 6), mTv600);
setColor("700", ColorUtil.darken(color, 12), mTv700);
setColor("800", ColorUtil.darken(color, 18), mTv800);
setColor("900", ColorUtil.darken(color, 24), mTv900);
        */
