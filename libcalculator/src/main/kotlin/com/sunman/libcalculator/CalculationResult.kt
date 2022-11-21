package com.sunman.libcalculator

/**
 * Sealed interface of calculation results. Inheritors: [Nothing], [Number] and [Error].
 */
sealed interface CalculationResult

object Nothing : CalculationResult {
    override fun toString() = ""
}

class Error(val e: Throwable) : CalculationResult {
    override fun toString() = e.localizedMessage.toString()
}
