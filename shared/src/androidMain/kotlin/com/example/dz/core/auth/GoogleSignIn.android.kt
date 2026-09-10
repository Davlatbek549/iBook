package com.example.dz.core.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Android account picker, via Credential Manager. The older `GoogleSignInClient` is deprecated.
 *
 * The token this returns is *not* trusted here — it goes straight to the server, which proves the
 * signature before believing any of it. Nothing on the device is in a position to verify it.
 */
private class AndroidGoogleSignInClient(
    private val activity: Activity,
    private val serverClientId: String,
) : GoogleSignInClient {

    override val isAvailable: Boolean get() = serverClientId.isNotBlank()

    override suspend fun requestIdToken(): GoogleSignInResult {
        if (!isAvailable) return GoogleSignInResult.Unsupported

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    // The Web client id, not the Android one: it becomes the token's `aud`,
                    // and the server checks exactly that.
                    .setServerClientId(serverClientId)
                    // False so every Google account on the device is offered. Filtering to
                    // previously-used ones shows an empty sheet on a fresh install.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        return try {
            val response = CredentialManager.create(activity).getCredential(activity, request)
            val credential = response.credential
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                GoogleSignInResult.Success(
                    GoogleIdTokenCredential.createFrom(credential.data).idToken
                )
            } else {
                GoogleSignInResult.Failed("Unexpected credential type")
            }
        } catch (_: GetCredentialCancellationException) {
            // Dismissing the sheet is a choice, not a failure.
            GoogleSignInResult.Cancelled
        } catch (_: NoCredentialException) {
            GoogleSignInResult.Failed("No Google account on this device")
        } catch (error: GetCredentialException) {
            GoogleSignInResult.Failed(error.message ?: "Google sign-in failed")
        }
    }
}

@Composable
actual fun rememberGoogleSignInClient(): GoogleSignInClient {
    val context = LocalContext.current
    return remember(context) {
        val activity = context.findActivity()
        if (activity == null) {
            UnavailableGoogleSignInClient
        } else {
            AndroidGoogleSignInClient(activity, GoogleAuthConfig.SERVER_CLIENT_ID)
        }
    }
}

/** Composition can sit behind wrappers, so the activity is walked to rather than cast to. */
private fun Context.findActivity(): Activity? {
    var current = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}

private object UnavailableGoogleSignInClient : GoogleSignInClient {
    override val isAvailable = false
    override suspend fun requestIdToken() = GoogleSignInResult.Unsupported
}
