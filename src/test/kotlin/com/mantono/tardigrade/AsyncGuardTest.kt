package com.mantono.tardigrade

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AsyncGuardTest {
    @Test
    fun `AsyncGuard alternative syntax with success`() {
        val result: Result<String> = runBlocking {
            val deferredResult: Deferred<Result<String>> = attemptAsync(maxAttempts = 10) { "Success" }
            deferredResult.await()
        }

        assertTrue(result.isSuccess)
        assertEquals("Success", result.getOrThrow())
    }

    @Test
    fun `AsyncGuard alternative syntax with failure`() {
        val result: Result<String> = runBlocking {
            val deferredResult: Deferred<Result<String>> = attemptAsync(maxAttempts = 1) {
                throw RuntimeException()
            }
            deferredResult.await()
        }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }
}