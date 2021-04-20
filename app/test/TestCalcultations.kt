package tech.challenge

import com.github.michaelbull.result.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.orekit.data.DataContext
import org.orekit.data.DirectoryCrawler
import org.orekit.propagation.analytical.tle.TLE
import java.io.File


class TLEDiscoveryTests: DescribeSpec( {
    beforeTest{
        setup()
    }
    val wikiTle = """
        ISS (ZARYA)             
        1 25544U 98067A   21110.88790192  .00002325  00000-0  50487-4 0  9999
        2 25544  51.6450 266.1357 0002502 255.7332 264.5540 15.48912929279711
    """.trimIndent()
    val titlelessTle = """
        1 25544U 98067A   21110.88790192  .00002325  00000-0  50487-4 0  9999
        2 25544  51.6450 266.1357 0002502 255.7332 264.5540 15.48912929279711
    """.trimIndent()
    val badTle = """
        My Bad Tle
        1 25544U 98067A   21110.88790192  .00002325  00000-0  50487-4 0  9999
        3 25544  51.6450 266.1357 0002502 255.7332 264.5540 
    """.trimIndent()
    describe("When parsing it with OreKit") {
        it ("then give appropriate instance") {
            val preTle = wikiTle.split('\n')
            preTle shouldHaveSize 3
            val tleRes = runCatching {
                TLE(preTle[1], preTle[2])
            }
            tleRes.shouldBeTypeOf<Ok<TLE>>()
        }
    }
    describe("When parsing it with the TitledTle"){
        it("Then give appropriate tle") {
            val tleRes = TitledTLE.parse(wikiTle)
            tleRes.shouldBeTypeOf<Ok<TLE>>()
            tleRes.unwrap().title.shouldNotBe(null)
        }
        it("Should give a tle even without a title") {
            val tleRes = TitledTLE.parse(titlelessTle)
            tleRes.shouldBeTypeOf<Ok<TLE>>()
            tleRes.unwrap().title.shouldBe(null)
        }
        it("should be fine splitting but fails with bad input") {
            val tleRes = TitledTLE.parse(badTle)
            tleRes.shouldBeTypeOf<Err<TLEError>>()
        }
    }
    describe("getting multiple tles at the same time") {
        it("should return an error as soon as there is one bad apple"){
            val tleRes = getTles(listOf(wikiTle, badTle, titlelessTle))
            tleRes.shouldBeTypeOf<Err<TLEError>>()
        }
        it("Should return the list if all is good") {
            val tleRes = getTles(listOf(wikiTle, titlelessTle))
            tleRes.shouldBeTypeOf<Ok<List<TitledTLE>>>()
        }
    }
})