package tech.challenge

import com.github.michaelbull.result.*
import org.hipparchus.util.FastMath
import org.orekit.bodies.GeodeticPoint
import org.orekit.frames.TopocentricFrame
import org.orekit.propagation.analytical.tle.TLE
import kotlin.math.PI

/**
 * Wrapper around a TLE in order to keep the title.
 */
class TitledTLE private constructor(val title: String?, val tle: TLE,){
    companion object {
        fun parse(data: String): Result<TitledTLE, TLEError> = binding {
            val separated = data.split('\n')
            when (separated.size) {
                2 -> TitledTLE(null,toTle(separated[0], separated[1]).bind())
                3 -> TitledTLE(separated[0], toTle(separated[1], separated[2]).bind())
                else -> Err(TLEError(data,"ErrorInput must be between 2 to 3 lines ")).bind()
            }
        }
        private fun toTle(line1: String, line2: String): Result<TLE, TLEError> = runCatching {
            TLE(line1, line2)
        }.mapError { TLEError(toString(), it.toString()) }
    }
}


// 0 in altitude since we don't get it.
private fun Site.toGeodeticPoint() = GeodeticPoint(FastMath.toRadians(lat), FastMath.toRadians(lng), 0.0)

/**
 * Convert a Site to a topological frame
 */
fun Site.toFrame() = TopocentricFrame(earth, toGeodeticPoint(), name)
