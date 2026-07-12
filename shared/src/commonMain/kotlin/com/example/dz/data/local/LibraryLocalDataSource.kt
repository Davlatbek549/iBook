package com.example.dz.data.local

import com.example.dz.database.DzDatabase
import com.example.dz.database.Library_book
import com.example.dz.domain.model.Author
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.LibraryBook

/**
 * Local persistence for the user's library, backed by SQLDelight. Repositories call this instead of
 * touching SQL directly; it maps rows to/from [LibraryBook] domain models.
 */
class LibraryLocalDataSource(database: DzDatabase) {
    private val queries = database.libraryBookQueries

    fun getLibraryBooks(): List<LibraryBook> =
        queries.selectAll().executeAsList().map { it.toLibraryBook() }

    fun getLibraryBook(bookId: String): LibraryBook? =
        queries.selectById(bookId).executeAsOneOrNull()?.toLibraryBook()

    fun getContinueReading(): LibraryBook? =
        queries.selectContinueReading().executeAsOneOrNull()?.toLibraryBook()

    /** Inserts or replaces a library entry. [addedAt] is an epoch-millis sort key. */
    fun upsert(libraryBook: LibraryBook, addedAt: Long) {
        val book = libraryBook.book
        queries.upsert(
            id = book.id,
            title = book.title,
            author = book.authors.firstOrNull()?.name,
            cover_url = book.coverUrl,
            description = book.description,
            text_url = book.textUrl,
            is_free = book.isFree.toLong(),
            progress_percent = libraryBook.progressPercent.toLong(),
            is_downloaded = libraryBook.isDownloaded.toLong(),
            is_favorite = libraryBook.isFavorite.toLong(),
            added_at = addedAt
        )
    }

    fun updateProgress(bookId: String, progressPercent: Int) {
        queries.updateProgress(progressPercent.toLong(), bookId)
    }

    fun setDownloaded(bookId: String, downloaded: Boolean) {
        queries.setDownloaded(downloaded.toLong(), bookId)
    }

    /** Marks a book downloaded and records the on-device file [path]. */
    fun setDownload(bookId: String, downloaded: Boolean, path: String?) {
        queries.setDownload(downloaded.toLong(), path, bookId)
    }

    /** Clears the downloaded flag and file path (used after deleting the local file). */
    fun clearDownload(bookId: String) {
        queries.clearDownload(bookId)
    }

    /** The stored local file path for a downloaded book, or null if not downloaded. */
    fun getDownloadPath(bookId: String): String? =
        queries.selectById(bookId).executeAsOneOrNull()?.download_path

    fun setFavorite(bookId: String, favorite: Boolean) {
        queries.setFavorite(favorite.toLong(), bookId)
    }

    fun remove(bookId: String) {
        queries.deleteById(bookId)
    }
}

private fun Library_book.toLibraryBook(): LibraryBook =
    LibraryBook(
        book = Book(
            id = id,
            title = title,
            authors = author?.let { listOf(Author(id = it, name = it)) }.orEmpty(),
            coverUrl = cover_url,
            description = description,
            language = null,
            isFree = is_free.toBoolean(),
            textUrl = text_url
        ),
        progressPercent = progress_percent.toInt(),
        isDownloaded = is_downloaded.toBoolean(),
        isFavorite = is_favorite.toBoolean()
    )

private fun Boolean.toLong(): Long = if (this) 1L else 0L
private fun Long.toBoolean(): Boolean = this != 0L
