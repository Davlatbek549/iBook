package com.example.dz.data.remote.api

/**
 * Runtime configuration for the app's own backend API.
 *
 * While [useMockBackend] is `true`, auth requests are served by an in-memory Ktor `MockEngine`.
 * Setting it to `false` routes auth to Firebase Authentication instead (see [FirebaseConfig] —
 * the Web API key must be set first). [baseUrl] is kept for future non-auth endpoints
 * (Phase 5 sync APIs).
 */
data class ApiConfig(
    val baseUrl: String = "https://api.dz.example.com",
    val useMockBackend: Boolean = true
)
