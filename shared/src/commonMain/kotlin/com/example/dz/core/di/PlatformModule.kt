package com.example.dz.core.di

import org.koin.core.module.Module

/**
 * Platform-only bindings that need a native construction path — e.g. [com.example.dz.data.local.db.DatabaseDriverFactory],
 * which requires an Android `Context` on Android and nothing on iOS. Included alongside [coreModule]
 * at each platform's Koin start-up.
 */
expect fun platformModule(): Module
