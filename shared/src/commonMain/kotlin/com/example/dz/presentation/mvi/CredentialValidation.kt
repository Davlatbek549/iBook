package com.example.dz.presentation.mvi

import com.example.dz.core.common.AppConstants
import com.example.dz.core.error.AppError

/**
 * Catches the credential problems the server would reject anyway, before spending a request on
 * them. The deployed server sleeps between uses, so a rejected sign-up can otherwise cost the user
 * a minute of waiting to be told their password is too short.
 *
 * Results are per field rather than one message per screen: the reader needs to know *which* box
 * to fix, and a single line at the foot of a four-field form does not say.
 *
 * The wording reuses [AppError.AuthReason], so a locally caught problem and the server's own
 * verdict produce the same sentence — it lives in [toPresentationMessage] alone and cannot drift
 * between the two paths.
 */

/** Deliberately loose: shape only. Whether an address exists is the server's business. */
private val EmailPattern = Regex("^[^@\\s]+@[^@\\s.]+(\\.[^@\\s.]+)+$")

internal data class SignInErrors(
    val email: String? = null,
    val password: String? = null,
) {
    val isValid: Boolean get() = email == null && password == null
}

internal data class SignUpErrors(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
) {
    val isValid: Boolean get() = name == null && email == null && password == null
}

internal fun validateEmail(email: String): String? =
    if (EmailPattern.matches(email.trim())) null
    else AppError.Auth(AppError.AuthReason.InvalidEmail).toPresentationMessage()

internal fun validatePassword(password: String): String? =
    if (password.length >= AppConstants.PASSWORD_MIN_LENGTH) null
    else AppError.Auth(AppError.AuthReason.WeakPassword).toPresentationMessage()

/**
 * Sign-in only checks that there is something to send. An account made before any rule we
 * tighten later still has to be able to sign in — refusing to transmit its password would lock
 * its owner out of their own account.
 */
internal fun validateSignIn(email: String, password: String) = SignInErrors(
    email = if (email.isBlank()) "Please enter your email address." else null,
    password = if (password.isBlank()) "Please enter your password." else null,
)

internal fun validateSignUp(name: String, email: String, password: String) = SignUpErrors(
    name = if (name.isBlank()) "Please enter your name." else null,
    email = validateEmail(email),
    password = validatePassword(password),
)

/** The reset screen sets a password rather than presenting one, so both boxes are checked. */
internal fun validateNewPassword(password: String, confirmation: String): String? =
    validatePassword(password) ?: if (password != confirmation) "Both passwords must match." else null
