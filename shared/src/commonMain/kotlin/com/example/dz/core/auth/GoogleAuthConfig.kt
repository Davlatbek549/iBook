package com.example.dz.core.auth

/**
 * The **Web** OAuth client id from the Google console — the same value the server checks tokens
 * against. Android ID tokens carry the web client in `aud`, not the Android one, which is the
 * usual point of confusion.
 *
 * Not a secret: it ships inside every copy of the app and is visible in any network trace. Its
 * job is identifying the project, not proving anything.
 */
object GoogleAuthConfig {
    const val SERVER_CLIENT_ID =
        "169301208092-ed5g27rr81ptcm2k3gfoiruhibgcl2g2.apps.googleusercontent.com"
}
