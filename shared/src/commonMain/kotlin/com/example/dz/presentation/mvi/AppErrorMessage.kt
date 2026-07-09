package com.example.dz.presentation.mvi

import com.example.dz.core.error.AppError

internal fun AppError.toPresentationMessage(): String =
    when (this) {
        AppError.Network -> "Could not load data. Check your connection and try again."
        AppError.NotFound -> "We could not find that item."
        AppError.Unauthorized -> "Please sign in to continue."
        is AppError.Unknown -> message ?: "Something went wrong."
    }
