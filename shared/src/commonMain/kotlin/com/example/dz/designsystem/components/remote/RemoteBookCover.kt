package com.example.dz.designsystem.components.remote

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.mp.KoinPlatform

@Composable
fun RemoteBookCover(
    coverUrl: String?,
    fallback: DrawableResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val client = remember { KoinPlatform.getKoinOrNull()?.getOrNull<HttpClient>() }
    val normalizedUrl = coverUrl?.takeIf { it.isNotBlank() }
    var imageBitmap by remember(normalizedUrl) {
        mutableStateOf(normalizedUrl?.let(RemoteImageCache::get))
    }

    LaunchedEffect(normalizedUrl, client) {
        if (normalizedUrl == null || imageBitmap != null || client == null) return@LaunchedEffect

        imageBitmap = runCatching {
            val bytes = client.get(normalizedUrl).body<ByteArray>()
            decodeRemoteImageBitmap(bytes)
        }.onSuccess { bitmap ->
            if (bitmap != null) {
                RemoteImageCache.put(normalizedUrl, bitmap)
            }
        }.getOrNull()
    }

    val bitmap = imageBitmap
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(fallback),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

internal expect fun decodeRemoteImageBitmap(bytes: ByteArray): ImageBitmap?

private object RemoteImageCache {
    private val images = mutableMapOf<String, ImageBitmap>()

    fun get(url: String): ImageBitmap? = images[url]

    fun put(url: String, imageBitmap: ImageBitmap) {
        images[url] = imageBitmap
    }
}
