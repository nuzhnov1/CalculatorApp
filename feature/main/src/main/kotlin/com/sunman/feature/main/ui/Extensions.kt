package com.sunman.feature.main.ui

import android.content.Context
import com.sunman.feature.main.presentation.CalculationFieldState
import com.sunman.feature.main.ui.resources.*
import com.sunman.libcalculator.AngleUnit
import com.sunman.libcalculator.E
import com.sunman.libcalculator.PI
import com.sunman.libcalculator.SyntaxException


internal fun String.toDisplayableRepresentation(context: Context) = context.resources.run {
    when (val value = this@toDisplayableRepresentation) {
        in "0".."9", ".", "," -> value
        addition -> "+"
        subtraction -> "-"
        multiplication -> "*"
        division -> "/"
        percent -> "%"
        power -> "^"
        factorial -> "!"
        pi -> PI.toString()
        e -> E.toString()
        powerOf2 -> "2^("
        powerOf10 -> "10^("
        sqr -> "^2"
        cube -> "^3"
        else -> value.lowercase() + "("
    }
}

internal fun CalculationFieldState.toString(context: Context): String = when (this) {
    is CalculationFieldState.InProgress -> context.resources.getInProgressString(
        expression.toString(context)
    )

    is CalculationFieldState.Expression -> toString()

    is CalculationFieldState.Error -> value.error.toString(context)
}

internal fun AngleUnit.toString(context: Context) = context.resources.run {
    when (this@toString) {
        AngleUnit.RADIAN -> rad
        AngleUnit.DEGREE -> deg
        AngleUnit.GRADIAN -> grad
    }
}

private fun Throwable.toString(context: Context) = context.resources.run {
    localizedMessage?.lowercase()?.run {
        when {
            this@toString is SyntaxException -> invalidExpression
            contains(other = "division by zero", ignoreCase = true) -> divisionByZero
            contains(other = "illegal", ignoreCase = true) -> invalidArgument

            Regex(pattern = "the function '\\w+' expects \\d+ arguments, but given \\d+")
                .matches(input = this) -> invalidArgumentsNumber

            else -> genericError
        }
    } ?: genericError
}
