package com.example.dz.data.repository

import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.repository.ReadingPositionRepository

/**
 * Reading position in the device's key-value store, a pair of keys per book.
 *
 * Two numbers per book is small enough that a table of its own would be more machinery than the
 * data deserves, and it keeps the position readable without a suspend call — which is the whole
 * point, since the reader needs it before it can draw anything.
 */
class LocalReadingPositionRepository(
    private val local: LocalDataSource,
) : ReadingPositionRepository {

    override fun lastOffset(bookId: String): Int =
        local.getSetting(offsetKey(bookId)).toIntOrNull()?.coerceAtLeast(0)
            ?: carriedOverPage(bookId)

    override fun saveLastOffset(bookId: String, offset: Int) {
        local.saveSetting(offsetKey(bookId), offset.toString())
    }

    override fun bookmark(bookId: String): Int? =
        local.getSetting(bookmarkKey(bookId)).toIntOrNull()?.takeIf { it >= 0 }

    override fun saveBookmark(bookId: String, offset: Int?) {
        if (offset == null) {
            local.removeSetting(bookmarkKey(bookId))
        } else {
            local.saveSetting(bookmarkKey(bookId), offset.toString())
        }
    }

    /**
     * A place left behind by the reader that counted pages instead of characters.
     *
     * Those pages were a fixed 1500 characters each, so the offset is exact rather than a guess.
     * It is read once and then written back as an offset, so this only runs for a book somebody
     * had open before the reader learned to measure.
     */
    private fun carriedOverPage(bookId: String): Int {
        val page = local.getSetting("reader_page_$bookId").toIntOrNull() ?: return 0
        return ((page - 1).coerceAtLeast(0)) * CHARS_PER_OLD_PAGE
    }

    private fun offsetKey(bookId: String) = "reader_offset_$bookId"

    private fun bookmarkKey(bookId: String) = "reader_bookmark_$bookId"

    private companion object {
        const val CHARS_PER_OLD_PAGE = 1_500
    }
}
