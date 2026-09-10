package com.example.dz.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String? = null,
    val avatarUrl: String? = null,
    /**
     * False until a code has been spent. The session exists either way — sign-up issues one so
     * the reader can hold it while they read their mail — but the server refuses everything else
     * until this is true, so the app has to send them back to finish rather than open Home.
     */
    val emailVerified: Boolean = false,
)
