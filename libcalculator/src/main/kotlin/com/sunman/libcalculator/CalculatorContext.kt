package com.sunman.libcalculator

import ch.obermuhlner.math.big.BigDecimalMath.*
import ch.obermuhlner.math.big.kotlin.bigdecimal.div
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

/**
 * Context class for calculator.
 * This class is mainly used to perform mathematical functions of a calculator.
 *
 * @property mc the precision of numbers and the rounding mode used in calculations.
 * @property actualArgumentsStack current arguments of the function.
 * @property constants static declared constants, such as the 'π' or 'e'.
 * @property angleUnit the unit of angle measurement used for trigonometric functions.
 */
internal class CalculatorContext(
    val mc: MathContext,
    var angleUnit: AngleUnit
) {

    // Constants that used in a private context:
    private val pi = pi(mc)
    private val e = e(mc)
    private val minusOne = BigDecimal(-1, mc)
    private val zero = BigDecimal.ZERO
    private val one = BigDecimal.ONE
    private val two = BigDecimal(2, mc)
    private val three = BigDecimal(3, mc)
    private val doublePi = two.multiply(pi, mc)
    private val oneHundredEighty = BigDecimal(180, mc)
    private val twoHundred = BigDecimal(200, mc)
    private val epsilon = BigDecimal.ONE.movePointLeft(mc.precision / 2)

    val actualArgumentsStack: ArrayDeque<CalculationNumber> = ArrayDeque()
    val constants: Map<String, CalculationNumber> = mapOf(
        "$PI" to CalculationNumber(pi),
        "$E" to CalculationNumber(e)
    )


    /**
     * Invokes the built-in calculator function by [name].
     *
     * @throws ExecutionException If the actual arguments count does not match the required one,
     * or unknown function is attempted to invoke.
     * @throws ArithmeticException If an error was occurred during the calculation.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    fun invokeFunction(name: String): CalculationNumber = when (name) {
        // Trigonometric functions:

        SIN -> {
            checkArgumentsCount(functionName = SIN, requiredCount = 1)
            sin(x = actualArgumentsStack.removeFirst())
        }

        COS -> {
            checkArgumentsCount(functionName = COS, requiredCount = 1)
            cos(x = actualArgumentsStack.removeFirst())
        }

        TAN -> {
            checkArgumentsCount(functionName = TAN, requiredCount = 1)
            tan(x = actualArgumentsStack.removeFirst())
        }

        COT -> {
            checkArgumentsCount(functionName = COT, requiredCount = 1)
            cot(x = actualArgumentsStack.removeFirst())
        }

        SEC -> {
            checkArgumentsCount(functionName = SEC, requiredCount = 1)
            sec(x = actualArgumentsStack.removeFirst())
        }

        COSEC -> {
            checkArgumentsCount(functionName = COSEC, requiredCount = 1)
            cosec(x = actualArgumentsStack.removeFirst())
        }

        // Inverse trigonometric functions:

        ASIN -> {
            checkArgumentsCount(functionName = ASIN, requiredCount = 1)
            asin(x = actualArgumentsStack.removeFirst())
        }

        ACOS -> {
            checkArgumentsCount(functionName = ACOS, requiredCount = 1)
            acos(x = actualArgumentsStack.removeFirst())
        }

        ATAN -> {
            checkArgumentsCount(functionName = ATAN, requiredCount = 1)
            atan(x = actualArgumentsStack.removeFirst())
        }

        ACOT -> {
            checkArgumentsCount(functionName = ACOT, requiredCount = 1)
            acot(x = actualArgumentsStack.removeFirst())
        }

        ASEC -> {
            checkArgumentsCount(functionName = ASEC, requiredCount = 1)
            asec(x = actualArgumentsStack.removeFirst())
        }

        ACOSEC -> {
            checkArgumentsCount(functionName = ACOSEC, requiredCount = 1)
            acosec(x = actualArgumentsStack.removeFirst())
        }

        // Hyperbolic functions:

        SINH -> {
            checkArgumentsCount(functionName = SINH, requiredCount = 1)
            sinh(x = actualArgumentsStack.removeFirst())
        }

        COSH -> {
            checkArgumentsCount(functionName = COSH, requiredCount = 1)
            cosh(x = actualArgumentsStack.removeFirst())
        }

        TANH -> {
            checkArgumentsCount(functionName = TANH, requiredCount = 1)
            tanh(x = actualArgumentsStack.removeFirst())
        }

        COTH -> {
            checkArgumentsCount(functionName = COTH, requiredCount = 1)
            coth(x = actualArgumentsStack.removeFirst())
        }

        SECH -> {
            checkArgumentsCount(functionName = SECH, requiredCount = 1)
            sech(x = actualArgumentsStack.removeFirst())
        }

        CSCH -> {
            checkArgumentsCount(functionName = CSCH, requiredCount = 1)
            csch(x = actualArgumentsStack.removeFirst())
        }

        // Inverse hyperbolic functions:

        ASINH -> {
            checkArgumentsCount(functionName = ASINH, requiredCount = 1)
            asinh(x = actualArgumentsStack.removeFirst())
        }

        ACOSH -> {
            checkArgumentsCount(functionName = ACOSH, requiredCount = 1)
            acosh(x = actualArgumentsStack.removeFirst())
        }

        ATANH -> {
            checkArgumentsCount(functionName = ATANH, requiredCount = 1)
            atanh(x = actualArgumentsStack.removeFirst())
        }

        ACOTH -> {
            checkArgumentsCount(functionName = ACOTH, requiredCount = 1)
            acoth(x = actualArgumentsStack.removeFirst())
        }

        ASECH -> {
            checkArgumentsCount(functionName = ASECH, requiredCount = 1)
            asech(x = actualArgumentsStack.removeFirst())
        }

        ACSCH -> {
            checkArgumentsCount(functionName = ACSCH, requiredCount = 1)
            acsch(x = actualArgumentsStack.removeFirst())
        }

        // Root functions:

        SQRT_CHAR.toString(), SQRT -> {
            checkArgumentsCount(functionName = SQRT, requiredCount = 1)
            sqrt(actualArgumentsStack.removeFirst())
        }

        CBRT -> {
            checkArgumentsCount(functionName = CBRT, requiredCount = 1)
            cbrt(actualArgumentsStack.removeFirst())
        }

        ROOT -> {
            checkArgumentsCount(functionName = ROOT, requiredCount = 2)

            val n = actualArgumentsStack.removeFirst()
            val x = actualArgumentsStack.removeFirst()

            root(x, n)
        }

        // Logarithmic functions:

        EXP -> {
            checkArgumentsCount(functionName = EXP, requiredCount = 1)
            exp(x = actualArgumentsStack.removeFirst())
        }

        LN -> {
            checkArgumentsCount(functionName = LN, requiredCount = 1)
            ln(x = actualArgumentsStack.removeFirst())
        }

        LOG2 -> {
            checkArgumentsCount(functionName = LOG2, requiredCount = 1)
            log2(x = actualArgumentsStack.removeFirst())
        }

        LG -> {
            checkArgumentsCount(functionName = LG, requiredCount = 1)
            lg(x = actualArgumentsStack.removeFirst())
        }

        LOG -> {
            checkArgumentsCount(functionName = LOG, requiredCount = 2)

            val x = actualArgumentsStack.removeFirst()
            val b = actualArgumentsStack.removeFirst()

            log(b, x)
        }

        // Conversion functions to an integer value:

        CEIL -> {
            checkArgumentsCount(functionName = CEIL, requiredCount = 1)
            ceil(x = actualArgumentsStack.removeFirst())
        }

        FLOOR -> {
            checkArgumentsCount(functionName = FLOOR, requiredCount = 1)
            floor(x = actualArgumentsStack.removeFirst())
        }

        ROUND -> {
            checkArgumentsCount(functionName = ROUND, requiredCount = 1)
            round(x = actualArgumentsStack.removeFirst())
        }

        // Other functions:

        ABS -> {
            checkArgumentsCount(functionName = ABS, requiredCount = 1)
            abs(x = actualArgumentsStack.removeFirst())
        }

        FRACTION -> {
            checkArgumentsCount(functionName = FRACTION, requiredCount = 1)
            fraction(x = actualArgumentsStack.removeFirst())
        }

        HYPOT -> {
            checkArgumentsCount(functionName = HYPOT, requiredCount = 2)

            val y = actualArgumentsStack.removeFirst()
            val x = actualArgumentsStack.removeFirst()

            hypot(x, y)
        }

        else -> throw ExecutionException("Unknown function '$name'")
    }

    /**
     * Compares the actual arguments count with the required one.
     *
     * @throws ExecutionException If the actual arguments count does not match the required one.
     */
    private fun checkArgumentsCount(functionName: String, requiredCount: Int) {
        val actualCount = actualArgumentsStack.count()

        if (actualCount != requiredCount) {
            actualArgumentsStack.clear()

            throw ExecutionException(
                "The function '$functionName' expects $requiredCount arguments, " +
                        "but given $actualCount"
            )
        }
    }

    // Trigonometric functions:

    /**
     * Returns the sine of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun sin(x: CalculationNumber): CalculationNumber {
        val radiansX = x.value.toRadians(angleUnit)
        val result = sin(mod2pi(radiansX), mc)

        return when {
            isEquals(result, minusOne) -> CalculationNumber(minusOne)
            isEquals(result, zero) -> CalculationNumber(zero)
            isEquals(result, one) -> CalculationNumber(one)
            else -> CalculationNumber(result)
        }
    }

    /**
     * Returns the cosine of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun cos(x: CalculationNumber): CalculationNumber {
        val radiansX = x.value.toRadians(angleUnit)
        val result = cos(mod2pi(radiansX), mc)

        return when {
            isEquals(result, minusOne) -> CalculationNumber(minusOne)
            isEquals(result, zero) -> CalculationNumber(zero)
            isEquals(result, one) -> CalculationNumber(one)
            else -> CalculationNumber(result)
        }
    }

    /**
     * Returns the tangent of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is equals to π * k / 2 in radians,
     * where k is odd.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun tan(x: CalculationNumber): CalculationNumber {
        val sinResult = sin(x).value
        val cosResult = cos(x).value

        return if (cosResult.signum() == 0) {
            when (angleUnit) {
                AngleUnit.RADIAN -> throw ArithmeticException(
                    "Illegal $TAN(x) for x = $PI * k / 2, where k is odd: x = $x"
                )

                AngleUnit.DEGREE -> throw ArithmeticException(
                    "Illegal $TAN(x) for x = 90° * k, where k is odd: x = $x"
                )

                AngleUnit.GRADIAN -> throw ArithmeticException(
                    "Illegal $TAN(x) for x = 100g * k, where k is odd: x = $x"
                )
            }

        } else {
            CalculationNumber(value = sinResult.divide(cosResult, mc))
        }
    }

    /**
     * Returns the cotangent of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is equals to π * k in radians,
     * where k is an integer.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun cot(x: CalculationNumber): CalculationNumber {
        val sinResult = sin(x).value
        val cosResult = cos(x).value

        return if (sinResult.signum() == 0) {
            when (angleUnit) {
                AngleUnit.RADIAN -> throw ArithmeticException(
                    "Illegal $COT(x) for x = $PI * k, where k is an integer: x = $x"
                )

                AngleUnit.DEGREE -> throw ArithmeticException(
                    "Illegal $COT(x) for x = 180° * k, where k is an integer: x = $x"
                )

                AngleUnit.GRADIAN -> throw ArithmeticException(
                    "Illegal $COT(x) for x = 200g * k, where k is an integer: x = $x"
                )
            }
        } else {
            CalculationNumber(value = cosResult.divide(sinResult, mc))
        }
    }

    /**
     * Returns the secant of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is equals to π * k / 2 in radians,
     * where k is odd.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun sec(x: CalculationNumber): CalculationNumber {
        val cosResult = cos(x).value

        return if (cosResult.signum() == 0) {
            when (angleUnit) {
                AngleUnit.RADIAN -> throw ArithmeticException(
                    "Illegal $SEC(x) for x = $PI * k / 2, where k is odd: x = $x"
                )

                AngleUnit.DEGREE -> throw ArithmeticException(
                    "Illegal $SEC(x) for x = 90° * k, where k is odd: x = $x"
                )

                AngleUnit.GRADIAN -> throw ArithmeticException(
                    "Illegal $SEC(x) for x = 100g * k, where k is odd: x = $x"
                )
            }

        } else {
            CalculationNumber(value = one.divide(cosResult, mc))
        }
    }

    /**
     * Returns the cosecant of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is equals to π * k in radians,
     * where k is an integer.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun cosec(x: CalculationNumber): CalculationNumber {
        val sinResult = sin(x).value

        return if (sinResult.signum() == 0) {
            when (angleUnit) {
                AngleUnit.RADIAN -> throw ArithmeticException(
                    "Illegal $COSEC(x) for x = $PI * k, where k is an integer: x = $x"
                )

                AngleUnit.DEGREE -> throw ArithmeticException(
                    "Illegal $COSEC(x) for x = 180° * k, where k is an integer: x = $x"
                )

                AngleUnit.GRADIAN -> throw ArithmeticException(
                    "Illegal $COSEC(x) for x = 200g * k, where k is an integer: x = $x"
                )
            }
        } else {
            CalculationNumber(value = one.divide(sinResult, mc))
        }
    }

    // Inverse trigonometric functions:

    /**
     * Returns the arcsine of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value does not belong to [-1, +1].
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun asin(x: CalculationNumber): CalculationNumber = when {
        x.value < minusOne -> throw ArithmeticException("Illegal $ASIN(x) for x < -1: x = $x")
        x.value > one -> throw ArithmeticException("Illegal $ASIN(x) for x > 1: x = $x")
        else -> CalculationNumber(value = asin(x.value, mc).toOtherUnit(angleUnit))
    }

    /**
     * Returns the arccosine of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value does not belong to [-1, +1].
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun acos(x: CalculationNumber): CalculationNumber = when {
        x.value < minusOne -> throw ArithmeticException("Illegal $ACOS(x) for x < -1: x = $x")
        x.value > one -> throw ArithmeticException("Illegal $ACOS(x) for x > 1: x = $x")
        else -> CalculationNumber(value = acos(x.value, mc).toOtherUnit(angleUnit))
    }

    /**
     * Returns the arctangent of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun atan(x: CalculationNumber) =
        CalculationNumber(value = atan(x.value, mc).toOtherUnit(angleUnit))

    /**
     * Returns the arccotangent of the [x] value, rounding it to the precision specified
     * in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun acot(x: CalculationNumber) =
        CalculationNumber(value = acot(x.value, mc).toOtherUnit(angleUnit))

    /**
     * Returns the arcsecant of the [x] value, rounding it to the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value belong to (-1, +1).
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun asec(x: CalculationNumber): CalculationNumber =
        if (x.value > minusOne && x.value < one) {
            throw ArithmeticException("Illegal $ASEC(x) for x > -1 and x < 1: x = $x")
        } else {
            CalculationNumber(value = acos(one.divide(x.value, mc), mc).toOtherUnit(angleUnit))
        }

    /**
     * Returns the arccosecant of the [x] value, rounding it to the precision specified
     * in the [mc].
     *
     * @throws ArithmeticException If the [x] value belong to (-1, +1).
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun acosec(x: CalculationNumber): CalculationNumber =
        if (x.value > minusOne && x.value < one) {
            throw ArithmeticException("Illegal $ACOSEC(x) for x > -1 and x < 1: x = $x")
        } else {
            CalculationNumber(value = asin(one.divide(x.value, mc), mc).toOtherUnit(angleUnit))
        }

    // Hyperbolic functions:

    /**
     * Returns the hyperbolic sine of the [x] value, rounding it to the precision specified
     * in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun sinh(x: CalculationNumber) = CalculationNumber(value = sinh(x.value, mc))

    /**
     * Returns the hyperbolic cosine of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun cosh(x: CalculationNumber) = CalculationNumber(value = cosh(x.value, mc))

    /**
     * Returns the hyperbolic tangent of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun tanh(x: CalculationNumber) = CalculationNumber(value = tanh(x.value, mc))

    /**
     * Returns the hyperbolic cotangent of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is equals to zero.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun coth(x: CalculationNumber): CalculationNumber =
        if (x.value.signum() == 0) {
            throw ArithmeticException("Illegal $COTH(x) for x = 0")
        } else {
            CalculationNumber(value = coth(x.value, mc))
        }

    /**
     * Returns the hyperbolic secant of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun sech(x: CalculationNumber) =
        CalculationNumber(value = one.divide(cosh(x.value, mc), mc))

    /**
     * Returns the hyperbolic cosecant of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is equals to zero.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun csch(x: CalculationNumber) =
        if (x.value.signum() == 0) {
            throw ArithmeticException("Illegal $CSCH(x) for x = 0")
        } else {
            CalculationNumber(value = one.divide(sinh(x.value, mc), mc))
        }

    // Inverse hyperbolic functions:

    /**
     * Returns the hyperbolic arcsine of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun asinh(x: CalculationNumber) = CalculationNumber(value = asinh(x.value, mc))

    /**
     * Returns the hyperbolic arccosine of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is less than 1.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun acosh(x: CalculationNumber): CalculationNumber =
        if (x.value < one) {
            throw ArithmeticException("Illegal $ACOSH(x) for x < 1: x = $x")
        } else {
            CalculationNumber(value = acosh(x.value, mc))
        }

    /**
     * Returns the hyperbolic arctangent of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is less than or equal to -1, or
     * if the [x] value is greater than or equal to 1.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun atanh(x: CalculationNumber): CalculationNumber = when {
        x.value <= minusOne -> throw ArithmeticException("Illegal $ATANH(x) for x <= -1: x = $x")
        x.value >= one -> throw ArithmeticException("Illegal $ATANH(x) for x >= 1: x = $x")
        else -> CalculationNumber(value = atanh(x.value, mc))
    }

    /**
     * Returns the hyperbolic arccotangent of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value belong to [-1, +1].
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun acoth(x: CalculationNumber): CalculationNumber =
        if (x.value in minusOne..one) {
            throw ArithmeticException("Illegal $ACOTH(x) for x >= -1 or x <= 1: x = $x")
        } else {
            CalculationNumber(value = acoth(x.value, mc))
        }

    /**
     * Returns the hyperbolic arcsecant of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value does not belong to (0, 1].
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun asech(x: CalculationNumber): CalculationNumber = when {
        x.value <= zero -> throw ArithmeticException("Illegal $ASECH(x) for x <= 0: x = $x")
        x.value > one -> throw ArithmeticException("Illegal $ASECH(x) for x > 1: x = $x")

        else -> {
            // 1 / x
            val firstAugend = one.divide(x.value, mc)
            // sqrt(1 / x^2 - 1)
            val secondAugend = sqrt(
                one.divide(pow(x.value, 2, mc), mc).subtract(one, mc),
                mc
            )

            // ln(1 / x + sqrt(1 / x^2 - 1))
            ln(CalculationNumber(value = firstAugend.add(secondAugend, mc)))
        }
    }

    /**
     * Returns the hyperbolic arccosecant of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value does not belong to (0, 1].
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun acsch(x: CalculationNumber) =
        if (x.value.signum() == 0) {
            throw ArithmeticException("Illegal $ACSCH(x) for x = 0")
        } else {
            // 1 / x
            val firstAugend = one.divide(x.value, mc)
            // sqrt(1 / x^2 + 1)
            val secondAugend = sqrt(
                one.divide(pow(x.value, 2, mc), mc).add(one, mc),
                mc
            )

            // ln(1 / x + sqrt(1 / x^2 + 1))
            ln(CalculationNumber(value = firstAugend.add(secondAugend, mc)))
        }

    // Root functions:

    /**
     * Returns the square root of the [x], rounding it to the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is less than zero.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun sqrt(x: CalculationNumber): CalculationNumber =
        if (x.value < zero) {
            throw ArithmeticException("Illegal $SQRT(x) for x < 0: x = $x")
        } else {
            CalculationNumber(value = sqrt(x.value, mc))
        }

    /**
     * Returns the cube root of the [x], rounding it to the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun cbrt(x: CalculationNumber): CalculationNumber =
        if (x.value < zero) {
            CalculationNumber(value = root(x.value.negate(mc), three, mc).negate(mc))
        } else {
            CalculationNumber(value = root(x.value, three, mc))
        }

    /**
     * Returns the [n]th-root of the [x], rounding it to the precision specified in the [mc].
     *
     * @throws ArithmeticException In the following cases:
     * 1. If the [x] value is less than zero, and the [n] value is an integer and even number.
     * 2. If the [x] value is less than zero and the [n] value is not an integer.
     * 3. If the [x] value is zero and the [n] value is less than zero.
     * 4. If the [x] value is greater than zero and the [n] value is zero.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun root(x: CalculationNumber, n: CalculationNumber): CalculationNumber = when {
        x.value < zero && n.isInteger -> {
            val rem = n.value
                .toBigIntegerExact()
                .mod(BigInteger("2"))

            if (rem != BigInteger.ZERO) {
                CalculationNumber(value = root(x.value.negate(mc), n.value, mc).negate(mc))
            } else {
                throw ArithmeticException(
                    "Illegal $ROOT(x, n) for x < 0 and even n: x = $x; n = $n"
                )
            }
        }

        x.value < zero && !n.isInteger -> {
            throw ArithmeticException(
                "Illegal $ROOT(x, n) for x < 0 and non-integer n: x = $x; n = $n"
            )
        }

        x.value.signum() == 0 && n.value < zero -> {
            throw ArithmeticException("Illegal $ROOT(x, n) for x = 0 and n < 0: n = $n")
        }

        x.value > zero && n.value.signum() == 0 -> {
            throw ArithmeticException("Illegal $ROOT(x, n) for x > 0 and n = 0: x = $x")
        }

        else -> CalculationNumber(value = root(x.value, n.value, mc))
    }

    // Exponential and logarithmic functions:

    /**
     * Returns the exponent of the [x] value, i.e. the result of e^[x] expression,
     * where e is the Euler number.
     * The result of calculation is rounded to the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun exp(x: CalculationNumber) = CalculationNumber(value = exp(x.value, mc))

    /**
     * Returns the natural logarithm of the [x] value, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is less than or equal to zero.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun ln(x: CalculationNumber): CalculationNumber =
        if (x.value <= zero) {
            throw ArithmeticException("Illegal $LN(x) for x <= 0: x = $x")
        } else {
            CalculationNumber(value = log(x.value, mc))
        }

    /**
     * Returns the logarithm of the [x] value with base 2, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is less than or equal to zero.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun log2(x: CalculationNumber): CalculationNumber =
        if (x.value <= zero) {
            throw ArithmeticException("Illegal $LOG2(x) for x <= 0: x = $x")
        } else {
            CalculationNumber(value = log2(x.value, mc))
        }

    /**
     * Returns the logarithm of the [x] value with base 10, rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException If the [x] value is less than or equal to zero.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun lg(x: CalculationNumber): CalculationNumber =
        if (x.value <= zero) {
            throw ArithmeticException("Illegal $LG(x) for x <= 0: x = $x")
        } else {
            CalculationNumber(value = log10(x.value, mc))
        }

    /**
     * Returns the logarithm of the [x] value with base [b], rounding it to
     * the precision specified in the [mc].
     *
     * @throws ArithmeticException In the following cases:
     * 1. If the [b] value is less than or equal to zero or one.
     * 2. If the [x] value is less than or equal to zero.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun log(b: CalculationNumber, x: CalculationNumber): CalculationNumber = when {
        b.value <= zero -> throw ArithmeticException("Illegal $LOG(b, x) for b <= 0: b = $b")
        b.value.compareTo(one) == 0 -> throw ArithmeticException("Illegal $LOG(b, x) for b = 1")
        x.value <= zero -> throw ArithmeticException("Illegal $LOG(b, x) for x <= 0: x = $x")
        else -> CalculationNumber(value = log(x.value, mc) / log(b.value, mc))
    }

    // Conversion functions to an integer value:

    /**
     * Returns the smallest integer greater than or equal to the [x] value.
     */
    private fun ceil(x: CalculationNumber) = CalculationNumber(
        value = x.value.setScale(0, RoundingMode.CEILING)
    )

    /**
     * Returns the greater integer less than or equal to the [x] value.
     */
    private fun floor(x: CalculationNumber) = CalculationNumber(
        value = x.value.setScale(0, RoundingMode.FLOOR)
    )

    /**
     * Returns the nearest integer relative to [x] value (the half-up rounding).
     * Examples of rounding:
     * * 5.5 to 6;
     * * 5.3 to 5;
     * * 5.7 to 6;
     * * -5.5 to -6;
     * * -5.3 to -5;
     * * -5.7 to -6;
     */
    private fun round(x: CalculationNumber) = CalculationNumber(
        value = x.value.setScale(0, RoundingMode.HALF_UP)
    )

    // Other functions:

    /**
     * Returns the absolute value of the [x], rounding it to the precision specified in the [mc].
     */
    private fun abs(x: CalculationNumber) = CalculationNumber(value = x.value.abs(mc))

    /**
     * Returns the fractional part of the [x] value.
     */
    private fun fraction(x: CalculationNumber) = x.subtract(
        other = floor(x),
        mc = MathContext(0, RoundingMode.HALF_UP)
    )

    /**
     * Returns the hypotenuse with the [x] and [y] legs, rounding it
     * to the precision specified in the [mc].
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun hypot(x: CalculationNumber, y: CalculationNumber) = CalculationNumber(
        value = sqrt(pow(x.value, two, mc).add(pow(y.value, two, mc), mc), mc)
    )

    // Auxiliary functions:

    /**
     * Returns the remainder of dividing the [x] value by the double π number.
     *
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun mod2pi(x: BigDecimal) = x.remainder(doublePi, mc)

    /**
     * Checks whether the values of [x] and [y] are equals.
     * The values of [x] and [y] are equal if the absolute value
     * of the subtraction of [x] and [y] is less than epsilon.
     */
    private fun isEquals(x: BigDecimal, y: BigDecimal): Boolean =
        x.subtract(y, mc).abs(mc) < epsilon

    /**
     * Converts an instance of [BigDecimal] class from unit specified in [angleUnit] to radians.
     *
     * @param angleUnit source measurement unit.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun BigDecimal.toRadians(angleUnit: AngleUnit) = when (angleUnit) {
        AngleUnit.RADIAN -> this
        AngleUnit.DEGREE -> multiply(pi).divide(oneHundredEighty, mc)
        AngleUnit.GRADIAN -> multiply(pi).divide(twoHundred, mc)
    }

    /**
     * Converts an instance of [BigDecimal] class from radians to unit specified in [angleUnit].
     *
     * @param angleUnit target measurement unit.
     * @throws UnsupportedOperationException If the [mc] has unlimited precision.
     */
    private fun BigDecimal.toOtherUnit(angleUnit: AngleUnit) = when (angleUnit) {
        AngleUnit.RADIAN -> this
        AngleUnit.DEGREE -> multiply(oneHundredEighty).divide(pi, mc)
        AngleUnit.GRADIAN -> multiply(twoHundred).divide(pi, mc)
    }
}
