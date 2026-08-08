package com.example.dz.data.local

import com.russhwolf.settings.Settings

private object Keys {
    const val TOKEN = "auth_token"
    const val REFRESH_TOKEN = "auth_refresh_token"
    const val USER_ID = "user_id"
    const val USER_EMAIL = "user_email"
    const val USER_NAME = "user_name"
    const val LOGGED_IN = "is_logged_in"
}

class LocalDataSourceImpl(private val settings: Settings) : LocalDataSource {

    override fun getToken(): String? = settings.getStringOrNull(Keys.TOKEN)

    override fun getRefreshToken(): String? = settings.getStringOrNull(Keys.REFRESH_TOKEN)

    override fun saveTokens(token: String, refreshToken: String?) {
        settings.putString(Keys.TOKEN, token)
        if (refreshToken == null) {
            settings.remove(Keys.REFRESH_TOKEN)
        } else {
            settings.putString(Keys.REFRESH_TOKEN, refreshToken)
        }
    }

    override fun getUserId(): String? = settings.getStringOrNull(Keys.USER_ID)

    override fun getUserEmail(): String? = settings.getStringOrNull(Keys.USER_EMAIL)

    override fun getUserName(): String? = settings.getStringOrNull(Keys.USER_NAME)

    override fun saveUserSession(
        userId: String,
        name: String,
        email: String,
        token: String,
        refreshToken: String?
    ) {
        saveTokens(token, refreshToken)
        settings.putString(Keys.USER_ID, userId)
        settings.putString(Keys.USER_EMAIL, email)
        settings.putString(Keys.USER_NAME, name)
        settings.putBoolean(Keys.LOGGED_IN, true)
    }

    override fun isLoggedIn(): Boolean = settings.getBoolean(Keys.LOGGED_IN, false)

    override fun clearSession() {
        settings.remove(Keys.TOKEN)
        settings.remove(Keys.REFRESH_TOKEN)
        settings.remove(Keys.USER_ID)
        settings.remove(Keys.USER_EMAIL)
        settings.remove(Keys.USER_NAME)
        settings.putBoolean(Keys.LOGGED_IN, false)
    }

    override fun getSetting(key: String, default: String): String =
        settings.getString(key, default)

    override fun saveSetting(key: String, value: String) {
        settings.putString(key, value)
    }

    override fun removeSetting(key: String) {
        settings.remove(key)
    }
}
