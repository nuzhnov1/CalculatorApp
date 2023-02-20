package com.sunman.feature.main.presentation

import com.sunman.libcalculator.CalculationError
import java.io.Serializable


sealed interface CalculationFieldState : Serializable {
    data class InProgress(val expression: Expression) : CalculationFieldState

    data class Expression(internal val representation: CharSequence) : CalculationFieldState {
        override fun toString() = representation.removeSeparators().toString()
    }

    data class Error(val value: CalculationError) : CalculationFieldState
}
