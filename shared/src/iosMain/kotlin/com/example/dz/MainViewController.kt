package com.example.dz

import androidx.compose.ui.window.ComposeUIViewController
import com.example.dz.core.di.startKoinIfNeeded

fun MainViewController() = ComposeUIViewController {
    startKoinIfNeeded()
    App()
}
