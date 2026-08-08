package com.example.dz.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.usecase.auth.GetCurrentUserUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/** How long the emblem stays up, so a restored session does not flash past the user. */
private const val SPLASH_MINIMUM_MILLIS = 1500L

/**
 * Decides where the app opens. The session survives in local storage, so someone who signed in
 * yesterday lands on Home instead of being asked for their password again.
 */
class SplashViewModel(
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    /**
     * Replayed because this is decided in [init], which can finish before the navigation graph
     * subscribes — a dropped effect would leave the splash on screen forever.
     */
    private val _effects = MutableSharedFlow<SplashEffect>(replay = 1)
    val effects = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            val session = async { getCurrentUser() }
            delay(SPLASH_MINIMUM_MILLIS)
            _effects.emit(session.await().toDestination())
        }
    }

    private fun AppResult<User?>.toDestination(): SplashEffect = when (this) {
        is AppResult.Success ->
            if (data != null) SplashEffect.NavigateToHome else SplashEffect.NavigateToOnboarding
        // A half-written session is not one worth trusting; start from the top rather than
        // opening a signed-in shell whose every request would be rejected.
        is AppResult.Error -> SplashEffect.NavigateToOnboarding
    }
}
