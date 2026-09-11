package com.example.dz.domain.usecase.account

import com.example.dz.core.result.AppResult
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.repository.DeviceDataRepository

/**
 * Deletes the signed-in account, then everything this device kept for it.
 *
 * The device is only cleared once the server has confirmed. If the request fails, the account
 * still exists, and its library should still be here when the reader tries again.
 */
class DeleteAccountUseCase(
    private val auth: AuthRepository,
    private val deviceData: DeviceDataRepository,
) {
    suspend operator fun invoke(): AppResult<Unit> {
        val result = auth.deleteAccount()
        if (result is AppResult.Success) deviceData.eraseAccountData()
        return result
    }
}
