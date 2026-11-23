package com.example.dz.core.media

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.max
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The library is the system photo picker, which needs no storage permission; below Android 11
 * without it, the contract falls back to the document picker by itself.
 *
 * The camera is whatever camera app the phone has, writing into a file this app shares with it
 * through a FileProvider. No CAMERA permission is declared, and that is on purpose: declaring it
 * would oblige the app to ask for it before even this hand-off is allowed.
 */
@Composable
actual fun rememberImagePicker(onResult: (ImagePickResult) -> Unit): ImagePicker {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val deliver by rememberUpdatedState(onResult)
    // Saveable because the camera app can outlive this activity's configuration.
    var capturePath by rememberSaveable { mutableStateOf<String?>(null) }

    fun prepareAndDeliver(uri: Uri, afterwards: () -> Unit = {}) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    ImagePickResult.Picked(prepare(context.contentResolver, uri))
                } catch (error: Exception) {
                    Log.w(TAG, "Could not read the picked picture", error)
                    ImagePickResult.Failed(error.message ?: error::class.simpleName.orEmpty())
                } finally {
                    afterwards()
                }
            }
            deliver(result)
        }
    }

    val library = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) deliver(ImagePickResult.Cancelled) else prepareAndDeliver(uri)
    }

    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        val file = capturePath?.let(::File)
        capturePath = null
        when {
            file == null -> deliver(ImagePickResult.Cancelled)
            // A camera app can report success and still leave the file empty.
            saved && file.length() > 0 -> prepareAndDeliver(Uri.fromFile(file)) { file.delete() }
            else -> {
                file.delete()
                deliver(ImagePickResult.Cancelled)
            }
        }
    }

    return remember(context) {
        object : ImagePicker {
            override val canTakePhoto: Boolean =
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

            override fun takePhoto() {
                val file = newCaptureFile(context)
                capturePath = file.path
                try {
                    camera.launch(FileProvider.getUriForFile(context, "${context.packageName}.photos", file))
                } catch (_: ActivityNotFoundException) {
                    capturePath = null
                    deliver(ImagePickResult.Failed("No camera app"))
                }
            }

            override fun choosePhoto() {
                library.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }
}

/** Matches `res/xml/photo_paths.xml` in androidApp, which is all the FileProvider will share. */
private fun newCaptureFile(context: Context): File {
    val folder = File(context.cacheDir, "photos").apply { mkdirs() }
    return File(folder, "capture-${System.currentTimeMillis()}.jpg")
}

/**
 * Decoded at the smallest power-of-two step that is still at least [PickedImage.MAX_SIDE], so a
 * 50-megapixel photo never sits in memory at full size, then scaled the rest of the way and
 * turned upright in one pass.
 */
private fun prepare(resolver: ContentResolver, uri: Uri): PickedImage {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    resolver.open(uri).use { BitmapFactory.decodeStream(it, null, bounds) }
    val sourceWidth = bounds.outWidth
    val sourceHeight = bounds.outHeight
    require(sourceWidth > 0 && sourceHeight > 0) { "Not a picture" }

    var sample = 1
    while (max(sourceWidth, sourceHeight) / (sample * 2) >= PickedImage.MAX_SIDE) sample *= 2
    val decoded = resolver.open(uri).use {
        BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = sample })
    } ?: error("Not a picture")

    val scale = minOf(1f, PickedImage.MAX_SIDE.toFloat() / max(decoded.width, decoded.height))
    val matrix = Matrix().apply {
        postScale(scale, scale)
        applyOrientation(readOrientation(resolver, uri))
    }
    val upright = Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
    if (upright !== decoded) decoded.recycle()

    // JPEG has no transparency; without a background, a transparent PNG turns black.
    val opaque = if (upright.hasAlpha()) {
        Bitmap.createBitmap(upright.width, upright.height, Bitmap.Config.ARGB_8888).also { flat ->
            Canvas(flat).apply {
                drawColor(Color.WHITE)
                drawBitmap(upright, 0f, 0f, null)
            }
            upright.recycle()
        }
    } else {
        upright
    }

    val bytes = ByteArrayOutputStream().use { out ->
        opaque.compress(Bitmap.CompressFormat.JPEG, PickedImage.JPEG_QUALITY, out)
        out.toByteArray()
    }
    return PickedImage(bytes, opaque.width, opaque.height).also { opaque.recycle() }
}

private fun ContentResolver.open(uri: Uri) = openInputStream(uri) ?: error("Could not open $uri")

/** PNGs and some library files carry no orientation, and some throw on asking; both mean upright. */
private fun readOrientation(resolver: ContentResolver, uri: Uri): Int = try {
    resolver.open(uri).use {
        ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    }
} catch (_: Exception) {
    ExifInterface.ORIENTATION_NORMAL
}

private fun Matrix.applyOrientation(orientation: Int) {
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> postRotate(270f)
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> postScale(-1f, 1f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> postScale(1f, -1f)
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            postRotate(90f)
            postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            postRotate(270f)
            postScale(-1f, 1f)
        }
    }
}

private const val TAG = "ImagePicker"
