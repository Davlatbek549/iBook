package com.example.dz.presentation.payment.payment_methods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.PaymentBrand as DomainPaymentBrand
import com.example.dz.domain.model.PaymentMethod
import com.example.dz.domain.usecase.payment.GetPaymentMethodsUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentMethodsViewModel(
    private val getPaymentMethods: GetPaymentMethodsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentMethodsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PaymentMethodsEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: PaymentMethodsEvent) {
        when (event) {
            PaymentMethodsEvent.BackClicked -> emitEffect(PaymentMethodsEffect.NavigateBack)
            PaymentMethodsEvent.AddMethodClicked -> Unit
            is PaymentMethodsEvent.MethodSelected -> selectMethod(event.methodId)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getPaymentMethods()) {
                is AppResult.Success -> _uiState.update { state ->
                    val methods = result.data.map { it.toPaymentMethodItem() }
                    val selectedId = result.data.firstOrNull { it.isSelected }?.id
                        ?.takeIf { id -> methods.any { it.id == id } }
                        ?: methods.firstOrNull()?.id.orEmpty()
                    state.copy(
                        methods = methods,
                        selectedId = selectedId,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun selectMethod(methodId: String) {
        val method = _uiState.value.methods.firstOrNull { it.id == methodId } ?: return
        _uiState.update { it.copy(selectedId = methodId) }
        // A declined method (Apple Pay) demonstrates the failure branch; any other
        // method completes the purchase. (No real gateway in the demo.)
        if (method.brand == PaymentBrand.ApplePay) {
            emitEffect(PaymentMethodsEffect.NavigateToFailure)
        } else {
            emitEffect(PaymentMethodsEffect.NavigateToSuccess)
        }
    }

    private fun emitEffect(effect: PaymentMethodsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}

private fun PaymentMethod.toPaymentMethodItem(): PaymentMethodItem =
    PaymentMethodItem(
        id = id,
        brand = when (brand) {
            DomainPaymentBrand.Paypal -> PaymentBrand.Paypal
            DomainPaymentBrand.Visa,
            DomainPaymentBrand.Mastercard,
            DomainPaymentBrand.Unknown -> PaymentBrand.Visa
            DomainPaymentBrand.ApplePay -> PaymentBrand.ApplePay
        },
        title = title,
        subtitle = subtitle.orEmpty()
    )
