package com.example.dz.core.media

import androidx.compose.runtime.Composable

/**
 * A picture from the camera or the library, made ready to work with: upright, a JPEG, and no
 * larger than [MAX_SIDE] on its long side.
 *
 * Upright because the server reads pixels, not the camera's orientation tag, so a phone photo
 * sent as it was taken would arrive on its side. JPEG because the server takes JPEG or PNG
 * only, and libraries hand out HEIC and WebP too. Capped because a 48-megapixel photo is far
 * more than a 1500×500 banner needs, and the server refuses anything over 4000 pixels a side.
 *
 * This is not yet the shape the server stores. Cropping it to a square or a 3:1 banner comes
 * after, so the picture is kept large enough to crop from.
 *
 * A plain class, not a data class: two picks of the same photo are still two picks, and
 * comparing megabytes of bytes on every state update would be wasted work.
 */
class PickedImage(val bytes: ByteArray, val width: Int, val height: Int) {
    companion object {
        const val MAX_SIDE = 2048
        const val JPEG_QUALITY = 90
    }
}

sealed interface ImagePickResult {
    data class Picked(val image: PickedImage) : ImagePickResult

    /** Backed out of the camera or the library. A choice, not a failure, so nothing is shown. */
    data object Cancelled : ImagePickResult

    /** Picked, but not something that could be read as a picture. [reason] is for logs only. */
    data class Failed(val reason: String) : ImagePickResult
}

/**
 * Opens the platform's camera or photo library. The answer arrives through the callback given to
 * [rememberImagePicker], not as a return value, because on Android the picker is another activity
 * and its answer comes back through the activity-result machinery.
 */
interface ImagePicker {
    /** False where there is no camera at all, e.g. the iOS simulator, so the action is not offered. */
    val canTakePhoto: Boolean

    fun takePhoto()

    fun choosePhoto()
}

/**
 * Composable because Android registers its result launchers in composition and iOS presents over
 * the window's top view controller.
 */
@Composable
expect fun rememberImagePicker(onResult: (ImagePickResult) -> Unit): ImagePicker
