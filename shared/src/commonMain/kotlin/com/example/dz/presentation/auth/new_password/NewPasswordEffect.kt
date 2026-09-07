package com.example.dz.presentation.auth.new_password

sealed interface NewPasswordEffect {
    /**
     * A reset is not a sign-in. The server issues no session for one and revokes the ones that
     * existed, so the only honest place to go is the sign-in screen — with the password the
     * reader just chose.
     */
    data class NavigateToLogin(val email: String) : NewPasswordEffect
    data object NavigateBack : NewPasswordEffect
}
