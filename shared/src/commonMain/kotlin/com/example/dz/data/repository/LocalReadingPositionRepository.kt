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

    override fun lastPage(bookId: String): Int =
        local.getSetting(pageKey(bookId)).toIntOrNull()?.coerceAtLeast(1) ?: 1

    override fun saveLastPage(bookId: String, page: Int) {
        local.saveSetting(pageKey(bookId), page.toString())
    }

    override fun bookmark(bookId: String): Int? =
        local.getSetting(bookmarkKey(bookId)).toIntOrNull()?.takeIf { it >= 1 }

    override fun saveBookmark(bookId: String, page: Int?) {
        if (page == null) {
            local.removeSetting(bookmarkKey(bookId))
        } else {
            local.saveSetting(bookmarkKey(bookId), page.toString())
        }
    }

    private fun pageKey(bookId: String) = "reader_page_$bookId"

    private fun bookmarkKey(bookId: String) = "reader_bookmark_$bookId"
}
