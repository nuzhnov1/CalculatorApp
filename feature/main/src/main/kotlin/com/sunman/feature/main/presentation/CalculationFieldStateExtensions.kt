package com.sunman.feature.main.presentation

import com.sunman.libcalculator.CalculationError
import com.sunman.libcalculator.CalculationNothing
import com.sunman.libcalculator.CalculationNumber
import com.sunman.libcalculator.CalculationResult


internal val CalculationFieldState.Expression.completedExpression get() =
    representation.completeExpression()

internal val CalculationFieldState.Expression.isJustNumber get() =
    representation.removeSeparators().isJustNumber


internal fun newExpression(item: CharSequence) =
    CalculationFieldState.Expression(representation = item + CHAR_SEPARATOR)

internal fun CalculationFieldState.Expression.addItem(item: CharSequence) =
    CalculationFieldState.Expression(representation = representation + item + CHAR_SEPARATOR)

internal fun CalculationFieldState.Expression.addParenthesis(): CalculationFieldState.Expression {
    val lastItem = representation.lastItemOrNull()

    return if (representation.openParenthesisCount == 0 || lastItem == '(') {
        this.addItem(item = "(")
    } else {
        this.addItem(item = ")")
    }
}

internal fun CalculationFieldState.Expression.removeLastItem() =
    CalculationFieldState.Expression(representation = representation.dropLastItem())

internal fun CalculationResult.toCalculationFieldState() = when (this) {
    is CalculationNothing -> EMPTY_EXPRESSION

    is CalculationNumber -> CalculationFieldState.Expression(
        representation = this.toString(
            precision = NUMBER_PRECISION,
            maxTrailingZeros = MAX_TRAILING_ZEROS
        ).addSeparators(separator = CHAR_SEPARATOR)
    )

    is CalculationError -> CalculationFieldState.Error(value = this)
}
