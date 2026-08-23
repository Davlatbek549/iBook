package com.example.dz.presentation.auth.sign_up

sealed interface SignUpEffect {
    /** Carries the address so the code screen can name it rather than saying "your email". */
    data class NavigateToVerification(val email: String) : SignUpEffect
    /** Google-verified accounts skip code entry — there is nothing left to prove. */
    data object NavigateToHome : SignUpEffect
    data object NavigateToLogin : SignUpEffect
}
