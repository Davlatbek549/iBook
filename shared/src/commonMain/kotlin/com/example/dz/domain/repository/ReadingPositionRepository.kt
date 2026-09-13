package com.example.dz.domain.repository

/**
 * Where the reader is in a book, and which page they pinned.
 *
 * Kept apart from reading *progress* in the library, which is a percentage for a progress bar on a
 * shelf. This is the page number itself: rounding a page out of a percentage puts a reader several
 * pages from where they stopped, which on a six-hundred-page book is a chapter away.
 *
 * Reads are synchronous because the reader asks for them while it is laying out the first page.
 */
interface ReadingPositionRepository {
    /** The page this book was last left on; 1 when it has never been opened. */
    fun lastPage(bookId: String): Int

    fun saveLastPage(bookId: String, page: Int)

    /** The page pinned in this book, or `null` when nothing is pinned. */
    fun bookmark(bookId: String): Int?

    /** Pins [page], or clears the pin when it is `null`. */
    fun saveBookmark(bookId: String, page: Int?)
}
