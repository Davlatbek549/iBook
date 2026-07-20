package com.example.dz.data.remote.api

/**
 * Configuration for Firebase Authentication (Identity Toolkit REST API).
 *
 * The Web API key is not a secret — it only identifies the Firebase project; access is guarded
 * server-side by Firebase. Get it from Firebase Console → Project settings → General → Web API key
 * and paste it into [apiKey]. Auth goes live once [ApiConfig.useMockBackend] is set to `false`.
 */
data class FirebaseConfig(
    val apiKey: String = "AIzaSyDMyfDbGrV6o8PQ03-scaX9FwRvkVpz_eI",
    val baseUrl: String = "https://identitytoolkit.googleapis.com/v1"
) {
    val isConfigured: Boolean
        get() = apiKey.isNotBlank() && apiKey != "PASTE_FIREBASE_WEB_API_KEY"
}
