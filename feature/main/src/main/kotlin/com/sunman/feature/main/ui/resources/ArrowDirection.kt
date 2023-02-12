package com.sunman.feature.main.ui.resources


internal enum class ArrowDirection {
    UP, DOWN
}


internal fun ArrowDirection.changeDirection() = when (this) {
    ArrowDirection.UP -> ArrowDirection.DOWN
    ArrowDirection.DOWN -> ArrowDirection.UP
}
