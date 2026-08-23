package com.example.dz.presentation.auth.sign_up

import com.example.dz.core.common.AppConstants
import com.example.dz.core.legal.LEGAL_DOCUMENTS_VERSION
import com.example.dz.core.legal.LegalDocumentKind

data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    /**
     * Which version of the documents was agreed to, rather than a bare "yes". When the wording
     * changes the constant moves, and a stale acceptance stops counting on its own.
     */
    val acceptedTermsVersion: Int? = null,
    /** Non-null while a document is open; the sheet shows whichever one was asked for. */
    val openDocument: LegalDocumentKind? = null,
    val isLoading: Boolean = false,
    /** The server's verdict. */
    val errorMessage: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
) {
    val termsAccepted: Boolean get() = acceptedTermsVersion == LEGAL_DOCUMENTS_VERSION

    val canSubmit: Boolean get() = termsAccepted && !isLoading

    /** Lit segments of the three-bar meter, 0 while the password is still too short to accept. */
    val passwordStrength: Int
        get() = when {
            password.isEmpty() -> 0
            password.length < AppConstants.PASSWORD_MIN_LENGTH -> 0
            password.length < AppConstants.PASSWORD_MIN_LENGTH + 3 -> 1
            password.length < AppConstants.PASSWORD_MIN_LENGTH + 7 -> 2
            else -> 3
        }
}
