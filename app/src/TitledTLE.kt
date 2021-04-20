package tech.challenge

import com.github.michaelbull.result.*
import org.orekit.propagation.analytical.tle.TLE


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