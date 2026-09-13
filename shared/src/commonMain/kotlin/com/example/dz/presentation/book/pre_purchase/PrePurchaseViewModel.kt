package com.example.dz.presentation.book.pre_purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.repository.DownloadRepository
import com.example.dz.domain.usecase.book.GetBookDetailsUseCase
import com.example.dz.domain.usecase.book.DownloadBookUseCase
import com.example.dz.domain.usecase.book.GetBooksByCategoryUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrePurchaseViewModel(
    private val bookId: String,
    private val getBookDetails: GetBookDetailsUseCase,
    private val getBooksByCategory: GetBooksByCategoryUseCase,
    private val downloadRepository: DownloadRepository,
    private val getLibraryBooks: GetLibraryBooksUseCase,
    private val downloadBook: DownloadBookUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrePurchaseUiState(bookId = bookId, isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PrePurchaseEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load(bookId)
        refreshDownloadState()
    }

    /**
     * Re-checks what only the device knows: whether the book is stored offline, and where the
     * reader is in it. Both change elsewhere — in the reader — and this screen outlives that trip.
     */
    fun refreshDownloadState() {
        viewModelScope.launch {
            val downloaded = downloadRepository.isDownloaded(bookId)
            val shelved = (getLibraryBooks() as? AppResult.Success)?.data?.firstOrNull { it.book.id == bookId }
            _uiState.update {
                it.copy(
                    isDownloaded = downloaded,
                    progressPercent = shelved?.progressPercent ?: 0,
                    ownership = when {
                        shelved == null -> BookOwnership.NOT_OWNED
                        shelved.progressPercent >= 100 -> BookOwnership.FINISHED
                        shelved.progressPercent > 0 -> BookOwnership.IN_PROGRESS
                        else -> BookOwnership.NOT_STARTED
                    }
                )
            }
        }
    }

    private fun download() {
        if (_uiState.value.isDownloaded || _uiState.value.isDownloading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isDownloading = true) }
            val result = downloadBook(bookId)
            _uiState.update {
                it.copy(
                    isDownloading = false,
                    isDownloaded = result is AppResult.Success,
                    errorMessage = (result as? AppResult.Error)?.error?.toPresentationMessage()
                )
            }
        }
    }

    fun onEvent(event: PrePurchaseEvent) {
        when (event) {
            PrePurchaseEvent.BackClicked -> emitEffect(PrePurchaseEffect.NavigateBack)
            PrePurchaseEvent.PrimaryActionClicked -> {
                val state = _uiState.value
                // A book that is owned or free opens; anything else has to be bought first.
                if (state.ownership == BookOwnership.NOT_OWNED && !state.isFree) {
                    emitEffect(PrePurchaseEffect.NavigateToPurchase(state.bookId))
                } else {
                    emitEffect(PrePurchaseEffect.NavigateToReading(state.bookId))
                }
            }
            PrePurchaseEvent.BookmarkClicked -> emitEffect(PrePurchaseEffect.NavigateToCollections)
            PrePurchaseEvent.DownloadClicked -> download()
            PrePurchaseEvent.AuthorClicked ->
                _uiState.value.authorId?.let { authorId -> emitEffect(PrePurchaseEffect.NavigateToAuthor(authorId)) }
            is PrePurchaseEvent.RelatedBookClicked -> emitEffect(PrePurchaseEffect.NavigateToBook(event.bookId))
        }
    }

    private fun load(bookId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getBookDetails(bookId)) {
                is AppResult.Success -> loadRelated(result.data)
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private suspend fun loadRelated(book: Book) {
        val categoryId = book.categories.firstOrNull()?.id
        val relatedBooks = if (categoryId != null) {
            when (val result = getBooksByCategory(categoryId)) {
                is AppResult.Success -> result.data.filterNot { it.id == book.id }
                is AppResult.Error -> emptyList()
            }
        } else {
            emptyList()
        }

        _uiState.update {
            // The book's own facts are replaced; what the device knows about it is kept, since
            // the offline check and the shelf lookup run beside this rather than inside it.
            book.toPrePurchaseUiState(relatedBooks).copy(
                isDownloaded = it.isDownloaded,
                ownership = it.ownership,
                progressPercent = it.progressPercent
            )
        }
    }

    private fun emitEffect(effect: PrePurchaseEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
