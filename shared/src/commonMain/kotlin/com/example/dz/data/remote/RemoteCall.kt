package com.example.dz.data.remote

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * Thrown by auth backends when the server rejects the request for a reason the UI should
 * distinguish (wrong password, duplicate email, ...). [runRemote] maps it to [AppError.Auth].
 */
class AuthBackendException(val reason: AppError.AuthReason) : Exception(reason.name)

/**
 * Runs a remote call and maps transport / HTTP failures onto [AppError] so repositories can
 * return a uniform [AppResult] without leaking Ktor exceptions into the domain layer.
 *
 * The call runs on [Dispatchers.Default]. The network wait itself never blocked anything, but what
 * follows it did: decoding the body and deserialising the JSON happen in the coroutine that asked,
 * and callers ask from a view model on the main thread. A page of search results, a book's
 * details, a whole book's text — each was parsed on the thread that draws the screen. This is
 * the one place every response passes through, so it is the one place that has to move.
 */
suspend fun <T> runRemote(block: suspend () -> T): AppResult<T> =
    try {
        AppResult.Success(withContext(Dispatchers.Default) { block() })
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
    } catch (error: HttpRequestTimeoutException) {
        AppResult.Error(AppError.Timeout)
    } catch (error: ConnectTimeoutException) {
        AppResult.Error(AppError.Timeout)
    } catch (error: SocketTimeoutException) {
        AppResult.Error(AppError.Timeout)
    } catch (error: CancellationException) {
        // A cancelled call has not failed, and must not be reported as though it had. Caught by
        // the clause below, a search replaced mid-flight came back as a network error for the
        // screen to show — and the cancelled coroutine carried on as if nothing had stopped it.
        // Ktor's own timeouts are IO exceptions, handled above, so none of them land here.
        throw error
    } catch (error: Throwable) {
        AppResult.Error(AppError.Network)
    }
