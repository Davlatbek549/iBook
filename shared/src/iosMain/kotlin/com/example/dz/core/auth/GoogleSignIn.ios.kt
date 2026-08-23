package com.example.dz.core.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * What Swift implements so Kotlin can reach GoogleSignIn-iOS.
 *
 * Kotlin/Native cannot call Swift directly — the dependency only runs the other way — so the
 * contract is declared here and satisfied on the Swift side, which conforms to it through the
 * generated Objective-C header.
 *
 * Callback-shaped rather than suspending because a Kotlin `suspend` function is exposed to Swift
 * as a completion handler anyway; taking one directly keeps the Swift implementation ordinary.
 *
 * Exactly one of [idToken] or [error] is non-null. Both null means the person dismissed the
 * sheet, which is a choice rather than a failure.
 */
interface GoogleSignInBridge {
    /**
     * False while the implementation is registered but cannot actually sign anyone in — the
     * state until GoogleSignIn-iOS is added to `iosApp`. Registration alone is not capability,
     * and a button that looks live but always fails is worse than one that is plainly disabled.
     */
    val isSupported: Boolean

    fun signIn(onResult: (idToken: String?, error: String?) -> Unit)
}

/**
 * Where `iOSApp.swift` hands its implementation in at start-up.
 *
 * A registry rather than constructor injection because the Compose entry point is built inside
 * the shared module, so there is no seam on the iOS side to pass it through.
 */
object GoogleSignInBridgeRegistry {
    var bridge: GoogleSignInBridge? = null
}

/**
 * Delegates to whatever Swift registered. With nothing registered — the state until the SDK is
 * added to `iosApp` — this reports [GoogleSignInResult.Unsupported], which renders the button
 * inert instead of letting it fail on tap.
 */
private object IosGoogleSignInClient : GoogleSignInClient {

    override val isAvailable: Boolean
        get() = GoogleSignInBridgeRegistry.bridge?.isSupported == true

    override suspend fun requestIdToken(): GoogleSignInResult {
        val bridge = GoogleSignInBridgeRegistry.bridge ?: return GoogleSignInResult.Unsupported

        return suspendCancellableCoroutine { continuation ->
            bridge.signIn { idToken, error ->
                // Guarded because a misbehaving implementation calling back twice would
                // otherwise crash on the second resume.
                if (!continuation.isActive) return@signIn

                continuation.resume(
                    when {
                        idToken != null -> GoogleSignInResult.Success(idToken)
                        error != null -> GoogleSignInResult.Failed(error)
                        else -> GoogleSignInResult.Cancelled
                    }
                )
            }
        }
    }
}

@Composable
actual fun rememberGoogleSignInClient(): GoogleSignInClient = remember { IosGoogleSignInClient }
