package beer.hapibrew.app.extensions

import beer.hapibrew.app.calculators.*

typealias Celsius = Double
typealias Fahrenheit = Double

typealias Sg = Double
typealias Brix = Double
typealias Plato = Double

typealias Psi = Double
typealias Bar = Double

typealias Liters = Double
typealias Milliliters = Double
typealias Gallons = Double
typealias Teaspoons = Double

typealias Grams = Double
typealias Kilograms = Double
typealias Ounces = Double
typealias Pounds = Double

fun Double.cToF() = celsiusToFahrenheit(this)
fun Double.fToC() = fahrenheitToCelsius(this)

fun Double.sgToBx() = sgToBrix(this)
fun Double.sgToPlato() = sgToPlato(this)

fun Double.bxToSg() = brixToSg(this)
fun Double.bxToPlato() = brixToPlato(this)

fun Double.platoToSg() = platoToSg(this)
fun Double.platoToBx() = platoToBrix(this)

fun Double.psiToBar():Bar = psiToBar(this)
fun Double.barToPsi():Psi = barToPsi(this)

fun Double.lToGal() = litersToGallons(this)
fun Double.lToMl() = litersToMilliliters(this)
fun Double.lToTsp() = litersToTeaspoon(this)
fun Double.galToL() = gallonsToLiters(this)

fun Double.lPerKgToGalPerLb() = litersToGallons(this) / 1.0.kgToLb()
fun Double.galPerLbToLPerKg() = gallonsToLiters(this) / 1.0.lbToKg()

fun Double.gToKg() = gramsToKilograms(this)
fun Double.gToOz() = gramsToOunces(this)
fun Double.gToLb() = gramsToPounds(this)

fun Double.kgToG() = kilogramsToGrams(this)
fun Double.kgToOz() = kilogramsToOunces(this)
fun Double.kgToLb() = kilogramsToPounds(this)

fun Double.ozToG() = ouncesToGrams(this)
fun Double.ozToKg() = ouncesToKilograms(this)
fun Double.ozToLb() = ouncesToPounds(this)

fun Double.lbToG() = poundsToGrams(this)
fun Double.lbToKg() = poundsToKilograms(this)
fun Double.lbToOz() = poundsToOunces(this)