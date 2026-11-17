package com.example.dz.core.error

sealed interface AppError {
    data object Network : AppError

    /**
     * The request ran out of time rather than failing. Distinct from [Network] because the
     * connection is fine and the advice is different: waiting helps, checking the wifi does not.
     * It also says nothing about whether the server did the work — a sign-up that times out may
     * well have created the account.
     */
    data object Timeout : AppError
    data object NotFound : AppError
    data object Unauthorized : AppError
    data class Unknown(val message: String? = null) : AppError

    /**
     * Auth backend rejected the credentials or the account operation. [reason] is specific enough
     * for the UI to show distinct wrong-password / duplicate-email / weak-password states.
     */
    data class Auth(val reason: AuthReason) : AppError

    /**
     * dz-server refused a profile edit for a reason Edit Profile shows against one field rather
     * than as a general failure.
     */
    data class Profile(val reason: ProfileReason) : AppError

    enum class ProfileReason {
        UsernameTaken,
        DateOfBirthLocked,
        GenderLocked,
        InvalidDateOfBirth,

        /** Validation failed on something the app should have caught first. */
        Invalid,
    }

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
