package com.example.dz.presentation.payment.purchase_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.payment.GetPurchaseDetailsUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PurchaseDetailsViewModel(
    private val bookId: String,
    private val getPurchaseDetails: GetPurchaseDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseDetailsUiState(bookId = bookId, isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PurchaseDetailsEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: PurchaseDetailsEvent) {
        when (event) {
            PurchaseDetailsEvent.BackClicked -> emitEffect(PurchaseDetailsEffect.NavigateBack)
            PurchaseDetailsEvent.ChangePaymentClicked -> emitEffect(PurchaseDetailsEffect.NavigateToPaymentMethods)
            PurchaseDetailsEvent.PayNowClicked -> emitEffect(PurchaseDetailsEffect.NavigateToConfirmation)
            PurchaseDetailsEvent.ApplyCoinsClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getPurchaseDetails(bookId)) {
                is AppResult.Success -> _uiState.update {
                    result.data.toPurchaseDetailsUiState().copy(isLoading = false, errorMessage = null)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: PurchaseDetailsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
