package com.example.dz.presentation.splash

sealed interface SplashEvent {
    data object GetStartedClicked : SplashEvent
    data object SignInClicked : SplashEvent
}
