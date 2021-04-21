package tech.challenge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Error

@Serializable
@SerialName("TLEError")
data class TLEError(val badData: String, val message: String): Error()

// Separated to add more context later on
@Serializable
data class ErrorMessage (
    val error: Error,
)
