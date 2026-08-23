package com.example.dz.presentation.auth.new_password

sealed interface NewPasswordEffect {
    /** Saving signs the reader in, so they never re-type what they just chose. */
    data object NavigateToHome : NewPasswordEffect
    data object NavigateBack : NewPasswordEffect
}
