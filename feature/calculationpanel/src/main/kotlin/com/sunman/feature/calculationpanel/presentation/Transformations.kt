package com.sunman.feature.calculationpanel.presentation

import android.content.Context
import android.widget.Button
import com.sunman.feature.calculationpanel.R
import com.sunman.libcalculator.SyntaxException

internal fun Button.toInternalRepresentation() =
    when (val label = text.toString().lowercase()) {
        in "0".."9", ".", "+", "-", "*", "%", ",", "^", "!", "e" -> label
        "÷" -> "/"
        "√" -> "sqrt("
        "2^x" -> "2^("
        "10^x" -> "10^("
        "π" -> "pi"
        else -> "$label("
    }

internal fun String.toDisplayableRepresentation() = lowercase()
    .replace(Regex("sqrt"), "√")
    .replace(Regex("pi"), "π")
    .replace(Regex("/"), "÷")

internal fun Exception.toDisplayableError(context: Context) =
    localizedMessage?.lowercase()?.run {
        when {
            this@toDisplayableError is SyntaxException -> context.getString(R.string.invalidExpression)
            contains("division by zero") -> context.getString(R.string.divisionByZero)
            contains("illegal") -> context.getString(R.string.illegalArgument)
            else -> context.getString(R.string.unknownError)
        }
    } ?: context.getString(R.string.unknownError)
