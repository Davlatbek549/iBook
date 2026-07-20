package com.example.dz.core.error

sealed interface AppError {
    data object Network : AppError
    data object NotFound : AppError
    data object Unauthorized : AppError
    data class Unknown(val message: String? = null) : AppError

    /**
     * Auth backend rejected the credentials or the account operation. [reason] is specific enough
     * for the UI to show distinct wrong-password / duplicate-email / weak-password states.
     */
    data class Auth(val reason: AuthReason) : AppError

    enum class AuthReason {
        InvalidCredentials,
        EmailAlreadyInUse,
        InvalidEmail,
        WeakPassword,
        UserDisabled,
        TooManyAttempts,
        Unknown
    }
}
