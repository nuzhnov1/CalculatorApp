package com.sunman.feature.main.ui

import android.content.Context
import android.util.DisplayMetrics
import com.sunman.feature.main.R
import com.sunman.libcalculator.*
import com.sunman.libcalculator.Nothing
import com.sunman.libcalculator.Number

fun CalculationResult.toDisplayableString(context: Context) =
    when (this) {
        is Number, is Nothing -> toString()
        is Error -> e.toDisplayableError(context)
    }

private fun Throwable.toDisplayableError(context: Context) =
    localizedMessage?.lowercase()?.run {
        when {
            this@toDisplayableError is SyntaxException -> context.getString(R.string.invalidExpression)
            contains("division by zero", true) -> context.getString(R.string.divisionByZero)
            contains("illegal", true) -> context.getString(R.string.invalidArgument)

            Regex("The function '\\w+' expects \\d+ arguments, but given \\d+")
                .matches(this) -> context.getString(R.string.invalidArgumentsNumber)

            else -> context.getString(R.string.genericError)
        }
    } ?: context.getString(R.string.genericError)


fun AngleUnit.toDisplayableString(context: Context) =
    when (this) {
        AngleUnit.RADIAN -> context.getString(R.string.rad)
        AngleUnit.DEGREE -> context.getString(R.string.deg)
        AngleUnit.GRADIAN -> context.getString(R.string.grad)
    }

fun Int.toDP(context: Context): Int {
    val doubleValue = this.toDouble()
    val densityDpi = context.resources.displayMetrics.densityDpi.toDouble()

    return (doubleValue / densityDpi / DisplayMetrics.DENSITY_DEFAULT).toInt()
}
