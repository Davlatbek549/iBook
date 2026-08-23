package com.example.dz.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.data.local.LocalDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * The Splash screen is stateless visually (brand moment + two entry points),
 * so the only job here is deciding where "Get started" leads: onboarding has
 * not been shown before, or it has already been completed/skipped in a prior
 * session (see [LocalDataSource.isOnboardingCompleted]) and should not be
 * forced on the user again.
 */
class SplashViewModel(
    private val localDataSource: LocalDataSource,
) : ViewModel() {

    private val _effects = MutableSharedFlow<SplashEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.GetStartedClicked -> emitEffect(
                if (localDataSource.isOnboardingCompleted()) {
                    SplashEffect.NavigateToSignUp
                } else {
                    SplashEffect.NavigateToOnboarding
                }
            )

            SplashEvent.SignInClicked -> emitEffect(SplashEffect.NavigateToSignIn)
        }
    }

    private fun emitEffect(effect: SplashEffect) {
        viewModelScope.launch { _effects.emit(effect) }
    }
}
