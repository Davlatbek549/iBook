package com.example.dz.presentation.auth.new_password

import com.example.dz.presentation.auth.passwordStrength

/**
 * How long the success moment holds before the screen leaves for sign-in.
 *
 * Long enough to be read and to let the overlay finish arriving, short enough that nobody reaches
 * for the screen to hurry it along. Nothing is tappable while it shows, so this is the whole of
 * the wait — it is not a dialog anyone has to dismiss.
 */
const val RESET_SUCCESS_DWELL_MILLIS = 1500L

data class NewPasswordUiState(
    /** Whose password is being reset; carried through from the code screen. */
    val email: String = "",
    val password: String = "",
    val confirmation: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    /** Sits under the confirmation box — both rules it can break belong to that field. */
    val confirmationError: String? = null,
    /**
     * Set once the server has taken the new password. It shows the success overlay and stops a
     * second save being sent, for the [RESET_SUCCESS_DWELL_MILLIS] the moment is held.
     */
    val isSaved: Boolean = false,
) {
    /** Lit segments of the three-bar meter, on the same rule sign-up uses to judge a password. */
    val passwordStrength: Int get() = passwordStrength(password)
}
