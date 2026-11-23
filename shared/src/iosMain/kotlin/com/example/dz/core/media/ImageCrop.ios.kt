package com.example.dz.core.media

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGContextSetInterpolationQuality
import platform.CoreGraphics.CGImageCreateWithImageInRect
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.CoreGraphics.kCGInterpolationHigh
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIColor
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIGraphicsImageRendererFormat
import platform.UIKit.UIImage
import platform.UIKit.UIRectFill

/**
 * The picture is already upright (see ImagePicker), so its pixels and the region share one
 * coordinate space and the region can be cut straight out of the CGImage.
 */
@OptIn(ExperimentalForeignApi::class)
actual suspend fun cropToJpeg(image: PickedImage, region: CropRegion, shape: CropShape): PickedImage =
    withContext(Dispatchers.Default) {
        val source = UIImage.imageWithData(image.bytes.toNSData())?.CGImage ?: error("Could not decode the picture")
        val rect = CGRectMake(region.left.toDouble(), region.top.toDouble(), region.width.toDouble(), region.height.toDouble())
        val part = CGImageCreateWithImageInRect(source, rect) ?: error("Region outside the picture")
        val partImage = UIImage.imageWithCGImage(part)

        val width = shape.outputWidth.toDouble()
        val height = shape.outputHeight.toDouble()
        val format = UIGraphicsImageRendererFormat.defaultFormat().apply {
            scale = 1.0
            opaque = true
        }
        val jpeg = UIGraphicsImageRenderer(size = CGSizeMake(width, height), format = format)
            .JPEGDataWithCompressionQuality(PickedImage.JPEG_QUALITY / 100.0) { context ->
                CGContextSetInterpolationQuality(context?.CGContext, kCGInterpolationHigh)
                val bounds = CGRectMake(0.0, 0.0, width, height)
                UIColor.whiteColor.setFill()
                UIRectFill(bounds)
                partImage.drawInRect(bounds)
            }
        PickedImage(jpeg.bytes?.readBytes(jpeg.length.toInt()) ?: ByteArray(0), shape.outputWidth, shape.outputHeight)
    }

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData =
    if (isEmpty()) NSData() else usePinned { NSData.create(bytes = it.addressOf(0), length = size.toULong()) }
