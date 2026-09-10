package com.example.dz.presentation.auth.sign_up

import com.example.dz.core.legal.LEGAL_DOCUMENTS_VERSION
import com.example.dz.presentation.auth.passwordStrength
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
    /**
     * The documents that have been read to the end and agreed to, one entry each.
     *
     * Consent needs every one of them. The sentence beside the box names the terms *and* the
     * privacy policy, and reaching the foot of one says nothing about the other — so agreement
     * is collected per document and only adds up once none is outstanding.
     */
    val agreedDocuments: Set<LegalDocumentKind> = emptySet(),
    val isLoading: Boolean = false,
    /** The server's verdict. */
    val errorMessage: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
) {
    val termsAccepted: Boolean get() = acceptedTermsVersion == LEGAL_DOCUMENTS_VERSION

    /**
     * Whichever document has still to be read, or null once both have been. Enum order decides
     * which comes first, so the two links and the box all send a reader the same way through.
     */
    val nextUnreadDocument: LegalDocumentKind?
        get() = LegalDocumentKind.entries.firstOrNull { it !in agreedDocuments }

    val canSubmit: Boolean get() = termsAccepted && !isLoading

    /** Lit segments of the three-bar meter, 0 while the password is still too short to accept. */
    val passwordStrength: Int get() = passwordStrength(password)
}
