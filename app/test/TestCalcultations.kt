package tech.challenge

import com.github.michaelbull.result.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.eclipse.jetty.util.ClassVisibilityChecker
import org.hipparchus.ode.events.Action
import org.hipparchus.util.FastMath
import org.orekit.propagation.analytical.tle.TLE
import org.orekit.propagation.analytical.tle.TLEPropagator
import org.orekit.propagation.events.*
import org.orekit.time.AbsoluteDate
import org.orekit.time.TimeScale
import org.orekit.time.TimeScales
import java.util.*
import javax.swing.text.Position


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

class TLEDiscoveryTests: DescribeSpec( {
    beforeTest{
        setup()
    }

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

val mtl = Site(45.50884, -73.58781 , "Montreal")
class ElevationDiscoveryTests: DescribeSpec({
    beforeEach{
        setup()
    }
    describe("Exploring the ElevationDetector"){
        it("Exploring the api for elevation detection" ){
            val iss = TitledTLE.parse(wikiTle).unwrap()
            val propagator = TLEPropagator.selectExtrapolator(iss.tle)
            val frame = mtl.toFrame()
            var start: AbsoluteDate? = null
            var end: AbsoluteDate? = null
            val detector = ElevationExtremumDetector(60.0, 0.001,frame)
                .withHandler { s, detector, increasing ->
                    if (increasing) {
                        start = s.date
                    } else {
                        end = s.date
                    }
                    println("Visibility on "+ detector.topocentricFrame.name + if(increasing) {" begins at "} else  {" ends at "} + s.date)

                    if(increasing) {
                        Action.CONTINUE
                    } else {
                        Action.STOP
                    }
                }
            propagator.addEventDetector(detector)
            val finalState = propagator.propagate(iss.tle.date.shiftedBy(600000.0))
            println("final date: ${finalState.date}")
            start.shouldNotBe(null)
            end.shouldNotBe(null)
        }
    }
})