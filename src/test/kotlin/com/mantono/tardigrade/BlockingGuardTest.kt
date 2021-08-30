package com.mantono.tardigrade

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BlockingGuardTest {

    @Test
    fun `return successfully on last attempt`() {
        val guard = BlockingGuard<Int>(
            maxAttempts = 3,
            backOff = { it.toLong() }
        )

        var attempts = 0
        val result: Result<Int> = guard {
            if (++attempts == 3) attempts else error("$attempts")
        }

        assertTrue(result.isSuccess)
        assertEquals(3, result.getOrThrow())
    }

    @Test
    fun `fail after max attempts`() {
        val guard = BlockingGuard<Int>(
            maxAttempts = 3,
            backOff = { it.toLong() }
        )

        var attempts = 0
        val result: Result<Int> = guard {
            attempts++
            error("$attempts")
        }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
        assertEquals("3", result.exceptionOrNull()?.message)
    }

    @Test
    fun `BlockingGuard alternative syntax`() {
        val result: Result<String> = attempt(maxAttempts = 10) { "Success" }
        assertTrue(result.isSuccess)
        assertEquals("Success", result.getOrThrow())
    }

    @Test
    fun `AsyncGuard alternative syntax`() {
        val asyncResult: Deferred<Result<String>> = attemptAsync(maxAttempts = 10) { "Success" }
        val result: Result<String> = runBlocking { asyncResult.await() }
        assertTrue(result.isSuccess)
        assertEquals("Success", result.getOrThrow())
    }
}