package com.example.dz.presentation.membership

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.MembershipPlan
import com.example.dz.domain.usecase.membership.GetMembershipPlansUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MembershipViewModel(
    private val getMembershipPlans: GetMembershipPlansUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MembershipUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<MembershipEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: MembershipEvent) {
        when (event) {
            MembershipEvent.BackClicked -> emitEffect(MembershipEffect.NavigateBack)
            MembershipEvent.StartTrialClicked -> emitEffect(MembershipEffect.NavigateToPremium)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getMembershipPlans()) {
                is AppResult.Success -> _uiState.update { state ->
                    // Override the displayed prices from any monthly/yearly plans the
                    // backend provides; otherwise keep the design defaults.
                    val monthly = result.data.firstOrNull { it.isMonthly() }
                    val yearly = result.data.firstOrNull { it.isYearly() }
                    state.copy(
                        monthlyPrice = monthly?.price ?: state.monthlyPrice,
                        yearlyPrice = yearly?.price ?: state.yearlyPrice,
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

    private fun emitEffect(effect: MembershipEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}

private fun MembershipPlan.isMonthly(): Boolean =
    id.contains("month", ignoreCase = true) || title.contains("month", ignoreCase = true)

private fun MembershipPlan.isYearly(): Boolean =
    id.contains("year", ignoreCase = true) || title.contains("year", ignoreCase = true) ||
        id.contains("annual", ignoreCase = true) || title.contains("annual", ignoreCase = true)
