package com.sunman.feature.main.presentation

import androidx.lifecycle.*
import com.sunman.libcalculator.AngleUnit
import com.sunman.libcalculator.CalculationResult
import com.sunman.libcalculator.Calculator
import kotlinx.coroutines.*

class MainViewModel(
    private val savedState: SavedStateHandle,
    // TODO: replace default argument to injected value
    // private val dispatcher: CoroutineContext = Dispatchers.Default
) : ViewModel() {

    val angleUnit: LiveData<AngleUnit> = savedState
        .getLiveData(key = ANGLE_UNIT_KEY, initialValue = DEFAULT_ANGLE_UNIT)
    private val angleUnitValue get() = angleUnit.value!!

    private val calculator = Calculator(CALCULATOR_MATH_CONTEXT).apply { angleUnit = angleUnitValue }

    val calculationField: LiveData<CalculationFieldState> = savedState
        .getLiveData(key = CALCULATION_FIELD_KEY, initialValue = EMPTY_EXPRESSION)
    private val calculationFieldValue get() = calculationField.value!!
    private var calculationFieldJob: Job? = null

    val resultField: LiveData<CalculationFieldState> = savedState
        .getLiveData(key = RESULT_FIELD_KEY, initialValue = EMPTY_EXPRESSION)
    private val resultFieldValue get() = resultField.value!!
    private var resultFieldCalculationJob: Job? = null

    private val calculationFieldObserver = Observer<CalculationFieldState> { calculationField ->
        updateResultField(calculationField)
    }


    init {
        calculationField.observeForever(calculationFieldObserver)
    }


    override fun onCleared() {
        super.onCleared()
        calculationField.removeObserver(calculationFieldObserver)
    }

    fun addItemToCalculationField(item: CharSequence) {
        when (val fieldState = calculationFieldValue) {
            is CalculationFieldState.InProgress -> {
                calculationFieldJob?.cancel()
                savedState[CALCULATION_FIELD_KEY] = newExpression(item)
            }

            is CalculationFieldState.Expression -> {
                savedState[CALCULATION_FIELD_KEY] = fieldState.addItem(item)
            }

            is CalculationFieldState.Error -> {
                savedState[CALCULATION_FIELD_KEY] = newExpression(item)
            }
        }
    }

    fun addParenthesisToCalculationField() {
        when (val fieldState = calculationFieldValue) {
            is CalculationFieldState.InProgress -> {
                calculationFieldJob?.cancel()
                savedState[CALCULATION_FIELD_KEY] = newExpression(item = "(")
            }

            is CalculationFieldState.Expression -> {
                savedState[CALCULATION_FIELD_KEY] = fieldState.addParenthesis()
            }

            is CalculationFieldState.Error -> {
                savedState[CALCULATION_FIELD_KEY] = newExpression(item = "(")
            }
        }
    }

    fun removeOneItemFromCalculationString() {
        when (val fieldState = calculationFieldValue) {
            is CalculationFieldState.InProgress -> {
                calculationFieldJob?.cancel()
                savedState[CALCULATION_FIELD_KEY] = EMPTY_EXPRESSION
            }

            is CalculationFieldState.Expression -> {
                savedState[CALCULATION_FIELD_KEY] = fieldState.removeLastItem()
            }

            is CalculationFieldState.Error -> {
                savedState[CALCULATION_FIELD_KEY] = EMPTY_EXPRESSION
            }
        }
    }

    fun clearInputString() {
        when (calculationFieldValue) {
            is CalculationFieldState.InProgress -> {
                calculationFieldJob?.cancel()
                savedState[CALCULATION_FIELD_KEY] = EMPTY_EXPRESSION
            }

            else -> savedState[CALCULATION_FIELD_KEY] = EMPTY_EXPRESSION
        }
    }

    private fun updateResultField(calculationField: CalculationFieldState) {
        if (calculationField !is CalculationFieldState.Expression) {
            return
        }

        resultFieldCalculationJob?.cancel()

        if (calculationField == resultFieldValue) {
            savedState[RESULT_FIELD_KEY] = EMPTY_EXPRESSION
            return
        }

        if (calculationField == EMPTY_EXPRESSION || calculationField.isJustNumber) {
            savedState[RESULT_FIELD_KEY] = EMPTY_EXPRESSION
            return
        }

        resultFieldCalculationJob = viewModelScope.launch {
            val messageAppearJob = launch {
                delay(MESSAGE_APPEAR_TIME_MS)
                savedState[RESULT_FIELD_KEY] = CalculationFieldState.InProgress(
                    expression = calculationField
                )
            }

            savedState[RESULT_FIELD_KEY] = calculateResult(expression = calculationField)
                .toCalculationFieldState()
                .also { messageAppearJob.cancel() }
        }
    }

    fun executeCalculation() {
        val fieldState = calculationFieldValue

        if (fieldState !is CalculationFieldState.Expression) {
            return
        }

        calculationFieldJob?.cancel()

        if (fieldState == EMPTY_EXPRESSION) {
            savedState[CALCULATION_FIELD_KEY] = EMPTY_EXPRESSION
            return
        } else if (fieldState.isJustNumber) {
            savedState[CALCULATION_FIELD_KEY] = fieldState
            return
        }

        calculationFieldJob = viewModelScope.launch {
            val messageAppearJob = launch {
                delay(MESSAGE_APPEAR_TIME_MS)
                savedState[CALCULATION_FIELD_KEY] = CalculationFieldState.InProgress(
                    expression = fieldState
                )
            }

            savedState[CALCULATION_FIELD_KEY] = calculateResult(expression = fieldState)
                .toCalculationFieldState()
                .also { messageAppearJob.cancel() }
        }
    }

    fun setAngleUnit(angleUnit: AngleUnit) {
        if (calculationFieldValue !is CalculationFieldState.InProgress) {
            calculator.angleUnit = angleUnit
            savedState[ANGLE_UNIT_KEY] = angleUnit
        }
    }

    // TODO: replace Dispatchers.Default to dispatcher
    private suspend fun calculateResult(
        expression: CalculationFieldState.Expression
    ): CalculationResult = withContext(Dispatchers.Default) {
        calculator.calculateExpression(expression.completedExpression.toString())
    }


    private companion object {
        const val CALCULATION_FIELD_KEY = "CALCULATION_FIELD_KEY"
        const val RESULT_FIELD_KEY = "RESULT_FIELD_KEY"
        const val ANGLE_UNIT_KEY = "ANGLE_UNIT_KEY"
    }
}
