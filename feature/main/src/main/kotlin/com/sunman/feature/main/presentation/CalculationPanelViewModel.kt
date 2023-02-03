package com.sunman.feature.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunman.libcalculator.*
import com.sunman.libcalculator.Nothing
import com.sunman.libcalculator.Number

class CalculationPanelViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val _calculationString = state.getLiveData(CALCULATION_STRING, "")
    val calculationString = Transformations.map(_calculationString) {
        it.replace(" ", "")  // Remove all whitespaces
    }

    val calculationResult = Transformations.map(calculationString) {
        val calculationString = (it + ")".repeat(it.openBracketsCount))
        val result = calculator.calculateExpression(calculationString)

        if (result is Number && result.toString() == it) {
            Nothing
        } else {
            result
        }
    }

    private val _angleUnit = state.getLiveData(ANGLE_UNIT, DEFAULT_ANGLE_UNIT)
    val angleUnit: LiveData<AngleUnit> = _angleUnit

    private val calculator = Calculator(CALCULATOR_MATH_CONTEXT).apply {
        angleUnit = _angleUnit.value!!
    }

    private val String.openBracketsCount: Int get() {
        val totalOpenBracket = count { chr -> chr == '(' }
        val totalClosedBracket = count { chr -> chr == ')' }

        return totalOpenBracket - totalClosedBracket
    }


    fun addItemToCalculationString(item: CharSequence) {
        // If the item is an opening or closing parenthesis:
        state[CALCULATION_STRING] = if (item == "()") {
            val calculationString = _calculationString.value!!
            // Dropping a trailing whitespace and trying to get a last char:
            val lastChar = calculationString.dropLast(1).lastOrNull()

            // Adding a trailing whitespace to separate input items
            if (calculationString.openBracketsCount == 0 || lastChar == '(') {
                "$calculationString( "
            } else {
                "$calculationString) "
            }
        } else {
            // Adding a trailing whitespace to separate input items
            _calculationString.value!! + "$item "
        }
    }

    fun removeOneItemFromCalculationString() {
        state[CALCULATION_STRING] = _calculationString.value!!
            .dropLast(1) // Drop one whitespace
            .dropLastWhile { it != ' ' } // Drop the item until whitespace
    }

    fun clearInputString() {
        state[CALCULATION_STRING] = ""
    }

    fun executeCalculation() = when (val result = calculationResult.value) {
        is Number -> {
            state[CALCULATION_STRING] = result.toString()
                .map { it }
                .joinToString(separator = " ", postfix = " ")
        }

        is Error -> {
            state[CALCULATION_STRING] = result.toString()
        }

        null, is Nothing -> Unit
    }


    fun setAngleUnit(angleUnit: AngleUnit) {
        calculator.angleUnit = angleUnit
        state[ANGLE_UNIT] = angleUnit.ordinal
    }


    internal companion object {
        const val CALCULATION_STRING = "CALCULATION_STRING"
        const val ANGLE_UNIT = "ANGLE_UNIT"
    }
}
