package com.example.dz.data.local.file

/**
 * Reads and writes downloaded book text to platform local storage. Implemented per platform by
 * [BookFileStorage]; kept as an interface so repositories can depend on it and tests can fake it.
 */
interface FileStorage {
    /** Saves [text] for [bookId] and returns the absolute file path it was written to. */
    fun save(bookId: String, text: String): String

    /** Reads the text at [path], or null if the file is missing/unreadable. */
    fun read(path: String): String?

    /** Deletes the file at [path]. Returns true if the file no longer exists afterward. */
    fun delete(path: String): Boolean

    /** Whether a file currently exists at [path]. */
    fun exists(path: String): Boolean
}
