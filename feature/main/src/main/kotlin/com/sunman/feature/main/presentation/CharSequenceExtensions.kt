package com.sunman.feature.main.presentation


internal val CharSequence.openParenthesisCount: Int get() {
    val totalOpenParenthesis = count { chr -> chr == '(' }
    val totalClosedParenthesis = count { chr -> chr == ')' }

    return totalOpenParenthesis - totalClosedParenthesis
}

internal val CharSequence.isJustNumber get() =
    Regex(pattern = "(\\d+\\.?\\d*)|(\\d*\\.?\\d+)").matches(input = this)


internal fun CharSequence.addSeparators(separator: Char): CharSequence = map { it }.joinToString(
    separator = separator.toString(),
    postfix = separator.toString()
)

internal fun CharSequence.removeSeparators(): CharSequence = filter { it != CHAR_SEPARATOR }

internal fun CharSequence.dropLastSeparator() = dropLast(1)

internal fun CharSequence.lastItemOrNull(): Char? = dropLastSeparator().lastOrNull()

internal fun CharSequence.dropLastItem(): CharSequence =
    dropLastSeparator().dropLastWhile { it != CHAR_SEPARATOR }

internal fun CharSequence.completeExpression(): CharSequence =
    this.removeSeparators() + ")".repeat(openParenthesisCount)

internal operator fun CharSequence.plus(item: Char): CharSequence = this.toString() + item

internal operator fun CharSequence.plus(other: CharSequence): CharSequence =
    this.toString() + other.toString()
