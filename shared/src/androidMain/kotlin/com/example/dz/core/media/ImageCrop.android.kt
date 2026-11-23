package com.example.dz.core.media

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Decodes only the region, at the smallest power-of-two step still at least the output's width,
 * then scales the rest of the way. Going 2048 → 512 in a single filtered draw would alias; most of
 * the reduction happens in the decoder instead, which averages properly.
 */
actual suspend fun cropToJpeg(image: PickedImage, region: CropRegion, shape: CropShape): PickedImage =
    withContext(Dispatchers.Default) {
        var sample = 1
        while (region.width / (sample * 2) >= shape.outputWidth) sample *= 2

        val decoder = newRegionDecoder(image.bytes)
        val part = try {
            decoder.decodeRegion(
                Rect(region.left, region.top, region.left + region.width, region.top + region.height),
                BitmapFactory.Options().apply { inSampleSize = sample },
            )
        } finally {
            decoder.recycle()
        } ?: error("Could not decode the picture")

        val out = Bitmap.createBitmap(shape.outputWidth, shape.outputHeight, Bitmap.Config.ARGB_8888)
        Canvas(out).apply {
            drawColor(Color.WHITE)
            drawBitmap(
                part,
                null,
                Rect(0, 0, shape.outputWidth, shape.outputHeight),
                Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG),
            )
        }
        part.recycle()

        val bytes = ByteArrayOutputStream().use { stream ->
            out.compress(Bitmap.CompressFormat.JPEG, PickedImage.JPEG_QUALITY, stream)
            stream.toByteArray()
        }
        out.recycle()
        PickedImage(bytes, shape.outputWidth, shape.outputHeight)
    }

private fun newRegionDecoder(bytes: ByteArray): BitmapRegionDecoder =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        BitmapRegionDecoder.newInstance(bytes, 0, bytes.size)
    } else {
        @Suppress("DEPRECATION")
        BitmapRegionDecoder.newInstance(bytes, 0, bytes.size, false)
    } ?: error("Could not read the picture")
