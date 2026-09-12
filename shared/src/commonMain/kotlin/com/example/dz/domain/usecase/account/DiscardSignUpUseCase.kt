package com.example.dz.domain.usecase.account

import com.example.dz.core.result.AppResult
import com.example.dz.domain.repository.AuthRepository
import kotlinx.coroutines.withTimeoutOrNull

/**
 * How long backing out of a fresh sign-up may wait on the server. It runs on a back press, and a
 * server waking from sleep can take a minute — far longer than anyone should be held on a screen
 * they are trying to leave.
 */
private const val DISCARD_TIMEOUT_MILLIS = 5_000L

/**
 * Throws away an account made moments ago and never verified — the reader has backed out of the
 * code screen, usually to fix a mistyped address. Deleting it means the corrected sign-up makes
 * the only account instead of a second one, and frees the address that was typed.
 *
 * Nothing else on the device is erased: an account a few seconds old owns none of it. Returns
 * whether the server let go, which also ends the session here; when it did not, the session is
 * still on the device for the caller to deal with.
 */
class DiscardSignUpUseCase(private val auth: AuthRepository) {
    suspend operator fun invoke(): Boolean =
        withTimeoutOrNull(DISCARD_TIMEOUT_MILLIS) { auth.deleteAccount() } is AppResult.Success
}
