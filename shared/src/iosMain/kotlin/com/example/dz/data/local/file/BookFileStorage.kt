package com.example.dz.data.local.file

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class)
actual class BookFileStorage : FileStorage {

    private val fileManager = NSFileManager.defaultManager

    private fun booksDirectory(): String {
        val documents = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, true
        ).firstOrNull() as? String ?: ""
        val directory = "$documents/$BOOKS_DIR"
        fileManager.createDirectoryAtPath(directory, true, null, null)
        return directory
    }

    actual override fun save(bookId: String, text: String): String {
        val path = "${booksDirectory()}/${bookId.sanitizedFileName()}.txt"
        text.encodeToByteArray().toNSData().writeToFile(path, atomically = true)
        return path
    }

    actual override fun read(path: String): String? {
        val data = NSData.dataWithContentsOfFile(path) ?: return null
        val length = data.length.toInt()
        if (length == 0) return ""
        val bytes = data.bytes ?: return null
        return bytes.readBytes(length).decodeToString()
    }

    actual override fun delete(path: String): Boolean =
        if (!fileManager.fileExistsAtPath(path)) true
        else fileManager.removeItemAtPath(path, null)

    actual override fun exists(path: String): Boolean =
        fileManager.fileExistsAtPath(path)

    private companion object {
        const val BOOKS_DIR = "books"
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData =
    if (isEmpty()) {
        NSData()
    } else {
        usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
        }
    }

private fun String.sanitizedFileName(): String =
    map { if (it.isLetterOrDigit()) it else '_' }.joinToString("")
