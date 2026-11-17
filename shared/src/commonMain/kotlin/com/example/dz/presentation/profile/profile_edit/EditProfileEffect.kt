package com.example.dz.presentation.profile.profile_edit

sealed interface EditProfileEffect {
    /** Leave without saving: nothing changed, or the changes were discarded. */
    data object NavigateBack : EditProfileEffect

    /** Saved. The screen behind says so ("Profile updated"). */
    data object Saved : EditProfileEffect
}
