package com.example.dz.domain.repository

import com.example.dz.domain.model.Appearance
import kotlinx.coroutines.flow.StateFlow

/** Reads and writes which [Appearance] this device draws the app in. No network, no account. */
interface AppearanceRepository {
    /**
     * The current choice. A [StateFlow] so the app's first frame can be drawn in it straight away,
     * rather than drawn once in the device's appearance and then again in the chosen one.
     */
    val appearance: StateFlow<Appearance>

    fun setAppearance(appearance: Appearance)
}
