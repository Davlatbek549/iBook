package com.example.dz.presentation.mvi

import com.example.dz.core.error.AppError

internal fun AppError.toPresentationMessage(): String =
    when (this) {
        AppError.Network -> "Could not load data. Check your connection and try again."
        AppError.NotFound -> "We could not find that item."
        AppError.Unauthorized -> "Please sign in to continue."
        is AppError.Unknown -> message ?: "Something went wrong."
        is AppError.Auth -> when (reason) {
            AppError.AuthReason.InvalidCredentials -> "Email or password is incorrect."
            AppError.AuthReason.EmailAlreadyInUse -> "An account with this email already exists."
            AppError.AuthReason.InvalidEmail -> "Please enter a valid email address."
            AppError.AuthReason.WeakPassword -> "Password is too weak. Use at least 6 characters."
            AppError.AuthReason.UserDisabled -> "This account has been disabled."
            AppError.AuthReason.TooManyAttempts -> "Too many attempts. Please try again later."
            AppError.AuthReason.Unknown -> "Sign-in failed. Please try again."
        }
    }
