package com.sunman.libcalculator.parser

/**
 * A sealed interface of various postfix token kinds.
 * A postfix token may contain a lexem - an instance of this token.
 */
internal sealed interface PostfixToken {
    data class Number(val lexem: String) : PostfixToken
    data class Identifier(val lexem: String) : PostfixToken
    data class Constant(val lexem: String) : PostfixToken

    sealed interface Operation : PostfixToken {
        object Negation : Operation
        object Addition : Operation
        object Subtraction : Operation
        object Multiplication : Operation
        object Division : Operation
        object Power : Operation
        object Factorial : Operation
        object Percent : Operation
    }

    sealed interface Action : PostfixToken {
        object Invoke : Action
        object PutArgument : Action
    }
}
