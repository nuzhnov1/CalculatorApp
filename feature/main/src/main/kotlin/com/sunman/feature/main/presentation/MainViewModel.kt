package com.sunman.feature.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunman.libcalculator.*
import com.sunman.libcalculator.Nothing
import com.sunman.libcalculator.Number

internal class MainViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val _calculationString = state.getLiveData(CALCULATION_STRING, "")
    val calculationString = Transformations.map(_calculationString) { it.removeSeparators() }

    val calculationResult = Transformations.map(calculationString) {
        val result = calculateResult()

        // If the calculation string is just a number
        if (result is Number && result.toString() == it) {
            Nothing
        } else {
            result
        }
    }

    private val _angleUnit = state.getLiveData(ANGLE_UNIT, DEFAULT_ANGLE_UNIT.ordinal)
    val angleUnit: LiveData<AngleUnit> = Transformations.map(_angleUnit) { it.toAngleUnit() }

    private val calculator = Calculator(CALCULATOR_MATH_CONTEXT).apply {
        angleUnit = _angleUnit.value!!.toAngleUnit()
    }


    fun addItemToCalculationString(item: CharSequence) {
        // If the item is an opening or closing parenthesis:
        val addingItem = if (item == "()") {
            val calculationString = _calculationString.value!!
            val lastChar = calculationString.dropLastSeparator().lastOrNull()

            if (calculationString.openParenthesisCount == 0 || lastChar == '(') {
                "$calculationString("
            } else {
                "$calculationString)"
            }
        } else {
            _calculationString.value!! + item
        }

        state[CALCULATION_STRING] = addingItem + ITEM_SEPARATOR
    }

    fun removeOneItemFromCalculationString() {
        state[CALCULATION_STRING] = _calculationString.value!!
            .dropLastSeparator()
            .dropLastWhile { it != ITEM_SEPARATOR } // Drop the item until separator
    }

    fun clearInputString() {
        state[CALCULATION_STRING] = ""
    }

    fun executeCalculation() {
        val result = calculateResult()

        if (result is Number) {
            state[CALCULATION_STRING] = result.toString()
                .map { it }
                .joinToString(
                    separator = ITEM_SEPARATOR.toString(),
                    postfix = ITEM_SEPARATOR.toString()
                )
        }
    }

    fun setAngleUnit(angleUnit: AngleUnit) {
        calculator.angleUnit = angleUnit
        state[ANGLE_UNIT] = angleUnit.ordinal
    }

    private fun calculateResult(): CalculationResult? {
        val completedCalculationString = calculationString.value
            ?.completeCalculationString()
            ?: return null

        return calculator.calculateExpression(expression = completedCalculationString)
    }


    private companion object {
        const val CALCULATION_STRING = "CALCULATION_STRING"
        const val ANGLE_UNIT = "ANGLE_UNIT"
    }
}
