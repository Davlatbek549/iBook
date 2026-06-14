package com.example.dz.core.error

sealed interface AppError {
    data object Network : AppError
    data object NotFound : AppError
    data object Unauthorized : AppError
    data class Unknown(val message: String? = null) : AppError
}
