package com.sunman.libcalculator

import com.sunman.libcalculator.parser.Parser
import com.sunman.libcalculator.parser.PostfixToken
import java.math.BigDecimal
import java.math.MathContext

/**
 * Calculator class.
 * This class is used to compute expressions read from strings.
 *
 * Expression is the single string, that may contains spaces of horizontal tabulations.
 * Examples of expressions:
 * * 1 + 1;
 * * sin(12);
 * * 5!^2
 *
 * Supported operators:
 * 1. '+' - addition. Priority: 4. Example 1 + 1;
 * 2. '-' - subtraction. Priority: 4. Example 1 - 1;
 * 3. '*' - multiplication. Priority: 3. Example 1 * 1;
 * 4. '/' - division. Priority: 3. Example 1 / 1;
 * 5. '^' - power. Priority: 2. Example 1 ^ 1;
 * 6. '!' - factorial. Priority: 1. Example 1!;
 * 7. '%' - percent. Priority: 1.Example 1%;
 *
 * Supported constants: π and e (Euler's number).
 *
 * Supported functions:
 * 1. Trigonometric functions:
 *     1. 'sin' - sine. Signature: f(x) = sin(x);
 *     2. 'cos' - cosine. Signature: f(x) = cos(x);
 *     3. 'tan' - tangent. Signature: f(x) = tan(x);
 *     4. 'cot' - cotangent. Signature: f(x) = cot(x);
 *     5. 'sec' - secant. Signature: f(x) = sec(x);
 *     6. 'cosec' - cosecant. Signature: f(x) = cosec(x);
 * 2. Inverse trigonometric functions:
 *     1. 'asin' - arcsine. Signature: f(x) = arcsine(x);
 *     2. 'acos' - arccosine. Signature: f(x) = arccosine(x);
 *     3. 'atan' - arctangent. Signature: f(x) = arctangent(x);
 *     4. 'acot' - arccotangent. Signature: f(x) = arccotangent(x);
 *     5. 'asec' - arcsecant. Signature: f(x) = arcsecant(x);
 *     6. 'acosec' - arccosecant. Signature: f(x) = arccosecant(x);
 * 3. Hyperbolic functions:
 *     1. 'sinh' - hyperbolic sine. Signature: f(x) = sinh(x);
 *     2. 'cosh' - hyperbolic cosine. Signature: f(x) = cosh(x);
 *     3. 'tanh' - hyperbolic tangent. Signature: f(x) = tanh(x);
 *     4. 'coth' - hyperbolic cotangent. Signature: f(x) = coth(x);
 *     5. 'sech' - hyperbolic secant. Signature: f(x) = sech(x);
 *     6. 'csch' - hyperbolic cosecant. Signature: f(x) = csch(x);
 * 4. Inverse hyperbolic functions:
 *     1. 'asinh' - hyperbolic arcsine. Signature: f(x) = asinh(x);
 *     2. 'acosh' - hyperbolic arccosine. Signature: f(x) = acosh(x);
 *     3. 'atanh' - hyperbolic arctangent. Signature: f(x) = atanh(x);
 *     4. 'acoth' - hyperbolic arccotangent. Signature: f(x) = acoth(x);
 *     5. 'asech' - hyperbolic arcsecant. Signature: f(x) = asech(x);
 *     6. 'acsch' - hyperbolic arccosecant. Signature: f(x) = acsch(x);
 * 5. Root functions:
 *     1. 'sqrt' or '√' - square root function. Signature: f(x) = sqrt(x) or f(x) = √(x);
 *     2. 'cbrt' - cube root function. Signature: f(x) = cbrt(x);
 *     3. 'root' - Nth root function. Signature: f(x) = root(n, x), when 'n' - is the root degree;
 * 6. Exponential and logarithmic functions:
 *     1. 'exp' - exponential function. Signature: f(x) = exp(x);
 *     2. 'ln' - natural logarithmic function. Signature: f(x) = ln(x);
 *     3. 'log2' - logarithmic function with base 2. Signature: f(x) = log2(x);
 *     4. 'lg' - logarithmic function with base 10. Signature: f(x) = lg(x);
 *     5. 'log' - logarithmic function with arbitrary base. Signature: f(x) = log(b, x), when 'b' -
 *     is base of the logarithmic function;
 * 7. Conversion functions to an integer value:
 *     1. 'ceil' - computes the smallest integer that is greater than or equal to x.
 *     Signature: f(x) = ceil(x);
 *     2. 'floor' - computes the greatest integer that is less than or equal to x.
 *     Signature: f(x) = floor(x);
 *     3. 'round' - rounds number to nearest integer (HALF-UP rounding).
 *     Signature: f(x) = round(x);
 * 8. Other functions:
 *     1. 'abs' - computes the absolute value of the current number. Signature: f(x) = abs(x);
 *     2. 'fraction' - computes the fraction part of the number. Signature: f(x) = fraction(x);
 *     3. 'hypot' - computes the hypotenuse by two specified cathets. Signature: f(x) = hypot(x, y);
 *
 * Supported angle units:
 * 1. 'rad' - radian unit;
 * 2. 'deg' - degree unit;
 * 3. 'grad' - gradian unit;
 *
 * @param mc the precision of numbers and the rounding mode used in calculations.
 *
 * @throws UnsupportedOperationException If the [mc] has unlimited precision.
 */
class Calculator(mc: MathContext = MathContext.DECIMAL128) {

    private val operandStack = ArrayDeque<Operand>()
    private val calculatorContext = CalculatorContext(mc, angleUnit = AngleUnit.RADIAN)
    private val mc = calculatorContext.mc
    private val actualArgumentsStack = calculatorContext.actualArgumentsStack
    private val constants: Map<String, CalculationNumber> = calculatorContext.constants

    var angleUnit: AngleUnit
        get() = calculatorContext.angleUnit
        set(value) { calculatorContext.angleUnit = value }


    /**
     * Calculate expression from the [expression] string.
     * 1. If the [expression] is an empty string, the calculator returns [CalculationNothing].
     * 2. If the [expression] is valid expression, the calculator returns [CalculationNumber]
     * instance.
     * 3. If the [expression] is invalid expression, the calculator returns [CalculationError]
     * instance.
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    fun calculateExpression(expression: String): CalculationResult {
        try {
            val postfixItems = Parser().parse(expression)

            postfixItems.forEach {
                when (it) {
                    is PostfixToken.Number -> operandStack.addFirst(makeNumber(it.lexem))
                    is PostfixToken.Constant -> operandStack.addFirst(constants[it.lexem]!!)
                    is PostfixToken.Identifier -> operandStack.addFirst(Identifier(it.lexem))
                    is PostfixToken.Operation -> executeOperator(it)
                    is PostfixToken.Action -> performAction(it)
                }
            }

            return operandStack.popNumberOrNull() ?: CalculationNothing
        } catch (e: Throwable) {
            return CalculationError(e)
        } finally {
            // Reset calculator state:
            operandStack.clear()
            actualArgumentsStack.clear()
        }
    }

    private fun executeOperator(operation: PostfixToken.Operation) = when (operation) {
        PostfixToken.Operation.Addition -> {
            val second = operandStack.popNumber()
            val first = operandStack.popNumber()

            operandStack.addFirst(first.add(second, mc))
        }

        PostfixToken.Operation.Subtraction -> {
            val second = operandStack.popNumber()
            val first = operandStack.popNumber()

            operandStack.addFirst(first.subtract(second, mc))
        }

        PostfixToken.Operation.Multiplication -> {
            val second = operandStack.popNumber()
            val first = operandStack.popNumber()

            operandStack.addFirst(first.multiply(second, mc))
        }

        PostfixToken.Operation.Division -> {
            val second = operandStack.popNumber()
            val first = operandStack.popNumber()

            operandStack.addFirst(first.divide(second, mc))
        }

        PostfixToken.Operation.Power -> {
            val second = operandStack.popNumber()
            val first = operandStack.popNumber()

            operandStack.addFirst(first.pow(second, mc))
        }

        PostfixToken.Operation.Factorial -> {
            val number = operandStack.popNumber()
            operandStack.addFirst(number.factorial(mc))
        }

        PostfixToken.Operation.Percent -> {
            val number = operandStack.popNumber()
            operandStack.addFirst(number.percent(mc))
        }

        PostfixToken.Operation.Negation -> {
            val operand = operandStack.popNumber()
            operandStack.addFirst(operand.negate(mc))
        }
    }

    private fun performAction(action: PostfixToken.Action) = when (action) {
        PostfixToken.Action.Invoke -> invokeFunction()
        PostfixToken.Action.PutArgument -> actualArgumentsStack.addFirst(operandStack.popNumber())
    }

    private fun invokeFunction() {
        val function = operandStack.popIdentifier()
        val result = calculatorContext.invokeFunction(function.name)

        operandStack.addFirst(result)
    }

    private fun makeNumber(representation: String): CalculationNumber =
        try {
            CalculationNumber(BigDecimal(representation, mc))
        } catch (e: NumberFormatException) {
            throw ExecutionException("'$representation' could not be converted to a number", e)
        }

    private fun ArrayDeque<Operand>.popNumberOrNull(): CalculationNumber? =
        when (val operand = removeFirstOrNull()) {
            is CalculationNumber -> operand
            else -> null
        }

    private fun ArrayDeque<Operand>.popNumber() = popNumberOrNull()!!
    private fun ArrayDeque<Operand>.popIdentifier() = removeFirst() as Identifier


    internal sealed interface Operand
    private data class Identifier(val name: String) : Operand
}
