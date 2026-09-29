package com.example.dz.presentation.collections.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.usecase.collection.GetCollectionDetailsUseCase
import com.example.dz.domain.usecase.collection.UpdateCollectionUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.presentation.collections.edit.toCollectionsEditBookUi
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionDetailsViewModel(
    private val collectionId: String,
    private val getCollectionDetails: GetCollectionDetailsUseCase,
    private val getLibraryBooks: GetLibraryBooksUseCase,
    private val updateCollection: UpdateCollectionUseCase
) : ViewModel() {
    // The domain collection backing this screen, retained so a picker toggle can be turned into a
    // full update: the server takes a whole membership list, not one book at a time.
    private var loadedCollection: Collection? = null

    /** Every book this screen has seen, by id — the shelf's own, plus whatever the picker loaded. */
    private val knownBooks = mutableMapOf<String, Book>()

    private val _uiState = MutableStateFlow(
        CollectionDetailsUiState(collectionId = collectionId, isLoading = true)
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CollectionDetailsEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: CollectionDetailsEvent) {
        when (event) {
            CollectionDetailsEvent.BackClicked -> emitEffect(CollectionDetailsEffect.NavigateBack)
            CollectionDetailsEvent.EditClicked -> emitEffect(CollectionDetailsEffect.NavigateToEdit(collectionId))
            is CollectionDetailsEvent.BookClicked -> emitEffect(CollectionDetailsEffect.NavigateToBook(event.bookId))
            // Opens the same picker the edit screen uses, right here — no detour through Edit
            // just to add a book to a shelf that already exists.
            CollectionDetailsEvent.AddBooksClicked -> openPicker()
            CollectionDetailsEvent.PickerDismissed -> dismissPicker()
            is CollectionDetailsEvent.PickerBookToggled -> togglePicked(event.bookId)
            CollectionDetailsEvent.ShareClicked,
            is CollectionDetailsEvent.BookOptionsClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getCollectionDetails(collectionId)) {
                is AppResult.Success -> {
                    loadedCollection = result.data
                    result.data.books.forEach { knownBooks[it.id] = it }
                    val details = result.data.toCollectionDetailsUiState()
                    _uiState.update {
                        details.copy(isLoading = false, errorMessage = null)
                    }
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    /**
     * Opens the picker, fetching the shelf the first time it is asked for — most visits here just
     * read the shelf and never open the picker at all.
     */
    private fun openPicker() {
        _uiState.update { it.copy(isPickerOpen = true) }
        if (_uiState.value.libraryBooks.isNotEmpty() || _uiState.value.isLibraryLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLibraryLoading = true) }
            when (val result = getLibraryBooks()) {
                is AppResult.Success -> {
                    val books = result.data.map { it.book }
                    books.forEach { knownBooks[it.id] = it }
                    _uiState.update { state ->
                        state.copy(
                            libraryBooks = books.map { it.toCollectionsEditBookUi() },
                            isLibraryLoading = false,
                        )
                    }
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLibraryLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    /** On if it is not on the shelf, off if it is. The picker is the same control either way. */
    private fun togglePicked(bookId: String) {
        _uiState.update { state ->
            if (state.books.any { it.id == bookId }) {
                state.copy(books = state.books.filterNot { it.id == bookId })
            } else {
                val book = knownBooks[bookId] ?: return@update state
                state.copy(books = state.books + book.toCollectionDetailsBookUi())
            }
        }
    }

    /**
     * Closes the picker and, only if it actually changed the shelf, sends the new membership —
     * there is no separate Save on this screen, so the picker's own Done is what commits it.
     */
    private fun dismissPicker() {
        val state = _uiState.value
        _uiState.update { it.copy(isPickerOpen = false) }

        val base = loadedCollection ?: return
        val chosenIds = state.books.map { it.id }.toSet()
        if (chosenIds == base.books.map { it.id }.toSet()) return

        val chosenBooks = state.books.mapNotNull { knownBooks[it.id] }
        viewModelScope.launch {
            when (val result = updateCollection(base.copy(books = chosenBooks))) {
                is AppResult.Success -> loadedCollection = result.data
                is AppResult.Error -> _uiState.update {
                    it.copy(errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: CollectionDetailsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
