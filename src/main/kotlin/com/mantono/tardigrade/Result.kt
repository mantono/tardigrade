package com.mantono.tardigrade

sealed class Result<T> {
    class Success<T>(val value: T): Result<T>()
    class Error<T>(val cause: Throwable): Result<T>()
}

sealed class Outcome {
    object Success: Outcome()
    class Failure(val cause: Throwable): Outcome()
}