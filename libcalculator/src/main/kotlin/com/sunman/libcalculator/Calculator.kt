package com.sunman.libcalculator

import ch.obermuhlner.math.big.BigDecimalMath.e
import ch.obermuhlner.math.big.BigDecimalMath.pi
import com.sunman.libcalculator.internal.Parser
import com.sunman.libcalculator.internal.PostfixItem
import java.math.BigDecimal
import java.math.MathContext

/**
 * The main class of the calculator.
 * This class is used to compute expressions read from various reading streams.
 *
 * @param mc the precision of numbers and the rounding mode used in calculations.
 * @throws UnsupportedOperationException If the [mc] has unlimited precision.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Calculator(private val mc: MathContext = MathContext.DECIMAL128) {

    private val operandStack = ArrayDeque<Operand>()
    private val actualArgumentsStack = ArrayDeque<Number>()
    private val constants: Map<String, Number> = mapOf(
        "π" to Number(pi(mc)),
        "e" to Number(e(mc))
    )


    /**
     * Execute expression from the [expression].
     * 1. If the [expression] is an empty string, the calculator returns [Nothing].
     * 2. If the [expression] is valid expression, the calculator returns [Number] instance.
     * 3. If the [expression] is invalid expression, the calculator returns [Error] instance.
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    fun executeExpression(expression: String): CalculationResult {
        val postfixItems = Parser().parse(expression)

        try {
            postfixItems.forEach {
                when (it.kind) {
                    PostfixItem.Kind.NUMBER -> operandStack.addFirst(makeNumber(it.lexem))
                    PostfixItem.Kind.CONSTANT -> operandStack.addFirst(constants[it.lexem]!!)
                    PostfixItem.Kind.IDENT -> operandStack.addFirst(Ident(it.lexem))
                    PostfixItem.Kind.OP -> executeOperator(it.lexem)
                    PostfixItem.Kind.ACTION -> performAction(it.lexem)
                }
            }

            return operandStack.popNumberOrNull() ?: Nothing
        } catch (e: RuntimeException) {
            return Error(e)
        } finally {
            // Reset calculator state:
            operandStack.clear()
            actualArgumentsStack.clear()
        }
    }

    private fun makeNumber(representation: String) =
        try {
            Number(BigDecimal(representation, mc))
        } catch (e: NumberFormatException) {
            throw ExecutionException("'$representation' could not be converted to a number", e)
        }

    private fun ArrayDeque<Operand>.popNumberOrNull() =
        when (val operand = removeFirstOrNull()) {
            is Number -> operand
            else -> null
        }

    private fun ArrayDeque<Operand>.popNumber() = popNumberOrNull()!!
    private fun ArrayDeque<Operand>.popIdent() = removeFirst() as Ident

    private fun executeOperator(operator: String) {
        when (operator) {
            "+" -> {
                val second = operandStack.popNumber()
                val first = operandStack.popNumber()

                operandStack.addFirst(first.add(second, mc))
            }

            "-" -> {
                val second = operandStack.popNumber()
                val first = operandStack.popNumber()

                operandStack.addFirst(first.subtract(second, mc))
            }

            "*" -> {
                val second = operandStack.popNumber()
                val first = operandStack.popNumber()

                operandStack.addFirst(first.multiply(second, mc))
            }

            "÷" -> {
                val second = operandStack.popNumber()
                val first = operandStack.popNumber()

                operandStack.addFirst(first.divide(second, mc))
            }

            "u-" -> {
                val operand = operandStack.popNumber()
                operandStack.addFirst(operand.negate(mc))
            }

            "^" -> {
                val second = operandStack.popNumber()
                val first = operandStack.popNumber()

                operandStack.addFirst(first.pow(second, mc))
            }

            "!" -> {
                val number = operandStack.popNumber()
                operandStack.addFirst(number.factorial(mc))
            }

            "%" -> {
                val number = operandStack.popNumber()
                operandStack.addFirst(number.percent(mc))
            }
        }
    }

    private fun performAction(actionName: String) {
        when (actionName) {
            "invoke" -> invokeFunction()
            "put_arg" -> actualArgumentsStack.addFirst(operandStack.popNumber())
        }
    }

    private fun invokeFunction() {
        val function = operandStack.popIdent()
        val result = invokeFunction(function.name, actualArgumentsStack, mc)

        operandStack.addFirst(result)
    }


    internal sealed interface Operand
    internal data class Ident(val name: String) : Operand
}
