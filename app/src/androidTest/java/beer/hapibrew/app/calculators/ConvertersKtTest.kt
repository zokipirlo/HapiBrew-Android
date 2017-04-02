package beer.hapibrew.app.calculators

import org.junit.Test

import org.junit.Assert.*

class ConvertersKtTest {
    @Test
    fun sgToPlato() {
        assertEquals(0.5, beer.hapibrew.app.calculators.sgToPlato(1.002), 0.1)
        assertEquals(3.5, beer.hapibrew.app.calculators.sgToPlato(1.014), 0.1)
        assertEquals(6.0, beer.hapibrew.app.calculators.sgToPlato(1.024), 0.1)
        assertEquals(9.0, beer.hapibrew.app.calculators.sgToPlato(1.036), 0.1)
        assertEquals(14.0, beer.hapibrew.app.calculators.sgToPlato(1.057), 0.1)
        assertEquals(24.0, beer.hapibrew.app.calculators.sgToPlato(1.101), 0.1)
        assertEquals(31.4, beer.hapibrew.app.calculators.sgToPlato(1.136), 0.1)
        assertEquals(40.0, beer.hapibrew.app.calculators.sgToPlato(1.179), 0.1)
    }

    @Test
    fun sgToBrix() {
        assertEquals(0.5, beer.hapibrew.app.calculators.sgToBrix(1.002), 0.1)
        assertEquals(3.5, beer.hapibrew.app.calculators.sgToBrix(1.014), 0.1)
        assertEquals(6.0, beer.hapibrew.app.calculators.sgToBrix(1.024), 0.1)
        assertEquals(9.0, beer.hapibrew.app.calculators.sgToBrix(1.036), 0.1)
        assertEquals(14.0, beer.hapibrew.app.calculators.sgToBrix(1.057), 0.1)
        assertEquals(24.0, beer.hapibrew.app.calculators.sgToBrix(1.101), 0.1)
        assertEquals(31.4, beer.hapibrew.app.calculators.sgToBrix(1.136), 0.1)
        assertEquals(40.0, beer.hapibrew.app.calculators.sgToBrix(1.179), 0.1)
    }

    @Test
    fun platoToSg() {
        assertEquals(1.002, beer.hapibrew.app.calculators.platoToSg(0.5), 0.01)
        assertEquals(1.014, beer.hapibrew.app.calculators.platoToSg(3.5), 0.01)
        assertEquals(1.024, beer.hapibrew.app.calculators.platoToSg(6.0), 0.01)
        assertEquals(1.036, beer.hapibrew.app.calculators.platoToSg(9.0), 0.01)
        assertEquals(1.057, beer.hapibrew.app.calculators.platoToSg(14.0), 0.01)
        assertEquals(1.101, beer.hapibrew.app.calculators.platoToSg(24.0), 0.01)
        assertEquals(1.136, beer.hapibrew.app.calculators.platoToSg(31.5), 0.01)
        assertEquals(1.179, beer.hapibrew.app.calculators.platoToSg(40.0), 0.01)
    }

    @Test
    fun brixToSg() {
        assertEquals(1.002, beer.hapibrew.app.calculators.brixToSg(0.5), 0.01)
        assertEquals(1.014, beer.hapibrew.app.calculators.brixToSg(3.5), 0.01)
        assertEquals(1.024, beer.hapibrew.app.calculators.brixToSg(6.0), 0.01)
        assertEquals(1.036, beer.hapibrew.app.calculators.brixToSg(9.0), 0.01)
        assertEquals(1.057, beer.hapibrew.app.calculators.brixToSg(14.0), 0.01)
        assertEquals(1.101, beer.hapibrew.app.calculators.brixToSg(24.0), 0.01)
        assertEquals(1.136, beer.hapibrew.app.calculators.brixToSg(31.5), 0.01)
        assertEquals(1.179, beer.hapibrew.app.calculators.brixToSg(40.0), 0.01)
    }

    @Test
    fun celsiusToFahrenheit() {
        assertEquals(32.0, beer.hapibrew.app.calculators.celsiusToFahrenheit(0.0), 0.05)
        assertEquals(37.4, beer.hapibrew.app.calculators.celsiusToFahrenheit(3.0), 0.05)
        assertEquals(68.0, beer.hapibrew.app.calculators.celsiusToFahrenheit(20.0), 0.05)
        assertEquals(105.8, beer.hapibrew.app.calculators.celsiusToFahrenheit(41.0), 0.05)
    }

    @Test
    fun fahrenheitToCelsius() {
        assertEquals(-17.78, beer.hapibrew.app.calculators.fahrenheitToCelsius(0.0), 0.01)
        assertEquals(-12.22, beer.hapibrew.app.calculators.fahrenheitToCelsius(10.0), 0.01)
        assertEquals(0.0, beer.hapibrew.app.calculators.fahrenheitToCelsius(32.0), 0.01)
        assertEquals(8.89, beer.hapibrew.app.calculators.fahrenheitToCelsius(48.0), 0.01)
    }

    @Test
    fun barToPsi() {
        assertEquals(2.9, beer.hapibrew.app.calculators.barToPsi(0.2), 0.05)
        assertEquals(14.5, beer.hapibrew.app.calculators.barToPsi(1.0), 0.05)
    }

    @Test
    fun psiToBar() {
        assertEquals(0.0, beer.hapibrew.app.calculators.psiToBar(0.0), 0.01)
        assertEquals(0.55, beer.hapibrew.app.calculators.psiToBar(8.0), 0.01)
    }

    @Test
    fun litersToGallons() {
        assertEquals(0.79, beer.hapibrew.app.calculators.litersToGallons(3.0), 0.01)
        assertEquals(2.11, beer.hapibrew.app.calculators.litersToGallons(8.0), 0.01)
    }

    @Test
    fun gallonsToLiters() {
        assertEquals(18.92, beer.hapibrew.app.calculators.gallonsToLiters(5.0), 0.01)
        assertEquals(3.78, beer.hapibrew.app.calculators.gallonsToLiters(1.0), 0.01)
    }

    @Test
    fun gramsToOunces() {
        assertEquals(0.35, beer.hapibrew.app.calculators.gramsToOunces(10.0), 0.01)
        assertEquals(4.23, beer.hapibrew.app.calculators.gramsToOunces(120.0), 0.01)
    }
}