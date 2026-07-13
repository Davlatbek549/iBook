package com.example.dz.core.di

import com.example.dz.data.local.db.DatabaseDriverFactory
import com.example.dz.data.local.file.BookFileStorage
import com.example.dz.data.local.file.FileStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseDriverFactory(androidContext()) }
    single<FileStorage> { BookFileStorage(androidContext()) }
}
