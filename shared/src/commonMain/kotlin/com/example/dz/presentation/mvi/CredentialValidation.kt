package com.example.dz.presentation.mvi

import com.example.dz.core.common.AppConstants
import com.example.dz.core.error.AppError

/**
 * Catches the credential problems the server would reject anyway, before spending a request on
 * them. The deployed server sleeps between uses, so a rejected sign-up can otherwise cost the user
 * a minute of waiting to be told their password is too short.
 *
 * These reuse [AppError.AuthReason], so a locally caught problem and the server's own verdict
 * produce the same sentence — the wording lives in [toPresentationMessage] alone and cannot drift
 * between the two paths.
 */

/** Deliberately loose: shape only. Whether an address exists is the server's business. */
private val EmailPattern = Regex("^[^@\\s]+@[^@\\s.]+(\\.[^@\\s.]+)+$")

internal fun validateEmail(email: String): String? =
    if (EmailPattern.matches(email.trim())) null
    else AppError.Auth(AppError.AuthReason.InvalidEmail).toPresentationMessage()

internal fun validatePassword(password: String): String? =
    if (password.length >= AppConstants.PASSWORD_MIN_LENGTH) null
    else AppError.Auth(AppError.AuthReason.WeakPassword).toPresentationMessage()

/** Sign-in only checks for something to send: an old account may predate any current rule. */
internal fun validateSignInCredentials(email: String, password: String): String? = when {
    email.isBlank() -> "Please enter your email address."
    password.isBlank() -> "Please enter your password."
    else -> null
}

internal fun validateSignUpCredentials(name: String, email: String, password: String): String? =
    when {
        name.isBlank() -> "Please enter your name."
        else -> validateEmail(email) ?: validatePassword(password)
    }
