package com.example.dz.data.remote

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import io.ktor.client.plugins.ResponseException

/**
 * Thrown by auth backends when the server rejects the request for a reason the UI should
 * distinguish (wrong password, duplicate email, ...). [runRemote] maps it to [AppError.Auth].
 */
class AuthBackendException(val reason: AppError.AuthReason) : Exception(reason.name)

/**
 * Runs a remote call and maps transport / HTTP failures onto [AppError] so repositories can
 * return a uniform [AppResult] without leaking Ktor exceptions into the domain layer.
 */
suspend fun <T> runRemote(block: suspend () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (error: AuthBackendException) {
        AppResult.Error(AppError.Auth(error.reason))
    } catch (error: ResponseException) {
        AppResult.Error(
            when (error.response.status.value) {
                401, 403 -> AppError.Unauthorized
                404 -> AppError.NotFound
                else -> AppError.Network
            }
        )
    } catch (error: Throwable) {
        AppResult.Error(AppError.Network)
    }
