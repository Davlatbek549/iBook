package com.example.dz.presentation.collections.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Collection
import com.example.dz.domain.usecase.collection.DeleteCollectionUseCase
import com.example.dz.domain.usecase.collection.GetCollectionDetailsUseCase
import com.example.dz.domain.usecase.collection.UpdateCollectionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionsEditViewModel(
    private val collectionId: String,
    private val getCollectionDetails: GetCollectionDetailsUseCase,
    private val updateCollection: UpdateCollectionUseCase,
    private val deleteCollection: DeleteCollectionUseCase
) : ViewModel() {

    // The domain collection backing the current edit, retained so save can
    // preserve book content that isn't represented in the UI model.
    private var loadedCollection: Collection? = null

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
            is CollectionsEditEvent.BookRemoved -> _uiState.update {
                it.copy(books = it.books.filterNot { book -> book.id == event.bookId })
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getCollectionDetails(collectionId)) {
                is AppResult.Success -> {
                    loadedCollection = result.data
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

    private fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            val keptBookIds = state.books.map { it.id }.toSet()
            val collection = (loadedCollection ?: Collection(id = collectionId, title = state.name)).copy(
                title = state.name,
                description = state.description.ifBlank { null },
                books = loadedCollection?.books.orEmpty().filter { it.id in keptBookIds }
            )
            updateCollection(collection)
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
