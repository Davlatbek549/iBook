package com.example.dz

import android.app.Application
import com.example.dz.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IBookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IBookApplication)
            modules(coreModule)
        }
    }
}
