package com.example.dz.core.di

import com.example.dz.data.local.db.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory(get()) }
}
