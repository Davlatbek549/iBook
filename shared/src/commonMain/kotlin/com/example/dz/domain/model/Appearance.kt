package com.example.dz.domain.model

/**
 * Whether the app is drawn light or dark.
 *
 * [SYSTEM] follows the device, and is where everyone starts. The other two hold the app to one
 * appearance whatever the device is set to.
 *
 * Stored by name, like [PageTheme], so the order here is free to change.
 */
enum class Appearance { SYSTEM, LIGHT, DARK }
