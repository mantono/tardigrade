package com.mantono.tardigrade

import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

internal fun fibonacci(n: Int): Long {
    val multiplier: Double = 1 / sqrt(5.0)
    val left: Double = ((1 + sqrt(5.0)) / 2.0).pow(n.toDouble())
    val right: Double = ((1 - sqrt(5.0)) / 2.0).pow(n.toDouble())
    val sum: Double = multiplier * (left - right)
    return sum.roundToLong() * 1_000L
}