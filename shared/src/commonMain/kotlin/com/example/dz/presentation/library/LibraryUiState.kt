package com.example.dz.presentation.library

import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook

/**
 * Which shelf state is showing. The handoff calls this `libraryFilter`, with these three values.
 *
 * Membership is read off reading progress rather than stored: a book with nothing read is waiting,
 * one part-read is open, one at the end is done. Nothing else has to be kept in step.
 */
enum class LibraryFilter { READING, TO_READ, FINISHED }

data class LibraryUiState(
    val books: List<LibraryBook> = emptyList(),
    val continueReading: LibraryBook? = null,
    val filter: LibraryFilter = LibraryFilter.READING,
    val collections: List<Collection> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /** The shelf the chosen filter is showing. */
    val shelf: List<LibraryBook>
        get() = books.filter { it.filter == filter }
}

/** Where a book sits, by how much of it has been read. */
val LibraryBook.filter: LibraryFilter
    get() = when {
        progressPercent >= FINISHED_PERCENT -> LibraryFilter.FINISHED
        progressPercent > 0 -> LibraryFilter.READING
        else -> LibraryFilter.TO_READ
    }

/** Pages still to go, when the book says how many it has. */
val LibraryBook.pagesLeft: Int?
    get() = book.pageCount?.let { total ->
        (total * (FINISHED_PERCENT - progressPercent) / FINISHED_PERCENT).coerceAtLeast(0)
    }

private const val FINISHED_PERCENT = 100
