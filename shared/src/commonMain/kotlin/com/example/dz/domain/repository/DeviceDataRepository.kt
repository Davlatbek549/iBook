package com.example.dz.domain.repository

/**
 * What this device keeps for an account besides the session: the library, collections, downloaded
 * books and per-account settings. None of it is tied to an account on the device, so it has to be
 * erased when the account goes — otherwise the next person to sign in here would find it.
 */
interface DeviceDataRepository {
    suspend fun eraseAccountData()
}
