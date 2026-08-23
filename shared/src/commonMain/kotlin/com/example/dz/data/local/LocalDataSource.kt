package com.example.dz.data.local

interface LocalDataSource {
    fun getToken(): String?
    fun saveToken(token: String)
    fun getUserId(): String?
    fun getUserEmail(): String?
    fun getUserName(): String?
    fun saveUserSession(userId: String, name: String, email: String, token: String)
    fun isLoggedIn(): Boolean
    fun clearSession()
    fun getSetting(key: String, default: String = ""): String
    fun saveSetting(key: String, value: String)
    fun removeSetting(key: String)
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)
}
