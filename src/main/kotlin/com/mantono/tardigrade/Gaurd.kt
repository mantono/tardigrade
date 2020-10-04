package com.mantono.tardigrade

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class BlockingGuard<T>(
    private val maxAttempts: Int = 3,
    private val backOff: (Int) -> Long = ::fibonacci
) {
    operator fun invoke(function: () -> T): Result<T> {
        return attempt(function, maxAttempts)
    }

    private tailrec fun attempt(function: () -> T, attempts: Int): Result<T> {
        val result: Result<T> = tryExecute(function)
        val remainingAttempts: Int = attempts - 1
        return if (result is Result.Success || remainingAttempts <= 0) {
            result
        } else {
            val doneAttempts: Int = maxAttempts - remainingAttempts
            val waitTime: Long = backOff(doneAttempts)
            Thread.sleep(waitTime)
            attempt(function, remainingAttempts)
        }
    }

    private inline fun tryExecute(function: () -> T): Result<T> {
        return try {
            Result.Success(function())
        } catch(e: Throwable) {
            Result.Error(e)
        }
    }
}

class AsyncGuard<T>(
    private val maxAttempts: Int = 3,
    private val scope: CoroutineScope = CoroutineScope(Job()),
    private val backOff: (Int) -> Long = ::fibonacci
): CoroutineScope by scope {
    operator fun invoke(function: suspend () -> T): Deferred<Result<T>> {
        return async(scope.coroutineContext) { attempt(function, maxAttempts) }
    }

    suspend fun await(function: suspend () -> T): Result<T> {
        return attempt(function, maxAttempts)
    }

    private tailrec suspend fun attempt(function: suspend () -> T, attempts: Int): Result<T> {
        val result: Result<T> = tryExecute(function)
        val remainingAttempts: Int = attempts - 1
        return if (result is Result.Success || remainingAttempts <= 0) {
            result
        } else {
            val doneAttempts: Int = maxAttempts - remainingAttempts
            val waitTime: Long = backOff(doneAttempts)
            delay(waitTime)
            attempt(function, remainingAttempts)
        }
    }

    private suspend inline fun tryExecute(crossinline function: suspend () -> T): Result<T> {
        return try {
            Result.Success(function())
        } catch(e: Throwable) {
            Result.Error(e)
        }
    }
}
