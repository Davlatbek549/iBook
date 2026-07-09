package com.example.dz.presentation.payment.purchase_receipt

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

class PurchaseReceiptViewModel(
    private val bookId: String,
    private val getPurchaseDetails: GetPurchaseDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseReceiptUiState(bookId = bookId, isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PurchaseReceiptEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: PurchaseReceiptEvent) {
        when (event) {
            PurchaseReceiptEvent.BackClicked -> emitEffect(PurchaseReceiptEffect.NavigateBack)
            PurchaseReceiptEvent.ReadNowClicked -> emitEffect(PurchaseReceiptEffect.NavigateToReading(bookId))
            PurchaseReceiptEvent.ShareClicked,
            PurchaseReceiptEvent.ReceiptClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getPurchaseDetails(bookId)) {
                is AppResult.Success -> _uiState.update {
                    result.data.toPurchaseReceiptUiState().copy(isLoading = false, errorMessage = null)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: PurchaseReceiptEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
