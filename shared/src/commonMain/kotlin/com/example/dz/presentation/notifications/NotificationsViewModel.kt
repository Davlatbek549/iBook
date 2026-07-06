package com.example.dz.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.notification.GetNotificationsUseCase
import com.example.dz.domain.usecase.notification.MarkNotificationAsReadUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotifications: GetNotificationsUseCase,
    private val markAsRead: MarkNotificationAsReadUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<NotificationsEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            NotificationsEvent.BackClicked -> emitEffect(NotificationsEffect.NavigateBack)
            NotificationsEvent.MarkAllReadClicked -> markAllRead()
            is NotificationsEvent.FilterSelected -> _uiState.update { it.copy(filter = event.filter) }
            is NotificationsEvent.NotificationClicked -> onNotificationClicked(event.notificationId)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            // The curated rows above carry the visuals the domain model can't yet express;
            // the use case still drives the loading/error state so this stays backend-ready.
            when (val result = getNotifications()) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun markAllRead() {
        val unreadIds = _uiState.value.items.filter { it.unread }.map { it.id }
        _uiState.update { state -> state.copy(items = state.items.map { it.copy(unread = false) }) }
        viewModelScope.launch {
            unreadIds.forEach { markAsRead(it) }
        }
    }

    private fun onNotificationClicked(notificationId: String) {
        _uiState.update { state ->
            state.copy(items = state.items.map { if (it.id == notificationId) it.copy(unread = false) else it })
        }
        viewModelScope.launch { markAsRead(notificationId) }
        emitEffect(NotificationsEffect.NavigateToChat(notificationId))
    }

    private fun emitEffect(effect: NotificationsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
