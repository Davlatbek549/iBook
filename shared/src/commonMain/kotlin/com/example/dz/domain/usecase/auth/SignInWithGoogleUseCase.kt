package com.example.dz.domain.usecase.auth

import com.example.dz.domain.repository.AuthRepository

class SignInWithGoogleUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = repository.signInWithGoogle(idToken)
}
