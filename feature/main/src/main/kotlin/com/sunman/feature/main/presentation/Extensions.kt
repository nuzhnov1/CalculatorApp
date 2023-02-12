package com.sunman.feature.main.presentation

import com.sunman.libcalculator.AngleUnit


internal val String.openParenthesisCount: Int get() {
    val totalOpen = count { chr -> chr == '(' }
    val totalClosed = count { chr -> chr == ')' }

    return totalOpen - totalClosed
}


internal fun Int.toAngleUnit() = AngleUnit.values()[this]
internal fun String.removeSeparators() = replace(oldValue = ITEM_SEPARATOR.toString(), newValue = "")
internal fun String.dropLastSeparator() = dropLast(1)
internal fun String.completeCalculationString() = this + ")".repeat(openParenthesisCount)
