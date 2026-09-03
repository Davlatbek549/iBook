package com.example.dz.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.User
import com.example.dz.domain.usecase.auth.GetCurrentUserUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/** How long the emblem stays up at minimum, so a restored session does not flash past the user. */
private const val SPLASH_MINIMUM_MILLIS = 1500L

/**
 * The Splash screen shows the brand moment while a stored session is checked in the background
 * (the session survives in local storage, so someone who signed in yesterday lands on Home
 * instead of being asked for their password again). If no session is restored, the reader picks
 * where to go next: "Get started" (→ onboarding, or straight past it if already seen — see
 * [LocalDataSource.isOnboardingCompleted]) or "Sign in" (→ existing sign-in screen).
 */
class SplashViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val localDataSource: LocalDataSource,
) : ViewModel() {

    /**
     * Replayed because the session check can finish before the navigation graph subscribes — a
     * dropped effect would leave the splash on screen forever.
     */
    private val _effects = MutableSharedFlow<SplashEffect>(replay = 1)
    val effects = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            val session = async { getCurrentUser() }
            delay(SPLASH_MINIMUM_MILLIS)
            if (session.await().toUser() != null) {
                _effects.emit(SplashEffect.NavigateToHome)
            }
            // No restored session: stay on the interactive splash and wait for onEvent.
        }
    }

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

    // A half-written session is not one worth trusting; start from the top rather than opening a
    // signed-in shell whose every request would be rejected.
    private fun AppResult<User?>.toUser(): User? = (this as? AppResult.Success)?.data

    private fun emitEffect(effect: SplashEffect) {
        viewModelScope.launch { _effects.emit(effect) }
    }
}
