package com.example.dz.presentation.home

import com.example.dz.domain.model.Book
import com.example.dz.domain.model.LibraryBook

data class HomeUiState(
    val books: List<Book> = emptyList(),
    val continueReading: LibraryBook? = null,
    /** Greeted by name. Null until the profile call lands, and the greeting stands alone. */
    val userName: String? = null,
    /** Null while unknown or when nobody is reading — the card is hidden rather than showing zero. */
    val presence: HomePresence? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Who is in a book right now.
 *
 * The design shows an app-wide figure ("1,284 reading right now"). Nothing serves one yet, so this
 * counts the reader's own friends, which the app can actually see — see the note in [HomeViewModel].
 */
data class HomePresence(
    val readerCount: Int,
    /** One of them, named — the design puts a face on the number. */
    val firstName: String,
)
