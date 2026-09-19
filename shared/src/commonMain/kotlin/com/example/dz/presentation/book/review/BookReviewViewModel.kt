package com.example.dz.presentation.book.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.localDate
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.BookRatings
import com.example.dz.domain.model.BookReview
import com.example.dz.domain.usecase.book.GetBookDetailsUseCase
import com.example.dz.domain.usecase.book.GetBookRatingsUseCase
import com.example.dz.domain.usecase.review.DeleteMyReviewUseCase
import com.example.dz.domain.usecase.review.GetMyReviewUseCase
import com.example.dz.domain.usecase.review.SaveMyReviewUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookReviewViewModel(
    private val bookId: String,
    private val getBookDetails: GetBookDetailsUseCase,
    private val getBookRatings: GetBookRatingsUseCase,
    private val getMyReview: GetMyReviewUseCase,
    private val saveMyReview: SaveMyReviewUseCase,
    private val deleteMyReview: DeleteMyReviewUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookReviewUiState(bookId = bookId, isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<BookReviewEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: BookReviewEvent) {
        when (event) {
            BookReviewEvent.BackClicked -> emitEffect(BookReviewEffect.NavigateBack)
            BookReviewEvent.RetryClicked -> load()
            BookReviewEvent.WriteReviewClicked -> _uiState.update { state ->
                state.copy(
                    editor = ReviewEditorUiState(
                        stars = state.myReview?.stars ?: 0,
                        note = state.myReview?.note.orEmpty(),
                        isEditing = state.myReview != null,
                    )
                )
            }

            is BookReviewEvent.EditorStarsChanged -> _uiState.update {
                it.copy(editor = it.editor?.copy(stars = event.stars))
            }

            is BookReviewEvent.EditorNoteChanged -> _uiState.update {
                it.copy(editor = it.editor?.copy(note = event.note))
            }

            BookReviewEvent.EditorDismissed -> _uiState.update { it.copy(editor = null) }
            BookReviewEvent.EditorSaved -> save()
            BookReviewEvent.DeleteReviewClicked -> delete()
        }
    }

    /**
     * The book's facts, its scores and the reader's own review, asked for together.
     *
     * The ratings call is allowed to fail on its own: a book whose scores cannot be fetched still
     * has a title, and this screen is also the way to write a review, which does not need them.
     */
    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            coroutineScope {
                val details = async { getBookDetails(bookId) }
                val ratings = async { getBookRatings(bookId) }
                val mine = async { getMyReview(bookId) }

                when (val result = details.await()) {
                    is AppResult.Success -> applyBook(
                        book = result.data,
                        ratings = (ratings.await() as? AppResult.Success)?.data,
                        mine = (mine.await() as? AppResult.Success)?.data,
                    )

                    is AppResult.Error -> _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                    }
                }
            }
        }
    }

    private fun applyBook(book: Book, ratings: BookRatings?, mine: BookReview?) {
        _uiState.update {
            it.copy(
                bookTitle = book.title,
                bookAuthor = book.authors.firstOrNull()?.name.orEmpty(),
                ratings = ratings,
                myReview = mine?.toUi(),
                isLoading = false,
                errorMessage = null,
            )
        }
    }

    private fun save() {
        val editor = _uiState.value.editor ?: return
        if (!editor.canSave) return
        viewModelScope.launch {
            val saved = saveMyReview(bookId, editor.stars, editor.note)
            _uiState.update {
                it.copy(
                    myReview = (saved as? AppResult.Success)?.data?.toUi() ?: it.myReview,
                    editor = null,
                )
            }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            deleteMyReview(bookId)
            _uiState.update { it.copy(myReview = null, editor = null) }
        }
    }

    private fun BookReview.toUi() = MyReviewUi(
        stars = stars,
        note = note,
        writtenOn = formatDate(writtenAt),
    )

    private fun emitEffect(effect: BookReviewEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}

/**
 * A date the way someone would say it: "17 Sep 2026".
 *
 * The month names are English here rather than in `strings.xml`, like everything else the app
 * currently says. They are the one piece of this that a second language would have to move.
 */
internal fun formatDate(epochMillis: Long): String {
    val date = localDate(epochMillis)
    val month = MONTHS.getOrNull(date.month - 1) ?: return "${date.year}"
    return "${date.day} $month ${date.year}"
}

private val MONTHS = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
)
