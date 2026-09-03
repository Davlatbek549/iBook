package com.example.dz.core.di

import org.koin.core.module.Module

/**
 * Platform-provided dependencies that need a native handle (Android [android.content.Context], iOS
 * file system). Android supplies a `Context` via `androidContext()` in `startKoin`; iOS constructs
 * the actuals directly. Loaded alongside [coreModule] in each platform's Koin start-up.
 */
expect val platformModule: Module
