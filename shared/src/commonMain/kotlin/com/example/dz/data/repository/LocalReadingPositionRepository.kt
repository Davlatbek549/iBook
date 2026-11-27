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
    private val library: com.example.dz.domain.repository.LibraryRepository? = null,
) : ReadingPositionRepository {

    override suspend fun resumeOffset(bookId: String, textLength: Int): Int {
        val offset = lastOffset(bookId).coerceIn(0, textLength)
        val result = library?.getLibraryBooks()
        val progress = (result as? com.example.dz.core.result.AppResult.Success)?.data
            ?.firstOrNull { it.book.id == bookId }?.progressPercent ?: return offset
        if (textLength == 0) return offset
        val localPercent = if (offset == 0) 0 else (offset.toLong() * 100 / textLength).toInt().coerceAtLeast(1)
        if (localPercent == progress) return offset
        return (textLength.toLong() * progress / 100).toInt().coerceIn(0, textLength)
    }

    override fun lastOffset(bookId: String): Int =
        local.getSetting(offsetKey(bookId)).toIntOrNull()?.coerceAtLeast(0)
            ?: legacyOffset(bookId)

    override fun saveLastOffset(bookId: String, offset: Int) {
        local.saveSetting(offsetKey(bookId), offset.toString())
    }

    override fun bookmark(bookId: String): Int? {
        val saved = local.getSetting(bookmarkKey(bookId))
        if (saved.isNotEmpty()) return saved.toIntOrNull()?.takeIf { it >= 0 }
        val user = local.getUserId()
        if (user != null && local.getSetting("reading_sync_legacy_owner") == user) {
            val legacy = local.getSetting("reader_bookmark_$bookId").toIntOrNull()
            saveBookmark(bookId, legacy)
            return legacy?.takeIf { it >= 0 }
        }
        return null
    }

    override fun saveBookmark(bookId: String, offset: Int?) {
        if (offset == null) {
            // A tombstone prevents a cleared pin from being imported from legacy storage again.
            local.saveSetting(bookmarkKey(bookId), "-1")
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

    private fun legacyOffset(bookId: String): Int {
        val user = local.getUserId()
        if (user != null && local.getSetting("reading_sync_legacy_owner") != user) return 0
        return local.getSetting("reader_offset_$bookId").toIntOrNull() ?: carriedOverPage(bookId)
    }

    private fun prefix() = local.getUserId()?.let { "reader_account_${it}_" } ?: "reader_"

    private fun offsetKey(bookId: String) = "${prefix()}offset_$bookId"

    private fun bookmarkKey(bookId: String) = "${prefix()}bookmark_$bookId"

    private companion object {
        const val CHARS_PER_OLD_PAGE = 1_500
    }
}
