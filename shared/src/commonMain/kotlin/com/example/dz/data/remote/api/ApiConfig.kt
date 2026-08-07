package com.example.dz.data.remote.api

/**
 * Runtime configuration for the app's own backend API (see `dz-server`).
 *
 * [baseUrl] already includes the `/api/v1` prefix, so [KtorAuthApi] appends
 * paths like `/auth/login` directly. The default points at a server running on
 * the developer's machine via [devServerHost]; set it to the deployed URL for
 * anything else.
 *
 * Setting [useMockBackend] back to `true` swaps in an in-memory `MockEngine`,
 * which is useful for UI work with no server running. Nothing else changes:
 * the same [KtorAuthApi] handles both.
 */
data class ApiConfig(
    val baseUrl: String = "http://$devServerHost:8080/api/v1",
    val useMockBackend: Boolean = false
)
