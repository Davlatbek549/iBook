package com.example.dz.presentation.book.author_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthorDetailViewModel(
    private val authorId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(sampleAuthorDetail(authorId))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<AuthorDetailEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: AuthorDetailEvent) {
        when (event) {
            AuthorDetailEvent.BackClicked -> emitEffect(AuthorDetailEffect.NavigateBack)
            AuthorDetailEvent.FollowClicked ->
                _uiState.update { it.copy(isFollowing = !it.isFollowing) }
            is AuthorDetailEvent.BookClicked ->
                emitEffect(AuthorDetailEffect.NavigateToBook(event.bookId))
            AuthorDetailEvent.ShareClicked,
            AuthorDetailEvent.MessageClicked,
            AuthorDetailEvent.SeeAllClicked -> Unit
        }
    }

    private fun emitEffect(effect: AuthorDetailEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
