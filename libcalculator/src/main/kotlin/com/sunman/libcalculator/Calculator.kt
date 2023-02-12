package com.sunman.libcalculator

import com.sunman.libcalculator.internal.Parser
import com.sunman.libcalculator.internal.PostfixItem
import java.math.BigDecimal
import java.math.MathContext

/**
 * Calculator class.
 * This class is used to compute expressions read from strings.
 *
 * @param mc the precision of numbers and the rounding mode used in calculations.
 * @throws UnsupportedOperationException If the [mc] has unlimited precision.
 */
class Calculator(mc: MathContext = MathContext.DECIMAL128) {

    private val operandStack = ArrayDeque<Operand>()
    private val calculatorContext = CalculatorContext(mc, angleUnit = AngleUnit.RADIAN)
    private val mc = calculatorContext.mc
    private val actualArgumentsStack = calculatorContext.actualArgumentsStack
    private val constants: Map<String, Number> = calculatorContext.constants

    var angleUnit: AngleUnit
        get() = calculatorContext.angleUnit
        set(value) {
            calculatorContext.angleUnit = value
        }


    /**
     * Calculate expression from the [expression] string.
     * 1. If the [expression] is an empty string, the calculator returns [Nothing].
     * 2. If the [expression] is valid expression, the calculator returns [Number] instance.
     * 3. If the [expression] is invalid expression, the calculator returns [Error] instance.
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    fun calculateExpression(expression: String): CalculationResult {
        try {
            val postfixItems = Parser().parse(expression)

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
        } catch (e: Throwable) {
            return Error(e)
        } finally {
            // Reset calculator state:
            operandStack.clear()
            actualArgumentsStack.clear()
        }
    }

    private fun makeNumber(representation: String): Number =
        try {
            Number(BigDecimal(representation, mc))
        } catch (e: NumberFormatException) {
            throw ExecutionException("'$representation' could not be converted to a number", e)
        }

    private fun ArrayDeque<Operand>.popNumberOrNull(): Number? =
        when (val operand = removeFirstOrNull()) {
            is Number -> operand
            else -> null
        }

    private fun ArrayDeque<Operand>.popNumber() = popNumberOrNull()!!
    private fun ArrayDeque<Operand>.popIdent() = removeFirst() as Ident

    private fun executeOperator(operator: String) = when (operator) {
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

        else -> throw IllegalStateException("Unknown operator '$operator'")
    }

    private fun performAction(actionName: String) = when (actionName) {
        "invoke" -> invokeFunction()
        "put_arg" -> actualArgumentsStack.addFirst(operandStack.popNumber())
        else -> throw IllegalStateException("Unknown action '$actionName'")
    }

    private fun invokeFunction() {
        val function = operandStack.popIdent()
        val result = calculatorContext.invokeFunction(function.name)

        operandStack.addFirst(result)
    }


    internal sealed interface Operand
    internal data class Ident(val name: String) : Operand
}
