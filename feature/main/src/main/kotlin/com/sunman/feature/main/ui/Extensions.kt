package com.sunman.feature.main.ui

import android.content.Context
import com.sunman.libcalculator.*
import com.sunman.libcalculator.Nothing
import com.sunman.libcalculator.Number
import com.sunman.feature.main.ui.resources.*


internal fun String.toDisplayableString() = when (val label = lowercase()) {
    in "0".."9", ".", "+", "-", "*", "÷", "%", "()", ",", "^", "!", "π", "e" -> label
    "2^x" -> "2^("
    "10^x" -> "10^("
    "x^2" -> "^2"
    "x^3" -> "^3"
    else -> "$label("
}

internal fun CalculationResult.toDisplayableString(context: Context) =
    when (this) {
        is Number, is Nothing -> toString()
        is Error -> e.toDisplayableString(context)
    }

private fun Throwable.toDisplayableString(context: Context) = context.resources.run {
    localizedMessage?.lowercase()?.run {
        when {
            this@toDisplayableString is SyntaxException -> invalidExpression
            contains(other = "division by zero", ignoreCase = true) -> divisionByZero
            contains(other = "illegal", ignoreCase = true) -> invalidArgument

            Regex(pattern = "the function '\\w+' expects \\d+ arguments, but given \\d+")
                .matches(input = this) -> invalidArgumentsNumber

            else -> genericError
        }
    } ?: genericError
}

internal fun AngleUnit.toDisplayableString(context: Context) = context.resources.run {
    when (this@toDisplayableString) {
        AngleUnit.RADIAN -> rad
        AngleUnit.DEGREE -> deg
        AngleUnit.GRADIAN -> grad
    }
}
