package com.example.dz.presentation.home

import com.example.dz.core.time.TimeOfDay
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import com.example.dz.domain.model.Friend
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingGoal

data class HomeUiState(
    val books: List<Book> = emptyList(),
    val continueReading: LibraryBook? = null,
    /** The rest of what is part-read, so Home shows more than the one book on the Keep going card. */
    val shelf: List<LibraryBook> = emptyList(),
    /** The week's hero title. */
    val editorsPick: Book? = null,
    /** Friends with a book open, for the social strip under the presence card. */
    val friendsReading: List<Friend> = emptyList(),
    val categories: List<Category> = emptyList(),
    /** A carousel per genre, the same shape as Picked for you. Genres with nothing in them are dropped. */
    val categoryShelves: List<CategoryShelf> = emptyList(),
    /** Today's reading against the target. Null only until the first load finishes. */
    val goal: ReadingGoal? = null,
    /** Greeted by name. Null until the profile call lands, and the greeting stands alone. */
    val userName: String? = null,
    /** Which greeting to use. Re-read when Home resumes, so it does not sit on the wrong one. */
    val greeting: TimeOfDay = TimeOfDay.MORNING,
    /** Null while unknown or when nobody is reading — the card is hidden rather than showing zero. */
    val presence: HomePresence? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/** One genre's carousel: the genre, and the books to show under its name. */
data class CategoryShelf(
    val category: Category,
    val books: List<Book>,
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
