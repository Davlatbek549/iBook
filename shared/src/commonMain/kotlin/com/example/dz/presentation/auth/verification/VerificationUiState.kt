package com.example.dz.presentation.auth.verification

const val VERIFICATION_CODE_LENGTH = 4
const val VERIFICATION_RESEND_SECONDS = 45

data class VerificationUiState(
    val code: String = "",
    val secondsLeft: Int = VERIFICATION_RESEND_SECONDS,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val canResend: Boolean get() = secondsLeft == 0
}
