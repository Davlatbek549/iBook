package com.example.dz

import com.example.dz.core.result.AppResult
import com.example.dz.data.remote.runRemote
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RemoteCancellationTest {

    @Test
    fun `a cancelled call is cancelled, not reported as a network failure`() = runTest {
        var returned: AppResult<Unit>? = null

        val call = launch { returned = runRemote { awaitCancellation() } }
        testScheduler.runCurrent()
        call.cancel()
        call.join()

        // Returning at all means the cancellation was swallowed and turned into an error result,
        // which the caller would then have shown — the flash of "network error" on every
        // replaced search.
        assertNull(returned, "runRemote returned $returned for a cancelled call")
        assertTrue(call.isCancelled)
    }
}
