package com.example.dz

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.example.dz.core.platform.PlatformAppearance
import com.example.dz.domain.model.Appearance
import com.example.dz.domain.repository.AppearanceRepository
import com.example.dz.presentation.navigation.DZNavGraph
import com.example.dz.designsystem.theme.DZTheme
import org.koin.mp.KoinPlatform

@Composable
fun App() {
    // Registered before anything draws an image. The fetcher is registered by hand rather than
    // left to discovery, which is not guaranteed on iOS. Covers fade in rather than popping into
    // place — in a list scrolling past, the pop is what reads as a stutter.
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .crossfade(true)
            .build()
    }

    // The reader's choice from Settings, read before the first frame so the app opens in it
    // rather than flashing the device's appearance first.
    val appearances = remember { KoinPlatform.getKoin().get<AppearanceRepository>() }
    val appearance by appearances.appearance.collectAsState()
    val darkTheme = when (appearance) {
        Appearance.SYSTEM -> isSystemInDarkTheme()
        Appearance.LIGHT -> false
        Appearance.DARK -> true
    }

    PlatformAppearance(appearance = appearance, darkTheme = darkTheme)
    DZTheme(darkTheme = darkTheme) {
        DZNavGraph()
    }
}
