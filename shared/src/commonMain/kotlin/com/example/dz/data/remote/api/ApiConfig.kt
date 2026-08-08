package com.example.dz.data.remote.api

/**
 * Runtime configuration for the app's own backend API (see `dz-server`).
 *
 * [baseUrl] already includes the `/api/v1` prefix, so [KtorAuthApi] appends
 * paths like `/auth/login` directly. It points at the deployed server by
 * default, so the app runs with nothing else started.
 *
 * To work against a server on your own machine instead, swap in
 * [localBaseUrl] — [devServerHost] resolves to whatever address this platform's
 * simulator reaches the host on.
 *
 * Setting [useMockBackend] to `true` swaps in an in-memory `MockEngine` and
 * ignores [baseUrl] entirely, which is useful for UI work offline. The same
 * [KtorAuthApi] serves all three cases.
 */
data class ApiConfig(
    val baseUrl: String = DEPLOYED_BASE_URL,
    val useMockBackend: Boolean = false
) {
    companion object {
        const val DEPLOYED_BASE_URL = "https://dz-server.onrender.com/api/v1"

        /**
         * The free tier sleeps after inactivity, so the first call after a pause
         * can take up to a minute while the server starts. Screens that hit the
         * network on load should show a loading state rather than appear stuck.
         */
        val localBaseUrl: String get() = "http://$devServerHost:8080/api/v1"
    }
}
