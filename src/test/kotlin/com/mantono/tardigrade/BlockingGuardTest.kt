package com.mantono.tardigrade

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

        assertTrue(result is Result.Success)
        result as Result.Success<Int>
        assertEquals(3, result.value)
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

        assertTrue(result is Result.Error)
        result as Result.Error<Int>
        assertTrue(result.cause is IllegalStateException)
        assertEquals("3", result.cause.message)
    }
}