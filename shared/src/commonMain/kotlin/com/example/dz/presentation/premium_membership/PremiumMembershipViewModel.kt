package com.example.dz.presentation.premium_membership

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.membership.GetCurrentMembershipUseCase
import com.example.dz.domain.usecase.membership.GetMembershipPlansUseCase
import com.example.dz.domain.usecase.payment.GetPaymentMethodsUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PremiumMembershipViewModel(
    private val getCurrentMembership: GetCurrentMembershipUseCase,
    private val getMembershipPlans: GetMembershipPlansUseCase,
    private val getPaymentMethods: GetPaymentMethodsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PremiumMembershipUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PremiumMembershipEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: PremiumMembershipEvent) {
        when (event) {
            PremiumMembershipEvent.BackClicked -> emitEffect(PremiumMembershipEffect.NavigateBack)
            PremiumMembershipEvent.ChangeCardClicked -> emitEffect(PremiumMembershipEffect.NavigateToPaymentMethods)
            PremiumMembershipEvent.SwitchPlanClicked -> emitEffect(PremiumMembershipEffect.NavigateToPaymentConfirmation)
            PremiumMembershipEvent.BillingHistoryClicked -> emitEffect(PremiumMembershipEffect.NavigateToBillingHistory)
            PremiumMembershipEvent.CancelClicked -> emitEffect(PremiumMembershipEffect.NavigateToSettings)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            coroutineScope {
                val membershipAsync = async { getCurrentMembership() }
                val plansAsync = async { getMembershipPlans() }
                val paymentMethodsAsync = async { getPaymentMethods() }

                val membershipResult = membershipAsync.await()
                val plans = (plansAsync.await() as? AppResult.Success)?.data.orEmpty()
                val paymentMethods = (paymentMethodsAsync.await() as? AppResult.Success)?.data.orEmpty()

                when (membershipResult) {
                    is AppResult.Success -> {
                        val plan = membershipResult.data
                        // Whatever else is on file, the one already flagged as selected is the one
                        // the subscription actually bills — not just the first in the list.
                        val card = paymentMethods.firstOrNull { it.isSelected } ?: paymentMethods.firstOrNull()
                        val alternate = plans.firstOrNull { it.id != plan?.id }
                        _uiState.update {
                            it.copy(
                                hasActiveMembership = plan != null,
                                planTitle = plan?.title.orEmpty(),
                                planPrice = plan?.price.orEmpty(),
                                benefits = plan?.benefits.orEmpty(),
                                alternatePlanTitle = alternate?.title,
                                alternatePlanPrice = alternate?.price,
                                paymentMethodLabel = card?.title,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is AppResult.Error -> _uiState.update {
                        it.copy(isLoading = false, errorMessage = membershipResult.error.toPresentationMessage())
                    }
                }
            }
        }
    }

    private fun emitEffect(effect: PremiumMembershipEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
