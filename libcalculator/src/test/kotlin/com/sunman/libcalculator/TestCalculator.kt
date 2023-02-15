package com.sunman.libcalculator

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.math.MathContext

internal class TestCalculator {

    private lateinit var calculator: Calculator


    @Before
    fun beforeEach() {
        calculator = Calculator(mc = MathContext.DECIMAL128)
    }

    @Test
    fun testOutputOfNumbers() {
        print("\tTesting the outputs of numbers... ")
        testCalculateExpression(inputExpression = "123456789", expectedResult = "123456789")
        testCalculateExpression(inputExpression = "10 ^ 9", expectedResult = "1000000000")
        testCalculateExpression(inputExpression = "10^10", expectedResult = "1*10^+10")
        testCalculateExpression(inputExpression = "1.1*10^10", expectedResult = "11000000000")
        testCalculateExpression(inputExpression = "10^10+0.1", expectedResult = "10000000000.1")
        testCalculateExpression(
            inputExpression = "111111111111111",
            expectedResult = "111111111111111"
        )
        testCalculateExpression(
            inputExpression = "1111111111111111",
            expectedResult = "1.11111111111111*10^+15"
        )
        testCalculateExpression(
            inputExpression = "1111111111111115",
            expectedResult = "1.11111111111112*10^+15"
        )
        testCalculateExpression(
            inputExpression = "12345678912345678",
            expectedResult = "1.23456789123457*10^+16"
        )
        println("OK")
    }

    @Test
    fun testOperators() {
        println("\tTesting the execution of operators...")

        print("\t\tTesting the execution of the unary plus operator... ")
        testCalculateExpression(inputExpression = "+1", expectedResult = "1")
        testCalculateExpression(inputExpression = "+0", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the unary minus operator... ")
        testCalculateExpression(inputExpression = "-12", expectedResult = "-12")
        testCalculateExpression(inputExpression = "-0", expectedResult = "0")
        testCalculateExpression(inputExpression = "-10000.1", expectedResult = "-10000.1")
        println("OK")

        print("\t\tTesting the execution of the addition operator... ")
        testCalculateExpression(inputExpression = "1+1", expectedResult = "2")
        testCalculateExpression(inputExpression = "1+0", expectedResult = "1")
        testCalculateExpression(inputExpression = "0+0", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the subtraction operator... ")
        testCalculateExpression(inputExpression = "12.1-6.9", expectedResult = "5.2")
        testCalculateExpression(inputExpression = "12.1-0.0", expectedResult = "12.1")
        testCalculateExpression(inputExpression = "0.00000-0", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the multiplication operator... ")
        testCalculateExpression(inputExpression = "6*2", expectedResult = "12")
        testCalculateExpression(inputExpression = "0.0*0.100", expectedResult = "0")
        testCalculateExpression(inputExpression = "0.000*1000000000000.1234", expectedResult = "0")
        testCalculateExpression(inputExpression = "0.0*0.00000000", expectedResult = "0")
        testCalculateExpression(inputExpression = "1.0*0.0001", expectedResult = "0.0001")
        testCalculateExpression(
            inputExpression = "1.0 * 10000000.124",
            expectedResult = "10000000.124"
        )
        testCalculateExpression(inputExpression = "1.0*1.0000000000", expectedResult = "1")
        println("OK")

        print("\t\tTesting the execution of the division operator... ")
        testCalculateExpression(inputExpression = "6/3", expectedResult = "2")
        testCalculateExpression(inputExpression = "1.2 / 3", expectedResult = "0.4")
        testCalculateExpression(inputExpression = "1.2/0.4", expectedResult = "3")
        testCalculateExpression(inputExpression = "1/5", expectedResult = "0.2")
        testCalculateExpression(inputExpression = "1/3", expectedResult = "0.333333333333333")
        testCalculateExpression(inputExpression = "0/1", expectedResult = "0")
        testCalculateExpression(inputExpression = "0/1000.1", expectedResult = "0")
        testCalculateExpression(inputExpression = "1/0", expectedResult = "Division by zero")
        testCalculateExpression(inputExpression = "0/0", expectedResult = "Division undefined")
        println("OK")

        print("\t\tTesting the execution of the exponentiation operator... ")
        testCalculateExpression(inputExpression = "(-2)^-2", expectedResult = "0.25")
        testCalculateExpression(inputExpression = "(-2)^-3", expectedResult = "-0.125")
        testCalculateExpression(
            inputExpression = "(-2)^-0.5",
            expectedResult = "Illegal x^y for x < 0 and non-integer y: x = -2; y = -0.5"

        )
        testCalculateExpression(inputExpression = "(-2)^0.0", expectedResult = "1")
        testCalculateExpression(inputExpression = "(-2)^2", expectedResult = "4")
        testCalculateExpression(inputExpression = "(-2)^3", expectedResult = "-8")
        testCalculateExpression(
            inputExpression = "0^-1.2",
            expectedResult = "Illegal x^y for x = 0 and y < 0: y = -1.2"
        )
        testCalculateExpression(
            inputExpression = "0.0^-2",
            expectedResult = "Illegal x^y for x = 0 and y < 0: y = -2"
        )
        testCalculateExpression(inputExpression = "0.0^0.000", expectedResult = "1")
        testCalculateExpression(inputExpression = "0.0^0.5", expectedResult = "0")
        testCalculateExpression(inputExpression = "0.0^1", expectedResult = "0")
        testCalculateExpression(inputExpression = "1^1000", expectedResult = "1")
        testCalculateExpression(inputExpression = "1.1^2", expectedResult = "1.21")
        testCalculateExpression(inputExpression = "1.21^0.5", expectedResult = "1.1")
        testCalculateExpression(inputExpression = "2^-2", expectedResult = "0.25")
        testCalculateExpression(inputExpression = "2^0", expectedResult = "1")
        testCalculateExpression(inputExpression = "2 ^ 0.5", expectedResult = "1.4142135623731")
        testCalculateExpression(inputExpression = "2^2", expectedResult = "4")
        testCalculateExpression(inputExpression = "2^10000000000000", expectedResult = "Overflow")
        testCalculateExpression(
            inputExpression = "2^-100000000000000000",
            expectedResult = "Overflow"
        )
        println("OK")

        print("\t\tTesting the execution of the factorial operator... ")
        testCalculateExpression(inputExpression = "(-0.5)!", expectedResult = "1.77245385090552")
        testCalculateExpression(
            inputExpression = "(-1)!",
            expectedResult = "Illegal x! for x < 0, where x is integer: x = -1"
        )
        testCalculateExpression(inputExpression = "0!", expectedResult = "1")
        testCalculateExpression(inputExpression = "0.5!", expectedResult = "0.886226925452758")
        testCalculateExpression(inputExpression = "1!", expectedResult = "1")
        testCalculateExpression(inputExpression = "10!", expectedResult = "3628800")
        println("OK")

        print("\t\tTesting the execution of the percent operator... ")
        testCalculateExpression(inputExpression = "0%", expectedResult = "0")
        testCalculateExpression(inputExpression = "1%", expectedResult = "0.01")
        testCalculateExpression(inputExpression = "100%", expectedResult = "1")
        println("OK")

        println("\tTesting the execution of operators... OK")
    }

    @Test
    fun testPowerFunctions() {
        println("\tTesting the execution of power functions...")

        print("\t\tTesting the execution of the sqrt function... ")
        testCalculateExpression(
            inputExpression = "$SQRT_CHAR(-9)",
            expectedResult = "Illegal $SQRT(x) for x < 0: x = -9"
        )
        testCalculateExpression(inputExpression = "$SQRT_CHAR(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SQRT_CHAR(1)", expectedResult = "1")
        testCalculateExpression(
            inputExpression = "$SQRT_CHAR(2)",
            expectedResult = "1.4142135623731"
        )
        testCalculateExpression(inputExpression = "$SQRT_CHAR(9)", expectedResult = "3")
        println("OK")

        print("\t\tTesting the execution of the cbrt function... ")
        testCalculateExpression(inputExpression = "$CBRT(-8)", expectedResult = "-2")
        testCalculateExpression(inputExpression = "$CBRT(-2)", expectedResult = "-1.25992104989487")
        testCalculateExpression(inputExpression = "$CBRT(-1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$CBRT(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$CBRT(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$CBRT(2)", expectedResult = "1.25992104989487")
        testCalculateExpression(inputExpression = "$CBRT(8)", expectedResult = "2")
        println("OK")

        print("\t\tTesting the execution of the root function... ")
        testCalculateExpression(
            inputExpression = "$ROOT(-2, 3)",
            expectedResult = "-1.25992104989487"
        )
        testCalculateExpression(
            inputExpression = "$ROOT(-2, 0)",
            expectedResult = "Illegal $ROOT(x, n) for x < 0 and even n: x = -2; n = 0"
        )
        testCalculateExpression(
            inputExpression = "$ROOT(-2, 2)",
            expectedResult = "Illegal $ROOT(x, n) for x < 0 and even n: x = -2; n = 2"
        )
        testCalculateExpression(
            inputExpression = "$ROOT(-2, 0.5)",
            expectedResult = "Illegal $ROOT(x, n) for x < 0 and non-integer n: x = -2; n = 0.5"
        )
        testCalculateExpression(
            inputExpression = "$ROOT(0, -2)",
            expectedResult = "Illegal $ROOT(x, n) for x = 0 and n < 0: n = -2"
        )
        testCalculateExpression(inputExpression = "$ROOT(0, 0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$ROOT(0, 2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$ROOT(0, 2.3)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$ROOT(2, -2)",
            expectedResult = "0.707106781186548"
        )
        testCalculateExpression(
            inputExpression = "$ROOT(2, 0)",
            expectedResult = "Illegal $ROOT(x, n) for x > 0 and n = 0: x = 2"
        )
        testCalculateExpression(inputExpression = "$ROOT(2,2)", expectedResult = "1.4142135623731")
        println("OK")

        print("\t\tTesting the execution of the exp function... ")
        testCalculateExpression(
            inputExpression = "$EXP(-10000000000000000000000)",
            expectedResult = "Overflow"
        )
        testCalculateExpression(inputExpression = "$EXP(-1)", expectedResult = "0.367879441171442")
        testCalculateExpression(inputExpression = "$EXP(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$EXP(1)", expectedResult = "2.71828182845905")
        testCalculateExpression(
            inputExpression = "$EXP(10000000000000000)",
            expectedResult = "Overflow"
        )
        println("OK")

        println("\tTesting the execution of power functions... OK")
    }

    @Test
    fun testLogarithmicFunctions() {
        println("\tTesting the execution of logarithmic functions...")

        print("\t\tTesting the execution of the ln function... ")
        testCalculateExpression(
            inputExpression = "$LN(-1)",
            expectedResult = "Illegal $LN(x) for x <= 0: x = -1"
        )
        testCalculateExpression(
            inputExpression = "$LN(0)",
            expectedResult = "Illegal $LN(x) for x <= 0: x = 0"
        )
        testCalculateExpression(inputExpression = "$LN(1 / $E^1000)", expectedResult = "-1000")
        testCalculateExpression(inputExpression = "$LN(1/$E)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$LN(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$LN($E)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$LN($E^1000)", expectedResult = "1000")
        println("OK")

        print("\t\tTesting the execution of the log function... ")
        testCalculateExpression(
            inputExpression = "$LOG(-1, -1)",
            expectedResult = "Illegal $LOG(b, x) for b <= 0: b = -1"
        )
        testCalculateExpression(
            inputExpression = "$LOG(0, -1)",
            expectedResult = "Illegal $LOG(b, x) for b <= 0: b = 0"
        )
        testCalculateExpression(
            inputExpression = "$LOG(1, -1)",
            expectedResult = "Illegal $LOG(b, x) for b = 1"
        )
        testCalculateExpression(
            inputExpression = "$LOG(3, -1)",
            expectedResult = "Illegal $LOG(b, x) for x <= 0: x = -1"
        )
        testCalculateExpression(
            inputExpression = "$LOG(3, 0)",
            expectedResult = "Illegal $LOG(b, x) for x <= 0: x = 0"
        )
        testCalculateExpression(
            inputExpression = "$LOG(3, 4)",
            expectedResult = "1.26185950714291"
        )
        println("OK")

        print("\t\tTesting the execution of the log2 function... ")
        testCalculateExpression(
            inputExpression = "$LOG2(-1)",
            expectedResult = "Illegal $LOG2(x) for x <= 0: x = -1"
        )
        testCalculateExpression(
            inputExpression = "$LOG2(0)",
            expectedResult = "Illegal $LOG2(x) for x <= 0: x = 0"
        )
        testCalculateExpression(inputExpression = "$LOG2(1/2^1000)", expectedResult = "-1000")
        testCalculateExpression(inputExpression = "$LOG2(1/2)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$LOG2(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$LOG2(2)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$LOG2(2^1000)", expectedResult = "1000")
        println("OK")

        print("\t\tTesting the execution of the lg function... ")
        testCalculateExpression(
            inputExpression = "$LG(-1)",
            expectedResult = "Illegal $LG(x) for x <= 0: x = -1"
        )
        testCalculateExpression(
            inputExpression = "$LG(0)",
            expectedResult = "Illegal $LG(x) for x <= 0: x = 0"
        )
        testCalculateExpression(inputExpression = "$LG(1/10^1000)", expectedResult = "-1000")
        testCalculateExpression(inputExpression = "$LG(1/10)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$LG(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$LG(10)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$LG(10^1000)", expectedResult = "1000")
        println("OK")

        println("\tTesting the execution of logarithmic functions... OK")
    }

    @Test
    fun testTrigonometricFunctions() {
        println("\tTesting the execution of trigonometric functions...")

        print("\t\tTesting the execution of the sin function... ")
        testCalculateExpression(inputExpression = "$SIN(-1000001$PI)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SIN(-1000001$PI/2)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$SIN(-2$PI)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SIN(-$PI)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SIN(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SIN($PI/2)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$SIN($PI)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SIN(3$PI/2)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$SIN(2$PI)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SIN(1000001$PI/2)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$SIN(1000001$PI)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the cos function... ")
        testCalculateExpression(inputExpression = "$COS(-1000001$PI)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$COS(-1000001$PI/2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$COS(-2$PI)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$COS(-$PI)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$COS(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$COS($PI/2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$COS($PI)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$COS(3$PI/2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$COS(2$PI)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$COS(1000001$PI/2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$COS(1000001$PI)", expectedResult = "-1")
        println("OK")

        print("\t\tTesting the execution of the tan function... ")
        testCalculateExpression(inputExpression = "$TAN(-1000001$PI)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$TAN(-1000001$PI/2)",
            expectedResult = "Illegal $TAN(x) for x = $PI * k / 2, where k is odd: " +
                    "x = -1570797.89759122"
        )
        testCalculateExpression(inputExpression = "$TAN(-2$PI)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$TAN(-$PI)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$TAN(0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$TAN($PI/2)",
            expectedResult = "Illegal $TAN(x) for x = $PI * k / 2, where k is odd: " +
                    "x = 1.5707963267949"
        )
        testCalculateExpression(inputExpression = "$TAN($PI)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$TAN(3$PI/2)",
            expectedResult = "Illegal $TAN(x) for x = $PI * k / 2, where k is odd: " +
                    "x = 4.71238898038469"
        )
        testCalculateExpression(inputExpression = "$TAN(2$PI)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$TAN(1000001$PI/2)",
            expectedResult = "Illegal $TAN(x) for x = $PI * k / 2, where k is odd: " +
                    "x = 1570797.89759122"
        )
        testCalculateExpression(inputExpression = "$TAN(1000001$PI)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the cot function... ")
        testCalculateExpression(
            inputExpression = "$COT(-1000001$PI)",
            expectedResult = "Illegal $COT(x) for x = $PI * k, where k is an integer: " +
                    "x = -3141595.79518245"
        )
        testCalculateExpression(inputExpression = "$COT(-1000001$PI/2)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$COT(-2$PI)",
            expectedResult = "Illegal $COT(x) for x = $PI * k, where k is an integer: " +
                    "x = -6.28318530717959"
        )
        testCalculateExpression(
            inputExpression = "$COT(0)",
            expectedResult = "Illegal $COT(x) for x = $PI * k, where k is an integer: x = 0"
        )
        testCalculateExpression(inputExpression = "$COT($PI/2)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$COT($PI)",
            expectedResult = "Illegal $COT(x) for x = $PI * k, where k is an integer: " +
                    "x = 3.14159265358979"
        )
        testCalculateExpression(inputExpression = "$COT(3$PI/2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$COT(1000001$PI/2)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the sec function... ")
        testCalculateExpression(inputExpression = "$SEC(-1000001$PI)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "sec(-1000001$PI/2)",
            expectedResult = "Illegal sec(x) for x = $PI * k / 2, where k is odd: " +
                    "x = -1570797.89759122"
        )
        testCalculateExpression(inputExpression = "sec(-2$PI)", expectedResult = "1")
        testCalculateExpression(inputExpression = "sec(0)", expectedResult = "1")
        testCalculateExpression(
            inputExpression = "sec($PI/2)",
            expectedResult = "Illegal sec(x) for x = $PI * k / 2, where k is odd: " +
                    "x = 1.5707963267949"
        )
        testCalculateExpression(inputExpression = "sec($PI)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "sec(3$PI/2)",
            expectedResult = "Illegal sec(x) for x = $PI * k / 2, where k is odd: " +
                    "x = 4.71238898038469"
        )
        testCalculateExpression(
            inputExpression = "sec(1000001$PI/2)",
            expectedResult = "Illegal sec(x) for x = $PI * k / 2, where k is odd: " +
                    "x = 1570797.89759122"
        )
        println("OK")

        print("\t\tTesting the execution of the cosec function... ")
        testCalculateExpression(
            inputExpression = "$COSEC(-1000001$PI)",
            expectedResult = "Illegal $COSEC(x) for x = $PI * k, where k is an integer: " +
                    "x = -3141595.79518245"
        )
        testCalculateExpression(inputExpression = "$COSEC(-1000001$PI/2)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "$COSEC(-2$PI)",
            expectedResult = "Illegal $COSEC(x) for x = $PI * k, where k is an integer: " +
                    "x = -6.28318530717959"
        )
        testCalculateExpression(
            inputExpression = "$COSEC(0)",
            expectedResult = "Illegal $COSEC(x) for x = $PI * k, where k is an integer: x = 0"
        )
        testCalculateExpression(inputExpression = "$COSEC($PI/2)", expectedResult = "1")
        testCalculateExpression(
            inputExpression = "$COSEC($PI)",
            expectedResult = "Illegal $COSEC(x) for x = $PI * k, where k is an integer: " +
                    "x = 3.14159265358979"
        )
        testCalculateExpression(inputExpression = "$COSEC(3$PI/2)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$COSEC(1000001$PI/2)", expectedResult = "1")
        println("OK")

        println("\tTesting the execution of trigonometric functions... OK")
    }

    @Test
    fun testTrigonometricFunctionsWithOtherAngleUnits() {
        println("\tTesting the execution of trigonometric functions with other angle units...")

        print("\t\tTesting the execution of the sin function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$SIN(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SIN(90)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$SIN(180)", expectedResult = "0")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$SIN(300)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$SIN(400)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the cos function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$COS(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$COS(90)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$COS(180)", expectedResult = "-1")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$COS(300)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$COS(400)", expectedResult = "1")
        println("OK")

        print("\t\tTesting the execution of the tan function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$TAN(0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$TAN(90)",
            expectedResult = "Illegal $TAN(x) for x = 90° * k, where k is odd: x = 90"
        )
        testCalculateExpression(inputExpression = "$TAN(180)", expectedResult = "0")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(
            inputExpression = "$TAN(300)",
            expectedResult = "Illegal $TAN(x) for x = 100g * k, where k is odd: x = 300"
        )
        testCalculateExpression(inputExpression = "$TAN(400)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the cot function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(
            inputExpression = "$COT(0)",
            expectedResult = "Illegal $COT(x) for x = 180° * k, where k is an integer: x = 0"
        )
        testCalculateExpression(inputExpression = "$COT(90)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$COT(180)",
            expectedResult = "Illegal $COT(x) for x = 180° * k, where k is an integer: x = 180"
        )
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$COT(300)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$COT(400)",
            expectedResult = "Illegal $COT(x) for x = 200g * k, where k is an integer: x = 400"
        )
        println("OK")

        print("\t\tTesting the execution of the sec function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$SEC(0)", expectedResult = "1")
        testCalculateExpression(
            inputExpression = "$SEC(90)",
            expectedResult = "Illegal $SEC(x) for x = 90° * k, where k is odd: x = 90"
        )
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$SEC(200)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "$SEC(300)",
            expectedResult = "Illegal $SEC(x) for x = 100g * k, where k is odd: x = 300"
        )
        println("OK")

        print("\t\tTesting the execution of the cosec function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(
            inputExpression = "$COSEC(0)",
            expectedResult = "Illegal $COSEC(x) for x = 180° * k, where k is an integer: x = 0"
        )
        testCalculateExpression(inputExpression = "$COSEC(90)", expectedResult = "1")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(
            inputExpression = "$COSEC(200)",
            expectedResult = "Illegal $COSEC(x) for x = 200g * k, where k is an integer: x = 200"
        )
        testCalculateExpression(inputExpression = "$COSEC(300)", expectedResult = "-1")
        println("OK")

        println("\tTesting the execution of trigonometric functions with other angle units... OK")
    }

    @Test
    fun testInverseTrigonometricFunctions() {
        println("\tTesting the execution of inverse trigonometric functions...")

        print("\t\tTesting the execution of the asin function... ")
        testCalculateExpression(
            inputExpression = "$ASIN(-2)",
            expectedResult = "Illegal $ASIN(x) for x < -1: x = -2"
        )
        testCalculateExpression(inputExpression = "$ASIN(-1)", expectedResult = "-1.5707963267949")
        testCalculateExpression(inputExpression = "$ASIN(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$ASIN(1)", expectedResult = "1.5707963267949")
        testCalculateExpression(
            inputExpression = "$ASIN(2)",
            expectedResult = "Illegal $ASIN(x) for x > 1: x = 2"
        )
        println("OK")

        print("\t\tTesting the execution of the acos function... ")
        testCalculateExpression(
            inputExpression = "$ACOS(-2)",
            expectedResult = "Illegal $ACOS(x) for x < -1: x = -2"
        )
        testCalculateExpression(inputExpression = "$ACOS(-1)", expectedResult = "3.14159265358979")
        testCalculateExpression(inputExpression = "$ACOS(0)", expectedResult = "1.5707963267949")
        testCalculateExpression(inputExpression = "$ACOS(1)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$ACOS(2)",
            expectedResult = "Illegal $ACOS(x) for x > 1: x = 2"
        )
        println("OK")

        print("\t\tTesting the execution of the atan function... ")
        testCalculateExpression(
            inputExpression = "$ATAN(-10^50)",
            expectedResult = "-1.5707963267949"
        )
        testCalculateExpression(
            inputExpression = "$ATAN(-1)",
            expectedResult = "-0.785398163397448"
        )
        testCalculateExpression(inputExpression = "$ATAN(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$ATAN(1)", expectedResult = "0.785398163397448")
        testCalculateExpression(
            inputExpression = "$ATAN(10^50)",
            expectedResult = "1.5707963267949"
        )
        println("OK")

        print("\t\tTesting the execution of the acot function... ")
        testCalculateExpression(
            inputExpression = "$ACOT(-10^50)",
            expectedResult = "3.14159265358979"
        )
        testCalculateExpression(inputExpression = "$ACOT(-1)", expectedResult = "2.35619449019234")
        testCalculateExpression(inputExpression = "$ACOT(0)", expectedResult = "1.5707963267949")
        testCalculateExpression(inputExpression = "$ACOT(1)", expectedResult = "0.785398163397448")
        testCalculateExpression(inputExpression = "$ACOT(10^50)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the asec function... ")
        testCalculateExpression(inputExpression = "$ASEC(-2)", expectedResult = "2.0943951023932")
        testCalculateExpression(inputExpression = "$ASEC(-1)", expectedResult = "3.14159265358979")
        testCalculateExpression(
            inputExpression = "$ASEC(0)",
            expectedResult = "Illegal $ASEC(x) for x > -1 and x < 1: x = 0"
        )
        testCalculateExpression(inputExpression = "$ASEC(1)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$ASEC(2)",
            expectedResult = "1.0471975511966"
        )
        println("OK")

        print("\t\tTesting the execution of the acosec function... ")
        testCalculateExpression(
            inputExpression = "$ACOSEC(-2)",
            expectedResult = "-0.523598775598299"
        )
        testCalculateExpression(
            inputExpression = "$ACOSEC(-1)",
            expectedResult = "-1.5707963267949"
        )
        testCalculateExpression(
            inputExpression = "$ACOSEC(0)",
            expectedResult = "Illegal $ACOSEC(x) for x > -1 and x < 1: x = 0"
        )
        testCalculateExpression(
            inputExpression = "$ACOSEC(1)",
            expectedResult = "1.5707963267949"
        )
        testCalculateExpression(
            inputExpression = "$ACOSEC(2)",
            expectedResult = "0.523598775598299"
        )
        println("OK")

        println("\tTesting the execution of inverse trigonometric functions... OK")
    }

    @Test
    fun testInverseTrigonometricFunctionsWithOtherAngleUnits() {
        println(
            "\tTesting the execution of inverse trigonometric functions with " +
                    "other angle units..."
        )

        print("\t\tTesting the execution of the $ASIN function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$ASIN(-1)", expectedResult = "-90")
        testCalculateExpression(inputExpression = "$ASIN(0)", expectedResult = "0")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$ASIN(1)", expectedResult = "100")
        println("OK")

        print("\t\tTesting the execution of the acos function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$ACOS(-1)", expectedResult = "180")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$ACOS(0)", expectedResult = "100")
        testCalculateExpression(inputExpression = "$ACOS(1)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the atan function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$ATAN(-1)", expectedResult = "-45")
        testCalculateExpression(inputExpression = "$ATAN(0)", expectedResult = "0")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$ATAN(1)", expectedResult = "50")
        println("OK")

        print("\t\tTesting the execution of the acot function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$ACOT(-1)", expectedResult = "135")
        testCalculateExpression(inputExpression = "$ACOT(0)", expectedResult = "90")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$ACOT(1)", expectedResult = "50")
        println("OK")

        print("\t\tTesting the execution of the asec function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$ASEC(-2)", expectedResult = "120")
        testCalculateExpression(inputExpression = "$ASEC(-1)", expectedResult = "180")
        testCalculateExpression(
            inputExpression = "$ASEC(0)",
            expectedResult = "Illegal $ASEC(x) for x > -1 and x < 1: x = 0"
        )
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$ASEC(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$ASEC(2)", expectedResult = "66.6666666666667")
        println("OK")

        print("\t\tTesting the execution of the acosec function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "$ACOSEC(-2)", expectedResult = "-30")
        testCalculateExpression(inputExpression = "$ACOSEC(-1)", expectedResult = "-90")
        testCalculateExpression(
            inputExpression = "$ACOSEC(0)",
            expectedResult = "Illegal $ACOSEC(x) for x > -1 and x < 1: x = 0"
        )
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "$ACOSEC(1)", expectedResult = "100")
        testCalculateExpression(inputExpression = "$ACOSEC(2)", expectedResult = "33.3333333333333")
        println("OK")

        println(
            "\tTesting the execution of inverse trigonometric functions with other angle units... OK"
        )
    }

    @Test
    fun testHyperbolicFunctions() {
        println("\tTesting the execution of hyperbolic functions...")

        print("\t\tTesting the execution of the sinh function... ")
        testCalculateExpression(inputExpression = "$SINH(-10)", expectedResult = "-11013.2328747034")
        testCalculateExpression(inputExpression = "$SINH(-1)", expectedResult = "-1.1752011936438")
        testCalculateExpression(inputExpression = "$SINH(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$SINH(1)", expectedResult = "1.1752011936438")
        testCalculateExpression(inputExpression = "$SINH(10)", expectedResult = "11013.2328747034")
        println("OK")

        print("\t\tTesting the execution of the cosh function... ")
        testCalculateExpression(inputExpression = "$COSH(-10)", expectedResult = "11013.2329201033")
        testCalculateExpression(inputExpression = "$COSH(-1)", expectedResult = "1.54308063481524")
        testCalculateExpression(inputExpression = "$COSH(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$COSH(1)", expectedResult = "1.54308063481524")
        testCalculateExpression(inputExpression = "$COSH(10)", expectedResult = "11013.2329201033")
        println("OK")

        print("\t\tTesting the execution of the tanh function... ")
        testCalculateExpression(inputExpression = "$TANH(-100)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$TANH(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$TANH(100)", expectedResult = "1")
        println("OK")

        print("\t\tTesting the execution of the coth function... ")
        testCalculateExpression(inputExpression = "$COTH(-100)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "$COTH(0)",
            expectedResult = "Illegal $COTH(x) for x = 0"
        )
        testCalculateExpression(inputExpression = "$COTH(100)", expectedResult = "1")
        println("OK")

        print("\t\tTesting the execution of the sech function... ")
        testCalculateExpression(
            inputExpression = "$SECH(-10)",
            expectedResult = "0.000090799859338"
        )
        testCalculateExpression(inputExpression = "$SECH(-1)", expectedResult = "0.648054273663885")
        testCalculateExpression(inputExpression = "$SECH(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$SECH(1)", expectedResult = "0.648054273663885")
        testCalculateExpression(inputExpression = "$SECH(10)", expectedResult = "0.000090799859338")
        println("OK")

        print("\t\tTesting the execution of the csch function... ")
        testCalculateExpression(inputExpression = "$CSCH(-10)", expectedResult = "-0.000090799859712")
        testCalculateExpression(inputExpression = "$CSCH(-1)", expectedResult = "-0.850918128239322")
        testCalculateExpression(
            inputExpression = "$CSCH(0)",
            expectedResult = "Illegal $CSCH(x) for x = 0"
        )
        testCalculateExpression(inputExpression = "$CSCH(1)", expectedResult = "0.850918128239322")
        testCalculateExpression(inputExpression = "$CSCH(10)", expectedResult = "0.000090799859712")
        println("OK")

        println("\tTesting the execution of hyperbolic functions... OK")
    }

    @Test
    fun testInverseHyperbolicFunctions() {
        println("\tTesting the execution of inverse hyperbolic functions...")

        print("\t\tTesting the execution of the asinh function... ")
        testCalculateExpression(
            inputExpression = "$ASINH(-100)",
            expectedResult = "-5.29834236561059"
        )
        testCalculateExpression(inputExpression = "$ASINH(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$ASINH(1)", expectedResult = "0.881373587019543")
        println("OK")

        print("\t\tTesting the execution of the acosh function... ")
        testCalculateExpression(
            inputExpression = "$ACOSH(0)",
            expectedResult = "Illegal $ACOSH(x) for x < 1: x = 0"
        )
        testCalculateExpression(inputExpression = "$ACOSH(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$ACOSH(100)", expectedResult = "5.29829236561048")
        println("OK")

        print("\t\tTesting the execution of the atanh function... ")
        testCalculateExpression(
            inputExpression = "$ATANH(-2)",
            expectedResult = "Illegal $ATANH(x) for x <= -1: x = -2"
        )
        testCalculateExpression(
            inputExpression = "$ATANH(-1)",
            expectedResult = "Illegal $ATANH(x) for x <= -1: x = -1"
        )
        testCalculateExpression(
            inputExpression = "$ATANH(-0.5)",
            expectedResult = "-0.549306144334055"
        )
        testCalculateExpression(inputExpression = "$ATANH(0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$ATANH(0.5)",
            expectedResult = "0.549306144334055"
        )
        testCalculateExpression(
            inputExpression = "$ATANH(1)",
            expectedResult = "Illegal $ATANH(x) for x >= 1: x = 1"
        )
        testCalculateExpression(
            inputExpression = "$ATANH(2)",
            expectedResult = "Illegal $ATANH(x) for x >= 1: x = 2"
        )
        println("OK")

        print("\t\tTesting the execution of the acoth function... ")
        testCalculateExpression(
            inputExpression = "$ACOTH(-2)",
            expectedResult = "-0.549306144334055"
        )
        testCalculateExpression(
            inputExpression = "$ACOTH(-1)",
            expectedResult = "Illegal $ACOTH(x) for x >= -1 or x <= 1: x = -1"
        )
        testCalculateExpression(
            inputExpression = "$ACOTH(0)",
            expectedResult = "Illegal $ACOTH(x) for x >= -1 or x <= 1: x = 0"
        )
        testCalculateExpression(
            inputExpression = "$ACOTH(1)",
            expectedResult = "Illegal $ACOTH(x) for x >= -1 or x <= 1: x = 1"
        )
        testCalculateExpression(
            inputExpression = "$ACOTH(2)",
            expectedResult = "0.549306144334055"
        )
        println("OK")

        print("\t\tTesting the execution of the asech function... ")
        testCalculateExpression(
            inputExpression = "$ASECH(0)",
            expectedResult = "Illegal $ASECH(x) for x <= 0: x = 0"
        )
        testCalculateExpression(
            inputExpression = "$ASECH(0.5)",
            expectedResult = "1.31695789692482"
        )
        testCalculateExpression(inputExpression = "$ASECH(1)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$ASECH(2)",
            expectedResult = "Illegal $ASECH(x) for x > 1: x = 2"
        )
        println("OK")

        print("\t\tTesting the execution of the acsch function... ")
        testCalculateExpression(
            inputExpression = "$ACSCH(-2)",
            expectedResult = "-0.481211825059603"
        )
        testCalculateExpression(
            inputExpression = "$ACSCH(-1)",
            expectedResult = "-0.881373587019543"
        )
        testCalculateExpression(
            inputExpression = "$ACSCH(-0.5)",
            expectedResult = "-1.44363547517881"
        )
        testCalculateExpression(
            inputExpression = "$ACSCH(0)",
            expectedResult = "Illegal $ACSCH(x) for x = 0"
        )
        testCalculateExpression(
            inputExpression = "$ACSCH(0.5)",
            expectedResult = "1.44363547517881"
        )
        testCalculateExpression(
            inputExpression = "$ACSCH(1)",
            expectedResult = "0.881373587019543"
        )
        testCalculateExpression(
            inputExpression = "$ACSCH(2)",
            expectedResult = "0.481211825059603"
        )
        println("OK")

        println("\tTesting the execution of inverse hyperbolic functions... OK")
    }

    @Test
    fun testRoundingFunctions() {
        println("\tTesting the execution of rounding functions...")

        print("\t\tTesting the execution of the ceil function... ")
        testCalculateExpression(inputExpression = "$CEIL(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$CEIL(1.1)", expectedResult = "2")
        testCalculateExpression(inputExpression = "$CEIL(1.5)", expectedResult = "2")
        testCalculateExpression(inputExpression = "$CEIL(1.7)", expectedResult = "2")
        testCalculateExpression(inputExpression = "$CEIL(-1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$CEIL(-1.1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$CEIL(-1.5)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$CEIL(-1.7)", expectedResult = "-1")
        println("OK")

        print("\t\tTesting the execution of the floor function... ")
        testCalculateExpression(inputExpression = "$FLOOR(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$FLOOR(1.1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$FLOOR(1.5)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$FLOOR(1.7)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$FLOOR(-1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$FLOOR(-1.1)", expectedResult = "-2")
        testCalculateExpression(inputExpression = "$FLOOR(-1.5)", expectedResult = "-2")
        testCalculateExpression(inputExpression = "$FLOOR(-1.7)", expectedResult = "-2")
        println("OK")

        print("\t\tTesting the execution of the round function... ")
        testCalculateExpression(inputExpression = "$ROUND(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$ROUND(1.1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "$ROUND(1.5)", expectedResult = "2")
        testCalculateExpression(inputExpression = "$ROUND(1.7)", expectedResult = "2")
        testCalculateExpression(inputExpression = "$ROUND(-1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$ROUND(-1.1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "$ROUND(-1.5)", expectedResult = "-2")
        testCalculateExpression(inputExpression = "$ROUND(-1.7)", expectedResult = "-2")
        println("OK")

        println("\tTesting the execution of rounding functions... OK")
    }

    @Test
    fun testOtherFunction() {
        println("\tTesting the execution of other functions...")

        print("\t\tTesting the execution of the abs function... ")
        testCalculateExpression(inputExpression = "$ABS(-10^100)", expectedResult = "1*10^+100")
        testCalculateExpression(inputExpression = "$ABS(-12)", expectedResult = "12")
        testCalculateExpression(inputExpression = "$ABS(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$ABS(12)", expectedResult = "12")
        testCalculateExpression(inputExpression = "$ABS(10^100)", expectedResult = "1*10^+100")
        println("OK")

        print("\t\tTesting the execution of the fraction function... ")
        testCalculateExpression(inputExpression = "$FRACTION(1.1)", expectedResult = "0.1")
        testCalculateExpression(
            inputExpression = "$FRACTION($PI)",
            expectedResult = "0.141592653589793"
        )
        testCalculateExpression(inputExpression = "$FRACTION(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "$FRACTION(0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$FRACTION($E)",
            expectedResult = "0.718281828459045"
        )
        println("OK")

        print("\t\tTesting the execution of the hypot function... ")
        testCalculateExpression(inputExpression = "$HYPOT(0,0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "$HYPOT(-1,-1)",
            expectedResult = "1.4142135623731"
        )
        testCalculateExpression(inputExpression = "$HYPOT(3,4)", expectedResult = "5")
        testCalculateExpression(inputExpression = "$HYPOT(12,5)", expectedResult = "13")
        testCalculateExpression(
            inputExpression = "$HYPOT(4,4)",
            expectedResult = "5.65685424949238"
        )
        println("OK")

        println("\tTesting the execution of other functions... OK")
    }

    @Test
    fun testFunctionsWithErrors() {
        println("\tTesting the execution of functions with errors...")

        print("\t\tTesting the execution of functions with a lack or excess of arguments... ")
        testCalculateExpression(
            inputExpression = "$SIN()",
            expectedResult = "The function '$SIN' expects 1 arguments, but given 0"
        )
        testCalculateExpression(
            inputExpression = "$SIN(1,2)",
            expectedResult = "The function '$SIN' expects 1 arguments, but given 2"
        )
        println("OK")

        print("\t\tTesting the execution of an unknown function... ")
        testCalculateExpression(
            inputExpression = "procedure()",
            expectedResult = "Unknown function 'procedure'"
        )
        println("OK")

        println("\tTesting the execution of functions with errors... OK")
    }

    @Test
    fun testSpecialCases() {
        println("\tTesting special cases of the Calculator class...")

        print("\t\tTesting the execution of empty expressions... ")
        testCalculateExpression(inputExpression = "", expectedResult = "")
        println("OK")

        println("\tTesting methods of the Calculator class... OK")
    }

    @Test
    fun testComplexExpressions() {
        println("\tTesting the execution of complex expressions... ")

        testCalculateExpression(inputExpression = "10!(23)(24)", expectedResult = "2003097600")
        testCalculateExpression(inputExpression = "9+1-2", expectedResult = "8")
        testCalculateExpression(inputExpression = "1.2*8/2.1", expectedResult = "4.57142857142857")
        testCalculateExpression(inputExpression = "+-+++1.2", expectedResult = "-1.2")
        testCalculateExpression(
            inputExpression = "2^3^9",
            expectedResult = "1.49074943748314*10^+5925"
        )
        testCalculateExpression(inputExpression = "2!!%!", expectedResult = "0.988844203263913")
        testCalculateExpression(inputExpression = "--+1.0+-+-+-++-1.3", expectedResult = "2.3")
        testCalculateExpression(
            inputExpression = "-7^+-2!!^-7-+-4%*--4(--1.0*+-1.0)/(-7^+-2!)",
            expectedResult = "6.85508744956883"
        )
        testCalculateExpression(inputExpression = "(1+2)*3", expectedResult = "9")
        testCalculateExpression(inputExpression = "(1-2)e", expectedResult = "-2.71828182845905")
        testCalculateExpression(
            inputExpression = "(((-7)^(+2)!!)^-7-+-4%)*(--4(--1.0*+-1.0)/(-7^+-2!))",
            expectedResult = "7.84000000028899"
        )
        testCalculateExpression(
            inputExpression = "$LOG(27^2,8!)8+$SIN(1+2)",
            expectedResult = "13.0114207345659"
        )

        println("\tTesting the execution of complex expressions... OK")
    }

    private fun testCalculateExpression(inputExpression: String, expectedResult: String) {
        val actualResult =
            when (val result = calculator.calculateExpression(inputExpression)) {
                is CalculationNothing -> ""
                is CalculationNumber -> result.toString(precision = 15, maxTrailingZeros = 9)
                is CalculationError -> result.error.localizedMessage
                else -> result.toString()
            }

        assertEquals(
            /* message = */ "The actual result is not equal to the expected one",
            /* expected = */ expectedResult, /* actual = */ actualResult
        )
    }


    companion object {
        @BeforeClass
        @JvmStatic
        fun start() {
            println(
                """
                |Testing methods of the Calculator and CalculationNumber classes,
                |as well as functions from the CalculatorContext class...
                """.trimMargin().trimIndent()
            )
        }

        @AfterClass
        @JvmStatic
        fun finish() {
            println(
                """
                |Testing methods of the Calculator and CalculationNumber classes,
                |as well as functions from the CalculatorContext class... OK
                """.trimMargin().trimIndent()
            )
            println()
        }
    }
}
