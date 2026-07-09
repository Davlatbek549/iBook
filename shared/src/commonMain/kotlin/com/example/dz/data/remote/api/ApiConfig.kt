package com.example.dz.data.remote.api

/**
 * Runtime configuration for the app's own backend API.
 *
 * There is no server yet, so [useMockBackend] defaults to `true` and requests are served by an
 * in-memory Ktor `MockEngine`. To go live, set [useMockBackend] to `false` and point [baseUrl]
 * at the real API — no other code changes are required.
 */
data class ApiConfig(
    val baseUrl: String = "https://api.dz.example.com",
    val useMockBackend: Boolean = true
)
