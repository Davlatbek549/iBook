package com.example.dz.presentation.auth.sign_up

sealed interface SignUpEffect {
    /**
     * Carries the address so the code screen can name it rather than saying "your email".
     *
     * [accountJustCreated] is true only when the form made the account. The code screen then
     * deletes it if the reader backs out, which is safe for an account that did not exist a
     * moment ago and never for one Google signed them in to — that may be years old.
     */
    data class NavigateToVerification(
        val email: String,
        val accountJustCreated: Boolean,
    ) : SignUpEffect
    /** Google-verified accounts skip code entry — there is nothing left to prove. */
    data object NavigateToHome : SignUpEffect
    data object NavigateToLogin : SignUpEffect
}
