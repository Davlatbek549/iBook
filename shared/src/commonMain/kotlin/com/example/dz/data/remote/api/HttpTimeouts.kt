package com.example.dz.data.remote.api

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout

/**
 * Timeouts every client installs, because the engine defaults do not agree across platforms:
 * left unset, the same slow response gives up after ten seconds on one and minutes on the other,
 * and neither number is one anybody chose.
 *
 * The values are sized for a free-tier host that sleeps. A container that has spun down needs
 * upwards of a minute to pull an image, start a JVM and open its pool, and the database behind it
 * wakes on the first query — so a request arriving cold is slow for reasons that have nothing to
 * do with the reader's connection. Giving up at ten seconds reports a network failure that has
 * not happened, and worse, abandons a request the server goes on to finish: a sign-up that
 * "failed" this way still creates the account, and the next attempt is refused as a duplicate.
 */
internal object HttpTimeouts {

    /**
     * Opening the socket. Short on purpose and unrelated to how slow the server is: reaching a
     * host that is awake takes well under a second, so failing here really is the connection.
     */
    const val CONNECT_MILLIS = 20_000L

    /** Silence between bytes once connected — long enough to cover a cold start. */
    const val SOCKET_MILLIS = 120_000L

    /** The whole round trip, the backstop that stops a request hanging indefinitely. */
    const val REQUEST_MILLIS = 150_000L
}

/** Applies [HttpTimeouts] to a client under construction. */
internal fun HttpClientConfig<*>.installDefaultTimeouts() {
    install(HttpTimeout) {
        connectTimeoutMillis = HttpTimeouts.CONNECT_MILLIS
        socketTimeoutMillis = HttpTimeouts.SOCKET_MILLIS
        requestTimeoutMillis = HttpTimeouts.REQUEST_MILLIS
    }
}
