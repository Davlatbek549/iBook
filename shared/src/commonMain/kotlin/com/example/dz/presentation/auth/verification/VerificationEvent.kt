package com.example.dz.presentation.auth.verification

sealed interface VerificationEvent {
    data class CodeChanged(val code: String) : VerificationEvent
    data object VerifyClicked : VerificationEvent
    data object ResendClicked : VerificationEvent
    data object BackClicked : VerificationEvent
}
