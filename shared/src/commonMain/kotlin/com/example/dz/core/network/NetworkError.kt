package com.example.dz.core.network

sealed interface NetworkError {
    data object NoInternet : NetworkError
    data object Timeout : NetworkError
    data object Serialization : NetworkError
    data class Unknown(val message: String? = null) : NetworkError
}
