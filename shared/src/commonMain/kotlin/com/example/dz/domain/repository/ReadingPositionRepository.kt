package com.example.dz.domain.repository

/**
 * Where the reader is in a book, and where they pinned it.
 *
 * Both are character offsets into the book's text, not page numbers. A page is however much fits on
 * the screen at the size the reader chose, so a page number stops meaning the same thing the moment
 * they change the type — an offset is the word they stopped on, and survives.
 *
 * Kept apart from reading *progress* in the library, which is a percentage for a progress bar on a
 * shelf. Reads are synchronous because the reader asks for them while laying out its first page.
 */
interface ReadingPositionRepository {
    /** The offset this book was last left at; 0 when it has never been opened. */
    fun lastOffset(bookId: String): Int

    fun saveLastOffset(bookId: String, offset: Int)

    /** The offset pinned in this book, or `null` when nothing is pinned. */
    fun bookmark(bookId: String): Int?

    /** Pins [offset], or clears the pin when it is `null`. */
    fun saveBookmark(bookId: String, offset: Int?)
}
