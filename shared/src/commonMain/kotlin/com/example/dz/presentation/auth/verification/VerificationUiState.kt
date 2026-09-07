package com.example.dz.presentation.auth.verification

import com.example.dz.core.auth.MailedCodeKind

/**
 * Six rather than four. This code guards the two most attacked doors in the app — taking over an
 * account by reset, and proving an address — and four digits is ten thousand guesses. Six is a
 * million, which the server's attempt cap turns into a hopeless proposition.
 */
const val VERIFICATION_CODE_LENGTH = 6
const val VERIFICATION_RESEND_SECONDS = 45

/** Which flow sent the reader here; decides where a correct code lets them out. */
enum class VerificationPurpose {
    /** After sign-up — the session already exists, so a good code lands on Home. */
    VerifyEmail,

    /** Inside a password reset — a good code leads to choosing the new password. */
    ResetPassword,
}

/** Which of the server's two codes this flow is waiting on, and so whose cooldown applies. */
val VerificationPurpose.mailedCodeKind: MailedCodeKind
    get() = when (this) {
        VerificationPurpose.VerifyEmail -> MailedCodeKind.EmailVerification
        VerificationPurpose.ResetPassword -> MailedCodeKind.PasswordReset
    }

data class VerificationUiState(
    /** Blank when the caller had no address to pass; the copy falls back to "your email address". */
    val email: String = "",
    val purpose: VerificationPurpose = VerificationPurpose.VerifyEmail,
    val code: String = "",
    val secondsLeft: Int = VERIFICATION_RESEND_SECONDS,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val canResend: Boolean get() = secondsLeft == 0

    val isComplete: Boolean get() = code.length == VERIFICATION_CODE_LENGTH

    /**
     * m:ss. The old screen printed a bare `"0:"` in front of the seconds, so any wait of a
     * minute or more rendered as `0:75`.
     */
    val countdown: String
        get() = "${secondsLeft / 60}:${(secondsLeft % 60).toString().padStart(2, '0')}"
}
