package com.example.dz.designsystem.components.remote

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * A book cover from the network, with a bundled drawable shown while it loads, when it fails,
 * and when the book has no cover at all.
 *
 * Loading goes through Coil (configured in [com.example.dz.App]). It replaced a hand-written
 * loader that decoded every cover at full size on the main thread and kept every one in memory
 * for good — scrolling a list of covers meant a burst of decodes on the thread drawing the list,
 * and a heap that only grew. Coil decodes off the main thread at the size the cover is actually
 * drawn, drops what is no longer needed, and keeps a disk copy so a relaunch does not download
 * every cover again.
 */
@Composable
fun RemoteBookCover(
    coverUrl: String?,
    fallback: DrawableResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val placeholder = painterResource(fallback)
    AsyncImage(
        model = coverUrl?.takeIf { it.isNotBlank() },
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        contentScale = contentScale,
    )
}
