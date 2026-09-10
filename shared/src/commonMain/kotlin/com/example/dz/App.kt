package com.example.dz

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.example.dz.presentation.navigation.DZNavGraph
import com.example.dz.designsystem.theme.DZTheme

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

    DZTheme {
        DZNavGraph()
    }
}
