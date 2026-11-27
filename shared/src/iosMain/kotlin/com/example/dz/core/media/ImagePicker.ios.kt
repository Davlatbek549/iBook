package com.example.dz.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIGraphicsImageRendererFormat
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIRectFill
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue

/**
 * The library is PHPicker, which runs outside the app and needs no photo-library permission.
 * The camera is UIImagePickerController, which needs `NSCameraUsageDescription` in Info.plist.
 */
@Composable
actual fun rememberImagePicker(onResult: (ImagePickResult) -> Unit): ImagePicker {
    val deliver by rememberUpdatedState(onResult)
    return remember { IosImagePicker { deliver(it) } }
}

private class IosImagePicker(private val onResult: (ImagePickResult) -> Unit) : ImagePicker {

    /** UIKit holds picker delegates weakly, so this keeps the current one alive until it answers. */
    private var delegate: NSObject? = null

    override val canTakePhoto: Boolean
        get() = UIImagePickerController.isSourceTypeAvailable(CAMERA)

    override fun takePhoto() {
        val presenter = topViewController() ?: return onResult(ImagePickResult.Failed("No window"))
        val picker = UIImagePickerController().apply { sourceType = CAMERA }
        val camera = CameraDelegate(::finish)
        delegate = camera
        picker.delegate = camera
        presenter.presentViewController(picker, animated = true, completion = null)
    }

    override fun choosePhoto() {
        val presenter = topViewController() ?: return onResult(ImagePickResult.Failed("No window"))
        val configuration = PHPickerConfiguration().apply {
            filter = PHPickerFilter.imagesFilter
            selectionLimit = 1
        }
        val picker = PHPickerViewController(configuration)
        val library = LibraryDelegate(::finish)
        delegate = library
        picker.delegate = library
        presenter.presentViewController(picker, animated = true, completion = null)
    }

    private fun finish(result: ImagePickResult) {
        delegate = null
        onResult(result)
    }
}

private class LibraryDelegate(
    private val done: (ImagePickResult) -> Unit,
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)
        val provider = (didFinishPicking.firstOrNull() as? PHPickerResult)?.itemProvider
            ?: return done(ImagePickResult.Cancelled)

        // "public.image" rather than UIImage, so HEIC, PNG and the rest all come back as data.
        provider.loadDataRepresentationForTypeIdentifier("public.image") { data, error ->
            val result = data?.let { UIImage.imageWithData(it) }?.let(::prepare)
                ?: ImagePickResult.Failed(error?.localizedDescription ?: "Not a picture")
            dispatch_async(dispatch_get_main_queue()) { done(result) }
        }
    }
}

private class CameraDelegate(
    private val done: (ImagePickResult) -> Unit,
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>,
    ) {
        picker.dismissViewControllerAnimated(true, completion = null)
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
            ?: return done(ImagePickResult.Failed("The camera returned no picture"))
        // Redrawing a full-size photo takes long enough to stutter the dismiss animation.
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)) {
            val result = prepare(image)
            dispatch_async(dispatch_get_main_queue()) { done(result) }
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
        done(ImagePickResult.Cancelled)
    }
}

/**
 * Redrawn rather than re-encoded: drawing applies the photo's orientation, so what comes out is
 * upright in its pixels, and the white fill underneath keeps a transparent PNG from going black.
 * `size` is already in the upright orientation, so the target is too.
 */
@OptIn(ExperimentalForeignApi::class)
private fun prepare(image: UIImage): ImagePickResult {
    val (width, height) = image.size.useContents { width * image.scale to height * image.scale }
    if (width <= 0.0 || height <= 0.0) return ImagePickResult.Failed("Empty picture")

    val scale = min(1.0, PickedImage.MAX_SIDE / max(width, height))
    val targetWidth = floor(width * scale).coerceAtLeast(1.0)
    val targetHeight = floor(height * scale).coerceAtLeast(1.0)

    val format = UIGraphicsImageRendererFormat.defaultFormat().apply {
        this.scale = 1.0
        opaque = true
    }
    val renderer = UIGraphicsImageRenderer(size = CGSizeMake(targetWidth, targetHeight), format = format)
    val jpeg = renderer.JPEGDataWithCompressionQuality(PickedImage.JPEG_QUALITY / 100.0) { _ ->
        val bounds = CGRectMake(0.0, 0.0, targetWidth, targetHeight)
        UIColor.whiteColor.setFill()
        UIRectFill(bounds)
        image.drawInRect(bounds)
    }
    return ImagePickResult.Picked(PickedImage(jpeg.toByteArray(), targetWidth.toInt(), targetHeight.toInt()))
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray = bytes?.readBytes(length.toInt()) ?: ByteArray(0)

/** Whatever is on top, so the picker opens over a sheet as well as over the screen. */
private fun topViewController(): UIViewController? {
    val window = UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { scene -> scene.windows.filterIsInstance<UIWindow>() }
        .let { windows -> windows.firstOrNull { it.isKeyWindow() } ?: windows.firstOrNull() }
    var top = window?.rootViewController
    while (top?.presentedViewController != null) top = top.presentedViewController
    return top
}

private val CAMERA = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
