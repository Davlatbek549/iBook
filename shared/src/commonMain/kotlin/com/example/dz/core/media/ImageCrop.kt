package com.example.dz.core.media

import kotlin.math.min
import kotlin.math.roundToInt

/**
 * The two shapes the server stores, at exactly the size it stores them (dz-server's
 * `ProfileImageKind`). The server refuses a picture more than 2% off its shape, so the app
 * sends the finished size rather than leaving the trim to it.
 */
enum class CropShape(
    val outputWidth: Int,
    val outputHeight: Int,
    /** The narrowest region the server accepts as a source. Zooming in stops before this. */
    val minSourceWidth: Int,
) {
    Circle(outputWidth = 512, outputHeight = 512, minSourceWidth = 256),
    Banner(outputWidth = 1500, outputHeight = 500, minSourceWidth = 750);

    val aspect: Float get() = outputWidth.toFloat() / outputHeight
}

/** A rectangle of the picture, in the picture's own pixels. */
data class CropRegion(val left: Int, val top: Int, val width: Int, val height: Int)

/**
 * Where the picture sits under the frame.
 *
 * [zoom] 1 is the picture as small as it may go: just covering the frame. [centerX] and
 * [centerY] are the point of the picture under the frame's centre, as fractions of its width
 * and height.
 *
 * Held in picture terms rather than screen pixels, so it means the same at any frame size.
 */
data class CropPosition(val zoom: Float = 1f, val centerX: Float = 0.5f, val centerY: Float = 0.5f)

/**
 * The rules for moving a picture under a frame of [shape]: it always covers the frame, it zooms
 * between [1, maxZoom], and the region under the frame is what [region] cuts out.
 */
class CropGeometry(val imageWidth: Int, val imageHeight: Int, val shape: CropShape) {

    init {
        require(imageWidth > 0 && imageHeight > 0) { "Empty picture" }
    }

    /** The widest region of the frame's shape that fits in the picture, which is what zoom 1 shows. */
    private val fullWidth: Float = min(imageWidth.toFloat(), imageHeight * shape.aspect)

    /**
     * Zooming stops while the region is still as wide as the server's minimum, or at 4×, whichever
     * comes first. A picture already narrower than the minimum does not zoom at all.
     */
    val maxZoom: Float = (fullWidth / shape.minSourceWidth).coerceIn(1f, MAX_ZOOM)

    fun regionWidth(zoom: Float): Float = fullWidth / zoom

    fun regionHeight(zoom: Float): Float = regionWidth(zoom) / shape.aspect

    /** Screen pixels per picture pixel, for a frame [frameWidth] pixels wide. */
    fun scaleOnScreen(zoom: Float, frameWidth: Float): Float = frameWidth / regionWidth(zoom)

    /** The nearest position that keeps the zoom in range and the frame covered. */
    fun clamp(position: CropPosition): CropPosition {
        val zoom = position.zoom.coerceIn(1f, maxZoom)
        val halfWidth = regionWidth(zoom) / 2 / imageWidth
        val halfHeight = regionHeight(zoom) / 2 / imageHeight
        return CropPosition(
            zoom = zoom,
            centerX = position.centerX.coerceIn(halfWidth, 1 - halfWidth),
            centerY = position.centerY.coerceIn(halfHeight, 1 - halfHeight),
        )
    }

    /**
     * One step of a drag and pinch, in screen pixels. [centroidX] and [centroidY] are where the
     * fingers are, measured from the frame's centre; the picture point under them stays under
     * them as the zoom changes, and the drag then moves the picture with the fingers.
     */
    fun transform(
        position: CropPosition,
        frameWidth: Float,
        panX: Float,
        panY: Float,
        zoomBy: Float,
        centroidX: Float,
        centroidY: Float,
    ): CropPosition {
        val before = scaleOnScreen(position.zoom, frameWidth)
        val zoom = (position.zoom * zoomBy).coerceIn(1f, maxZoom)
        val after = scaleOnScreen(zoom, frameWidth)
        val x = position.centerX * imageWidth + centroidX * (1 / before - 1 / after) - panX / after
        val y = position.centerY * imageHeight + centroidY * (1 / before - 1 / after) - panY / after
        return clamp(CropPosition(zoom, x / imageWidth, y / imageHeight))
    }

    /** The picture's pixels under the frame, always inside the picture. */
    fun region(position: CropPosition): CropRegion {
        val clamped = clamp(position)
        val width = regionWidth(clamped.zoom).roundToInt().coerceIn(1, imageWidth)
        val height = (width / shape.aspect).roundToInt().coerceIn(1, imageHeight)
        val left = (clamped.centerX * imageWidth - width / 2f).roundToInt().coerceIn(0, imageWidth - width)
        val top = (clamped.centerY * imageHeight - height / 2f).roundToInt().coerceIn(0, imageHeight - height)
        return CropRegion(left, top, width, height)
    }

    private companion object {
        const val MAX_ZOOM = 4f
    }
}

/**
 * Cuts [region] out of [image] and scales it to [shape]'s exact size, as a fresh JPEG.
 * Runs off the main thread. Throws when the picture cannot be decoded.
 */
expect suspend fun cropToJpeg(image: PickedImage, region: CropRegion, shape: CropShape): PickedImage
