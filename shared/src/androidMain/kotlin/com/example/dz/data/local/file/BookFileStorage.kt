package com.example.dz.data.local.file

import android.content.Context
import java.io.File

actual class BookFileStorage(private val context: Context) : FileStorage {

    private fun booksDirectory(): File = File(context.filesDir, BOOKS_DIR).apply { mkdirs() }

    actual override fun save(bookId: String, text: String): String {
        val file = File(booksDirectory(), "${bookId.sanitizedFileName()}.txt")
        file.writeText(text)
        return file.absolutePath
    }

    actual override fun read(path: String): String? =
        File(path).takeIf { it.exists() }?.readText()

    actual override fun delete(path: String): Boolean {
        val file = File(path)
        return !file.exists() || file.delete()
    }

    actual override fun exists(path: String): Boolean = File(path).exists()

    private companion object {
        const val BOOKS_DIR = "books"
    }
}

private fun String.sanitizedFileName(): String =
    map { if (it.isLetterOrDigit()) it else '_' }.joinToString("")
