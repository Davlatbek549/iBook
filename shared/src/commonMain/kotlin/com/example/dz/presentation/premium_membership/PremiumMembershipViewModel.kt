package com.example.dz.presentation.premium_membership

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.membership.GetCurrentMembershipUseCase
import com.example.dz.domain.usecase.user.GetProfileUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PremiumMembershipViewModel(
    private val getProfile: GetProfileUseCase,
    private val getCurrentMembership: GetCurrentMembershipUseCase
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
            PremiumMembershipEvent.BillingDateClicked,
            PremiumMembershipEvent.BillingHistoryClicked,
            PremiumMembershipEvent.CancelClicked -> emitEffect(PremiumMembershipEffect.NavigateToSettings)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val memberName = when (val result = getProfile()) {
                is AppResult.Success -> result.data.user.name.ifBlank { null }
                is AppResult.Error -> null
            }

            when (val result = getCurrentMembership()) {
                is AppResult.Success -> _uiState.update { state ->
                    val plan = result.data
                    state.copy(
                        memberName = memberName ?: state.memberName,
                        hasActiveMembership = plan != null,
                        renewalPrice = plan?.price ?: state.renewalPrice,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(
                        memberName = memberName ?: it.memberName,
                        isLoading = false,
                        errorMessage = result.error.toPresentationMessage()
                    )
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
