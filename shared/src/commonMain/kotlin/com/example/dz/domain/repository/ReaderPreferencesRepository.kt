package com.example.dz.domain.repository

import com.example.dz.domain.model.ReaderPreferences

/** Reads and writes how this device sets a page. No network, no account. */
interface ReaderPreferencesRepository {
    fun get(): ReaderPreferences
    fun save(preferences: ReaderPreferences)
}
