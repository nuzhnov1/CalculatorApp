package com.sunman.libcalculator.parser

/**
 * A sealed interface of various token kinds.
 * A token may contain a lexem - an instance of this token.
 */
internal sealed interface Token : GrammarSymbol {
    object EOL : Token {
        override fun toString() = "end of line"
    }

    data class Number(val lexem: String) : Token {
        override fun toString() = lexem
    }

    data class Identifier(val lexem: String) : Token {
        override fun toString() = lexem
    }

    data class Constant(val lexem: String) : Token {
        override fun toString() = lexem
    }

    sealed interface Operator : Token {
        object Plus : Operator {
            override fun toString() = "+"
        }

        object Minus : Operator {
            override fun toString() = "-"
        }

        object Multiplication : Operator {
            override fun toString() = "*"
        }

        object Division : Operator {
            override fun toString() = "/"
        }

        object Power : Operator {
            override fun toString() = "^"
        }

        object Factorial : Operator {
            override fun toString() = "!"
        }

        object Percent : Operator {
            override fun toString() = "%"
        }
    }

    object Comma : Token {
        override fun toString() = ","
    }

    sealed interface Parentheses : Token {
        object Open : Parentheses {
            override fun toString() = "("
        }

        object Close : Parentheses {
            override fun toString() = ")"
        }
    }

    data class Space(val lexem: String) : Token {
        override fun toString() = lexem
    }
}
