package tech.challenge

sealed class Error

data class TLEError(val badData: String, val message: String): Error()
