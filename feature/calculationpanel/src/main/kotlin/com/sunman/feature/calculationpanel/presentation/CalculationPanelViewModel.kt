package com.sunman.feature.calculationpanel.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunman.libcalculator.Calculator
import com.sunman.libcalculator.Number

class CalculationPanelViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val bracketCount = state.getLiveData(BRACKET_COUNT, 0L)

    private val _calculationString = state.getLiveData(CALCULATION_STRING, "")
    val calculationString: LiveData<String> get() = Transformations.map(_calculationString) { it.toDisplayableRepresentation() }

    private val _isFunctionSubpanelShown = state.getLiveData(IS_FUNCTION_SUBPANEL_SHOWN, false)
    internal val isFunctionSubpanelShown: LiveData<Boolean> get() = _isFunctionSubpanelShown

    private val _isUsingInverseFunctions = state.getLiveData(IS_USING_INVERSE_FUNCTIONS, false)
    internal val isUseInverseFunctions: LiveData<Boolean> get() = _isUsingInverseFunctions

    private val calculator = Calculator()


    internal fun addToInputString(input: CharSequence) {
        if (input.endsWith('(')) {
            bracketCount.value = bracketCount.value!!.inc()
        }

        state[CALCULATION_STRING] = _calculationString.value!! + input
    }

    internal fun addBracket() {
        state[CALCULATION_STRING] = _calculationString.value!!.run {
            val lastChar = lastOrNull()

            if (bracketCount.value == 0L || lastChar == '(') {
                bracketCount.value = bracketCount.value!!.inc()
                "$this("
            } else {
                bracketCount.value = bracketCount.value!!.dec()
                "$this)"
            }
        }
    }

    internal fun removeOneCharFromInputString() {
        state[CALCULATION_STRING] = _calculationString.value!!.dropLast(1)
    }

    internal fun clearInputString() {
        state[CALCULATION_STRING] = ""
    }

    internal fun changeFunctionSubpanel() {
        state[IS_FUNCTION_SUBPANEL_SHOWN] = !(_isFunctionSubpanelShown.value!!)
    }

    internal fun changeUseInverseFunctions() {
        state[IS_USING_INVERSE_FUNCTIONS] = !(_isUsingInverseFunctions.value!!)
    }

    internal fun executeCalculation(context: Context) {
        state[CALCULATION_STRING] = try {
            val calculationResult = calculator.executeStatement(_calculationString.value)

            if (calculationResult is Number) {
                calculationResult.toString().toDisplayableRepresentation()
            } else {
                ""
            }
        } catch (e: Exception) {
            e.toDisplayableError(context)
        }
    }


    companion object {
        const val BRACKET_COUNT = "bracketCount"
        const val CALCULATION_STRING = "calculationString"
        const val IS_FUNCTION_SUBPANEL_SHOWN = "isFunctionSubpanelCollapsed"
        const val IS_USING_INVERSE_FUNCTIONS = "isUsingInverseFunctions"
    }
}
