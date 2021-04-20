package tech.challenge

import kotlinx.serialization.Serializable

@Serializable
data class Bundle(
    // list of Two-line elements
    val tles: List<String>,
    val targets: List<Site>,
)

@Serializable
data class Site(
    /// Latitude
    val lat: Double,
    /// Longitude
    val lng: Double,
    /// Site name
    val name: String,
)

@Serializable
data class OverheadPass(
    /// Start date in string, because kotlinx.serialization does not support date times
    val start: String,
    /// End date in string
    val end: String,
    /// Site Name
    val side: String,
)