package beer.hapibrew.app.calculators

import beer.hapibrew.beerxml2proto.BeerXmlParser
import beer.hapibrew.beerxml2proto.proto.Recipe
import beer.hapibrew.app.extensions.cToF
import beer.hapibrew.app.extensions.gToOz
import beer.hapibrew.app.extensions.galToL
import beer.hapibrew.app.extensions.lToGal
import android.test.MoreAsserts.assertNotEmpty
import com.squareup.wire.Wire
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

class CalculatorsKtTest {
    val recipeList:ArrayList<Recipe> = ArrayList()

    @Before
    fun setUp() {
        val zokaRecipeList = BeerXmlParser().parse(javaClass.classLoader.getResourceAsStream("zoka-pale-ale.xml"))
        if (zokaRecipeList != null) {
            recipeList.addAll(zokaRecipeList)
        }
        val demoRecipeList = BeerXmlParser().parse(javaClass.classLoader.getResourceAsStream("recipes.xml"))
        if (demoRecipeList != null) {
            recipeList.addAll(demoRecipeList)
        }
        val oatmeal = BeerXmlParser().parse(javaClass.classLoader.getResourceAsStream("oatmeal-stout-large.xml"))
        if (oatmeal != null) {
            recipeList.addAll(oatmeal)
        }
        val brewdog = BeerXmlParser().parse(javaClass.classLoader.getResourceAsStream("brewdog.xml"))
        if (brewdog != null) {
            recipeList.addAll(brewdog)
        }
    }

    @Test
    fun importRecipe() {
        assertNotEmpty(recipeList)
    }

    @Test
    fun calculateAbv() {
        recipeList
                .map {
                    beer.hapibrew.app.calculators.calculateRecipeAbv(
                            Wire.get(it.batch_size, Recipe.DEFAULT_BATCH_SIZE)!!.lToGal(),
                            Wire.get(it.efficiency, Recipe.DEFAULT_EFFICIENCY)!!,
                            Wire.get(it.fermentables, emptyList()),
                            Wire.get(it.yeasts, emptyList()))
                }
                .forEach {
                    assertTrue(it.og in 1.020..1.150)
                    assertTrue(it.fg in 1.001..1.020)
                    assertTrue(it.abv in 2.0..7.0)
                }
    }

    @Test
    fun calculateSrm() {
        recipeList
                .map {
                    beer.hapibrew.app.calculators.calculateSrm(
                            Wire.get(it.batch_size, Recipe.DEFAULT_BATCH_SIZE)!!.lToGal(),
                            Wire.get(it.fermentables, emptyList()))
                }
                .forEach { assertTrue(it > 1.0 && it < 90.0) }
    }

    @Test
    fun calculateIbu() {
        recipeList
                .map {
                    beer.hapibrew.app.calculators.calculateIbu(
                            Wire.get(it.batch_size, Recipe.DEFAULT_BATCH_SIZE)!!.lToGal(),
                            Wire.get(it.efficiency, Recipe.DEFAULT_EFFICIENCY)!!,
                            Wire.get(it.boil_time, Recipe.DEFAULT_BOIL_TIME)!!,
                            Wire.get(it.fermentables, emptyList()),
                            Wire.get(it.hops, emptyList()))
                }
                .forEach { assertTrue(it > 15.0 && it < 100.0) }
    }

    @Test
    fun fixRefractometer() {
        val (og, fg, abv) = beer.hapibrew.app.calculators.fixRefractometer(1.057, 1.028, 1.04)
        assertEquals(1.055, og, 0.01)
        assertEquals(1.013, fg, 0.01)
        assertEquals(5.65, abv, 0.1)
    }

    @Test
    fun kegPressure() {
        assertEquals(5.1, beer.hapibrew.app.calculators.kegPressure(36.0, 2.0), 0.1)
        assertEquals(5.0, beer.hapibrew.app.calculators.kegPressure(2.0.cToF(), 2.0), 0.1)
    }

    @Test
    fun priming() {
        val (sucrose1, dextrose1, dme1) = beer.hapibrew.app.calculators.priming(20.0.cToF(), 19.0, 2.0)
        assertEquals(86.5, sucrose1, 0.1)
        assertEquals(95.1, dextrose1, 0.1)
        assertEquals(127.2, dme1, 0.1)
        val (sucrose2, dextrose2, dme2) = beer.hapibrew.app.calculators.priming(68.0, 5.0.galToL(), 2.0)
        assertEquals(3.0, sucrose2.gToOz(), 0.1)
        assertEquals(3.3, dextrose2.gToOz(), 0.1)
        assertEquals(4.5, dme2.gToOz(), 0.1)
    }
}