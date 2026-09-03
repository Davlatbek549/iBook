package com.example.dz.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.data.local.LocalDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Owns only the *terminal* onboarding actions (Skip / Start). Which page is
 * currently showing is intentionally NOT duplicated here — the pager's own
 * `currentPage` (see [OnboardingScreen]) is the single source of truth for
 * that, per the design spec.
 */
class OnboardingViewModel(
    private val localDataSource: LocalDataSource,
) : ViewModel() {

    private val _effects = MutableSharedFlow<OnboardingEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            OnboardingEvent.SkipClicked,
            OnboardingEvent.StartClicked -> {
                localDataSource.setOnboardingCompleted(true)
                emitEffect(OnboardingEffect.NavigateToSignUp)
            }
        }
    }

    private fun emitEffect(effect: OnboardingEffect) {
        viewModelScope.launch { _effects.emit(effect) }
    }
}
