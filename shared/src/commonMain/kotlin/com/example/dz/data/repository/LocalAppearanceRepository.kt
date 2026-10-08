package com.example.dz.data.repository

import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.Appearance
import com.example.dz.domain.repository.AppearanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * The appearance in the device's key-value store, read once when the app starts and held from
 * then on, so the root can draw its first frame in it.
 *
 * It belongs to the device rather than to whoever is signed in — it is about this screen, like
 * having seen the onboarding — so signing out keeps it, and so does deleting an account: its key
 * is outside the prefixes that [LocalDataSource.clearUserData] erases.
 */
class LocalAppearanceRepository(
    private val local: LocalDataSource,
) : AppearanceRepository {

    private val current = MutableStateFlow(read())

    override val appearance: StateFlow<Appearance> = current.asStateFlow()

    override fun setAppearance(appearance: Appearance) {
        local.saveSetting(KEY, appearance.name)
        current.value = appearance
    }

    private fun read(): Appearance =
        local.getSetting(KEY)
            .takeIf { it.isNotBlank() }
            // An unknown name is a choice this version cannot draw; following the device is a
            // better answer than a crash.
            ?.let { name -> Appearance.entries.firstOrNull { it.name == name } }
            ?: Appearance.SYSTEM

    companion object {
        const val KEY = "appearance"
    }
}
