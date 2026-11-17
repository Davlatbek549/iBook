package com.example.dz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.dz.designsystem.modifiers.hideKeyboardOnBackgroundTap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import com.example.dz.data.sync.ReadingDataSync
import org.koin.mp.KoinPlatform
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

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                KoinPlatform.getKoin().get<ReadingDataSync>().syncNow()
                delay(30_000)
            }
        }
    }

    DZTheme {
        // A tap on empty space on any screen puts the keyboard away.
        Box(Modifier.fillMaxSize().hideKeyboardOnBackgroundTap()) {
            DZNavGraph()
        }
    }
}
