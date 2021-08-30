package com.mantono.tardigrade

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

private const val DEFAULT_MAX_ATTEMPTS: Int = 3

fun <T> attempt(
    maxAttempts: Int = DEFAULT_MAX_ATTEMPTS,
    function: () -> T,
): Result<T> = BlockingGuard<T>(maxAttempts).invoke(function)

class BlockingGuard<T>(
    private val maxAttempts: Int = DEFAULT_MAX_ATTEMPTS,
    private val backOff: (Int) -> Long = ::fibonacci
) {
    operator fun invoke(function: () -> T): Result<T> {
        return attempt(function, maxAttempts)
    }

    private tailrec fun attempt(function: () -> T, attempts: Int): Result<T> {
        val result: Result<T> = runCatching(function)
        val remainingAttempts: Int = attempts - 1
        return if (result.isSuccess || remainingAttempts <= 0) {
            result
        } else {
            val doneAttempts: Int = maxAttempts - remainingAttempts
            val waitTime: Long = backOff(doneAttempts)
            Thread.sleep(waitTime)
            attempt(function, remainingAttempts)
        }
    }
}

fun <T> attemptAsync(
    maxAttempts: Int = DEFAULT_MAX_ATTEMPTS,
    function: suspend () -> T,
): Deferred<Result<T>> = AsyncGuard<T>(maxAttempts).invoke(function)

class AsyncGuard<T>(
    private val maxAttempts: Int = DEFAULT_MAX_ATTEMPTS,
    private val scope: CoroutineScope = CoroutineScope(Job()),
    private val backOff: (Int) -> Long = ::fibonacci
): CoroutineScope by scope {
    operator fun invoke(function: suspend () -> T): Deferred<Result<T>> {
        return async(scope.coroutineContext) { attempt(function, maxAttempts) }
    }

    private tailrec suspend fun attempt(function: suspend () -> T, attempts: Int): Result<T> {
        val result: Result<T> = runCatching { function() }
        val remainingAttempts: Int = attempts - 1
        return if (result.isSuccess || remainingAttempts <= 0) {
            result
        } else {
            val doneAttempts: Int = maxAttempts - remainingAttempts
            val waitTime: Long = backOff(doneAttempts)
            delay(waitTime)
            attempt(function, remainingAttempts)
        }
    }
}
