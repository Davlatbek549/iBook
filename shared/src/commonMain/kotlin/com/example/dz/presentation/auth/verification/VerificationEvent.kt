package com.example.dz.presentation.auth.verification

sealed interface VerificationEvent {
    data class CodeChanged(val code: String) : VerificationEvent
    data object VerifyClicked : VerificationEvent
    data object ResendClicked : VerificationEvent
    data object BackClicked : VerificationEvent

    /**
     * Leaving for good, when there is no screen behind this one to go back to — the code screen
     * was opened straight from the splash on a relaunch. The half-made session is given up here
     * rather than left to send the next launch back to this screen.
     */
    data object AbandonSession : VerificationEvent
}
