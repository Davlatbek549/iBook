package com.example.dz.core.platform

/**
 * The ISO 3166 alpha-2 region the device is set to, e.g. "GB", or `null` when it has none.
 * Edit Profile starts a new phone number's dial code from it.
 */
expect fun deviceRegionCode(): String?
