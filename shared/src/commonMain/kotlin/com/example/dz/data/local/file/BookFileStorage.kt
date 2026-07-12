package com.example.dz.data.local.file

/**
 * Platform implementation of [FileStorage].
 *
 * Android stores files under the app's private `filesDir`; iOS under the app's Documents directory.
 * Construct the platform actual in platform DI (Android needs a `Context`) and inject the
 * [FileStorage] interface into repositories.
 */
expect class BookFileStorage : FileStorage {
    override fun save(bookId: String, text: String): String
    override fun read(path: String): String?
    override fun delete(path: String): Boolean
    override fun exists(path: String): Boolean
}
