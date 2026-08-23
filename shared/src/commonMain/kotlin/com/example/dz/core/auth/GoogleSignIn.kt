package com.example.dz.core.auth

import androidx.compose.runtime.Composable

/**
 * Outcome of asking the platform for a Google ID token.
 *
 * Cancellation is deliberately not an error: someone who opens the account picker and changes
 * their mind has not hit a failure, and showing them one reads as a bug.
 */
sealed interface GoogleSignInResult {
    /** The signed assertion, to be proven by the server before anything is believed. */
    data class Success(val idToken: String) : GoogleSignInResult

    data object Cancelled : GoogleSignInResult

    data class Failed(val message: String) : GoogleSignInResult

    /** No implementation on this platform yet, so the button should not offer itself. */
    data object Unsupported : GoogleSignInResult
}

/**
 * The one part of Google sign-in that cannot be shared: showing the account picker.
 *
 * Everything after this point — trading the token for a session, storing it, refreshing it — is
 * common code, so each platform implements only this.
 */
interface GoogleSignInClient {
    /** True where the platform can actually present a picker; drives whether the button is live. */
    val isAvailable: Boolean

    suspend fun requestIdToken(): GoogleSignInResult
}

/**
 * Composable because the Android implementation needs the hosting activity, which is only
 * reachable from composition.
 */
@Composable
expect fun rememberGoogleSignInClient(): GoogleSignInClient
