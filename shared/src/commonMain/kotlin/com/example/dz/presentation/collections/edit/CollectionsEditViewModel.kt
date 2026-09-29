package com.example.dz.presentation.collections.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.usecase.collection.CreateCollectionUseCase
import com.example.dz.domain.usecase.collection.DeleteCollectionUseCase
import com.example.dz.domain.usecase.collection.GetCollectionDetailsUseCase
import com.example.dz.domain.usecase.collection.UpdateCollectionUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionsEditViewModel(
    private val collectionId: String,
    private val getCollectionDetails: GetCollectionDetailsUseCase,
    private val createCollection: CreateCollectionUseCase,
    private val updateCollection: UpdateCollectionUseCase,
    private val deleteCollection: DeleteCollectionUseCase,
    private val getLibraryBooks: GetLibraryBooksUseCase
) : ViewModel() {

    // The domain collection backing the current edit, retained so save can
    // preserve book content that isn't represented in the UI model.
    private var loadedCollection: Collection? = null

    /**
     * Every book this screen has seen, by id — the shelf's own, plus whatever the picker loaded.
     *
     * The UI model is a title and a cover; a collection stores whole [Book]s. Save has to turn the
     * ids the reader ticked back into books, and a book picked from the library is not in the
     * collection yet, so the collection alone cannot answer that.
     */
    private val knownBooks = mutableMapOf<String, Book>()

    private val isNewCollection = collectionId == "new" || collectionId == "all"

    private val _uiState = MutableStateFlow(
        CollectionsEditUiState(
            collectionId = collectionId,
            isNewCollection = isNewCollection,
            isLoading = !isNewCollection
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CollectionsEditEffect>()
    val effects = _effects.asSharedFlow()

    init {
        if (!isNewCollection) load()
    }

    fun onEvent(event: CollectionsEditEvent) {
        when (event) {
            CollectionsEditEvent.BackClicked -> emitEffect(CollectionsEditEffect.NavigateBack)
            CollectionsEditEvent.SaveClicked -> save()
            CollectionsEditEvent.DeleteClicked -> delete()
            is CollectionsEditEvent.NameChanged -> _uiState.update { it.copy(name = event.value) }
            is CollectionsEditEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.value) }
            is CollectionsEditEvent.VisibilityChanged -> _uiState.update { it.copy(visibleToFriends = event.value) }
            is CollectionsEditEvent.ColorSelected -> _uiState.update { it.copy(colorIndex = event.index) }
            is CollectionsEditEvent.BookRemoved -> _uiState.update {
                it.copy(books = it.books.filterNot { book -> book.id == event.bookId })
            }
            CollectionsEditEvent.AddBooksClicked -> openPicker()
            CollectionsEditEvent.PickerDismissed ->
                _uiState.update { it.copy(isPickerOpen = false) }
            is CollectionsEditEvent.PickerBookToggled -> togglePicked(event.bookId)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getCollectionDetails(collectionId)) {
                is AppResult.Success -> {
                    loadedCollection = result.data
                    result.data.books.forEach { knownBooks[it.id] = it }
                    val details = result.data.toCollectionsEditUiState()
                    _uiState.update {
                        details.copy(
                            isNewCollection = false,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                is AppResult.Error -> {
                    // Not found → treat as a fresh, blank collection rather than an error.
                    _uiState.update {
                        it.copy(isNewCollection = true, isLoading = false, errorMessage = null)
                    }
                }
            }
        }
    }

    /**
     * Opens the picker, fetching the shelf the first time it is asked for.
     *
     * The fetch is not done on load: most visits here rename a shelf or recolour it and never open
     * the picker at all, and a screen that can be reached from a tile tap should not pay for a
     * library it may not show.
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
                    it.copy(isLibraryLoading = false, errorMessage = result.error.toString())
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
                state.copy(books = state.books + book.toCollectionsEditBookUi())
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            // Resolved from everything this screen has seen rather than filtered out of the
            // collection: filtering can only ever drop a book it already had, so a book picked
            // from the library was discarded on the way to the server and the shelf never grew.
            val chosenBooks = state.books.mapNotNull { knownBooks[it.id] }
            // A collection loaded for editing is updated in place. Otherwise this is a brand-new
            // collection: it must be created first — update on a nonexistent id is a silent no-op.
            val base = loadedCollection
                ?: state.name.takeIf { it.isNotBlank() }?.let { name ->
                    (createCollection(name) as? AppResult.Success)?.data
                }
            if (base != null) {
                updateCollection(
                    base.copy(
                        title = state.name,
                        description = state.description.ifBlank { null },
                        colorIndex = state.colorIndex,
                        isShared = state.visibleToFriends,
                        books = chosenBooks
                    )
                )
            }
            emitEffect(CollectionsEditEffect.NavigateBack)
        }
    }

    private fun delete() {
        viewModelScope.launch {
            deleteCollection(collectionId)
            emitEffect(CollectionsEditEffect.NavigateBack)
        }
    }

    private fun emitEffect(effect: CollectionsEditEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
