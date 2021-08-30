package com.mantono.tardigrade

import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * Closed-form function for the Fibonacci sequence numbers, where `n` is the number of attempts
 * done, and the return value is the n:th Fibonacci number * 1000 (wait time in milliseconds).
 *
 * So this yields;
 * 1 -> 1 000
 * 2 -> 1 000
 * 3 -> 2 000
 * 4 -> 3 000
 * 5 -> 5 000
 * 6 -> 8 000
 * 7 -> 13 000
 * 8 -> 21 000
 * 9 -> 34 000
 * 10 -> 55 000
 *
 * and so on.
 */
internal fun fibonacci(n: Int): Long {
    val multiplier: Double = 1 / sqrt(5.0)
    val left: Double = ((1 + sqrt(5.0)) / 2.0).pow(n.toDouble())
    val right: Double = ((1 - sqrt(5.0)) / 2.0).pow(n.toDouble())
    val sum: Double = multiplier * (left - right)
    return sum.roundToLong() * 1_000L
}