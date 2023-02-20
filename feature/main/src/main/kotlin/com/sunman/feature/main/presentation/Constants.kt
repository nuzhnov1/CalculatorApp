package com.sunman.feature.main.presentation

import com.sunman.libcalculator.AngleUnit
import java.math.MathContext


internal const val CHAR_SEPARATOR = ' '
internal const val NUMBER_PRECISION = 12
internal const val MAX_TRAILING_ZEROS = 9
internal const val MESSAGE_APPEAR_TIME_MS = 1_000L

internal val EMPTY_EXPRESSION = CalculationFieldState.Expression(representation = "")
internal val DEFAULT_ANGLE_UNIT = AngleUnit.RADIAN
internal val CALCULATOR_MATH_CONTEXT: MathContext = MathContext.DECIMAL128
