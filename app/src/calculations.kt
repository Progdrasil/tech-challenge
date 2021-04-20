package tech.challenge

import com.github.michaelbull.result.*

fun calculateOverheadPasses(bundle: Bundle): Result<List<OverheadPass>, Error> = binding {
    val tles = getTles(bundle.tles).bind()



    emptyList()
}

fun getTles(data: List<String>) = data.map(TitledTLE::parse).combine()