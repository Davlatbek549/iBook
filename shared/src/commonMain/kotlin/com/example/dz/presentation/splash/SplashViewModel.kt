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

/**
 * How long the brand moment stays up. Long enough for the shelf to finish assembling itself (see
 * `SplashScreen`), which is what sets the floor here rather than the session check — that usually
 * answers sooner.
 */
private const val SPLASH_MINIMUM_MILLIS = 1600L

/**
 * Decides where the app opens, while the splash plays. Nothing on that screen is tappable, so this
 * always resolves to exactly one destination:
 *
 * - a restored, verified session → Home;
 * - a restored session whose address was never proved → the code screen;
 * - no session → onboarding, or straight past it to sign-in if it has already been seen
 *   (see [LocalDataSource.isOnboardingCompleted]).
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
            _effects.emit(session.await().toDestination())
        }
    }

    private fun AppResult<User?>.toDestination(): SplashEffect {
        // A half-written session is not one worth trusting; start from the top rather than
        // opening a signed-in shell whose every request would be rejected.
        val user = (this as? AppResult.Success)?.data ?: return firstRunDestination()

        // Signing up issues a session before the code is spent, so a session on its own is not
        // proof of anything. Without this the reader could background the app during verification
        // and come back to a Home whose every request the server refuses.
        return if (user.emailVerified) {
            SplashEffect.NavigateToHome
        } else {
            SplashEffect.NavigateToVerification(user.email.orEmpty())
        }
    }

    private fun firstRunDestination(): SplashEffect =
        if (localDataSource.isOnboardingCompleted()) {
            SplashEffect.NavigateToLogin
        } else {
            SplashEffect.NavigateToOnboarding
        }
}
