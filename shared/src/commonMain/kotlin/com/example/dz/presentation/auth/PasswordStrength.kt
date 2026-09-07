package com.example.dz.presentation.auth

import com.example.dz.core.common.AppConstants
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.auth_strength_fair
import dz.shared.generated.resources.auth_strength_good
import dz.shared.generated.resources.auth_strength_strong
import dz.shared.generated.resources.auth_strength_weak
import org.jetbrains.compose.resources.StringResource

/**
 * How the three-bar meter reads a password, shared by the two screens that set one — sign-up and
 * the last step of a reset. Both are choosing a new password against the same rule, so a password
 * the one calls strong cannot be one the other calls fair.
 *
 * Length only. It is what the server actually enforces, and a meter that scolds about symbols it
 * will happily accept teaches readers to distrust it.
 */
internal fun passwordStrength(password: String): Int = when {
    password.isEmpty() -> 0
    // Nothing lit below the minimum: the bar is not a score out of three, it is a promise that
    // what has been typed would be accepted.
    password.length < AppConstants.PASSWORD_MIN_LENGTH -> 0
    password.length < AppConstants.PASSWORD_MIN_LENGTH + 3 -> 1
    password.length < AppConstants.PASSWORD_MIN_LENGTH + 7 -> 2
    else -> 3
}

internal fun passwordStrengthLabel(filled: Int): StringResource = when (filled) {
    0 -> Res.string.auth_strength_weak
    1 -> Res.string.auth_strength_fair
    2 -> Res.string.auth_strength_good
    else -> Res.string.auth_strength_strong
}
