package tech.challenge

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.runCatching
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.orekit.data.DataContext
import org.orekit.data.DirectoryCrawler
import org.orekit.propagation.analytical.tle.TLE
import java.io.File


class TLEDiscoveryTests: DescribeSpec( {
    val wikiTle = """
        ISS (ZARYA)             
        1 25544U 98067A   21110.88790192  .00002325  00000-0  50487-4 0  9999
        2 25544  51.6450 266.1357 0002502 255.7332 264.5540 15.48912929279711
    """.trimIndent()
    describe("When parsing it with OreKit") {
        it ("then give appropriate instance") {
            setup()
            val preTle = wikiTle.split('\n')
            preTle shouldHaveSize 3
            val tleRes = runCatching {
                TLE(preTle[1], preTle[2])
            }
            tleRes.shouldBeTypeOf<Ok<TLE>>()

        }
    }
})