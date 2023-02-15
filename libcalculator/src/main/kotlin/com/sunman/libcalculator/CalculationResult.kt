package com.sunman.libcalculator

import java.io.Serializable


/**
 * Sealed interface of calculation results. Inheritors: [CalculationNothing], [CalculationNumber]
 * and [CalculationError].
 */
sealed interface CalculationResult : Serializable

object CalculationNothing : CalculationResult {
    override fun toString() = ""
}

data class CalculationError(val error: Throwable) : CalculationResult {
    override fun toString() = error.localizedMessage.toString()
}
