package com.example.dz.presentation.auth.new_password

data class NewPasswordUiState(
    /** Whose password is being reset; carried through from the code screen. */
    val email: String = "",
    val password: String = "",
    val confirmation: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    /** Sits under the confirmation box — both rules it can break belong to that field. */
    val confirmationError: String? = null,
)
