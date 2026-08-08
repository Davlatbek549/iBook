package com.example.dz

import com.example.dz.data.local.LocalDataSource

/**
 * In-memory [LocalDataSource] shared by the auth tests, so they exercise session storage
 * without touching platform settings.
 */
internal class FakeLocalDataSource : LocalDataSource {
    private val values = mutableMapOf<String, String>()

    override fun getToken(): String? = values["token"]

    override fun getRefreshToken(): String? = values["refreshToken"]

    override fun saveTokens(token: String, refreshToken: String?) {
        values["token"] = token
        if (refreshToken == null) values.remove("refreshToken") else values["refreshToken"] = refreshToken
    }

    override fun getUserId(): String? = values["userId"]
    override fun getUserEmail(): String? = values["email"]
    override fun getUserName(): String? = values["name"]

    override fun saveUserSession(
        userId: String,
        name: String,
        email: String,
        token: String,
        refreshToken: String?
    ) {
        saveTokens(token, refreshToken)
        values["userId"] = userId
        values["name"] = name
        values["email"] = email
    }

    override fun isLoggedIn(): Boolean = !values["token"].isNullOrEmpty()
    override fun clearSession() { values.clear() }
    override fun getSetting(key: String, default: String): String = values[key] ?: default
    override fun saveSetting(key: String, value: String) { values[key] = value }
    override fun removeSetting(key: String) { values.remove(key) }
}
