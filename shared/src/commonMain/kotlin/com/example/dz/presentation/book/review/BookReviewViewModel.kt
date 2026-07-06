package com.example.dz.presentation.book.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.usecase.book.GetBookDetailsUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.img_neil_alvin
import dz.shared.generated.resources.profile_2
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookReviewViewModel(
    private val bookId: String,
    private val getBookDetails: GetBookDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        BookReviewUiState(bookId = bookId, reviews = sampleReviews(), isLoading = true)
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<BookReviewEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: BookReviewEvent) {
        when (event) {
            BookReviewEvent.BackClicked -> emitEffect(BookReviewEffect.NavigateBack)
            BookReviewEvent.StartReadingClicked -> emitEffect(BookReviewEffect.NavigateToReading(bookId))
            is BookReviewEvent.HelpfulToggled -> toggleHelpful(event.reviewId)
            BookReviewEvent.ShareClicked,
            BookReviewEvent.WriteReviewClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getBookDetails(bookId)) {
                is AppResult.Success -> applyBook(result.data)
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun applyBook(book: Book) {
        _uiState.update {
            it.copy(
                bookTitle = book.title,
                bookAuthor = book.authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
                averageRating = book.rating?.let { rating -> ((rating * 10).toInt() / 10.0).toString() } ?: it.averageRating,
                ratingsCount = book.reviewCount?.let { count -> "$count ratings" } ?: it.ratingsCount,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    private fun toggleHelpful(reviewId: String) {
        _uiState.update { state ->
            state.copy(
                reviews = state.reviews.map { review ->
                    if (review.id == reviewId) {
                        review.copy(
                            markedHelpful = !review.markedHelpful,
                            helpfulCount = review.helpfulCount + if (review.markedHelpful) -1 else 1
                        )
                    } else {
                        review
                    }
                }
            )
        }
    }

    private fun emitEffect(effect: BookReviewEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}

private fun sampleReviews(): List<BookReviewItemUi> = listOf(
    BookReviewItemUi(
        id = "patricia-lane",
        avatarRes = Res.drawable.profile_2,
        name = "Patricia Lane",
        date = "2 days ago",
        stars = 5,
        text = "Atmospheric and slow in the best way. I read the last hundred pages in one sitting with the lights low.",
        helpfulCount = 32,
        markedHelpful = true
    ),
    BookReviewItemUi(
        id = "neil-alvin",
        avatarRes = Res.drawable.img_neil_alvin,
        name = "Neil Alvin",
        date = "1 week ago",
        stars = 4,
        text = "Gorgeous prose. The middle drags slightly, but the house itself is the best character I’ve met all year.",
        helpfulCount = 11,
        markedHelpful = false
    )
)
