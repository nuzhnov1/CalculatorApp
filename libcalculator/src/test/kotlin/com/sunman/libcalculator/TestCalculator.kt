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
        testCalculateExpression(inputExpression = "6÷3", expectedResult = "2")
        testCalculateExpression(inputExpression = "1.2 ÷ 3", expectedResult = "0.4")
        testCalculateExpression(inputExpression = "1.2÷0.4", expectedResult = "3")
        testCalculateExpression(inputExpression = "1÷5", expectedResult = "0.2")
        testCalculateExpression(inputExpression = "1÷3", expectedResult = "0.333333333333333")
        testCalculateExpression(inputExpression = "0÷1", expectedResult = "0")
        testCalculateExpression(inputExpression = "0÷1000.1", expectedResult = "0")
        testCalculateExpression(inputExpression = "1÷0", expectedResult = "Division by zero")
        testCalculateExpression(inputExpression = "0÷0", expectedResult = "Division undefined")
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
            inputExpression = "√(-9)",
            expectedResult = "Illegal √(x) for x < 0: x = -9"
        )
        testCalculateExpression(inputExpression = "√(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "√(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "√(2)", expectedResult = "1.4142135623731")
        testCalculateExpression(inputExpression = "√(9)", expectedResult = "3")
        println("OK")

        print("\t\tTesting the execution of the cbrt function... ")
        testCalculateExpression(inputExpression = "cbrt(-8)", expectedResult = "-2")
        testCalculateExpression(inputExpression = "cbrt(-2)", expectedResult = "-1.25992104989487")
        testCalculateExpression(inputExpression = "cbrt(-1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "cbrt(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "cbrt(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "cbrt(2)", expectedResult = "1.25992104989487")
        testCalculateExpression(inputExpression = "cbrt(8)", expectedResult = "2")
        println("OK")

        print("\t\tTesting the execution of the root function... ")
        testCalculateExpression(
            inputExpression = "root(-2, 3)",
            expectedResult = "-1.25992104989487"
        )
        testCalculateExpression(
            inputExpression = "root(-2, 0)",
            expectedResult = "Illegal root(x, n) for x < 0 and even n: x = -2; n = 0"
        )
        testCalculateExpression(
            inputExpression = "root(-2, 2)",
            expectedResult = "Illegal root(x, n) for x < 0 and even n: x = -2; n = 2"
        )
        testCalculateExpression(
            inputExpression = "root(-2, 0.5)",
            expectedResult = "Illegal root(x, n) for x < 0 and non-integer n: x = -2; n = 0.5"
        )
        testCalculateExpression(
            inputExpression = "root(0, -2)",
            expectedResult = "Illegal root(x, n) for x = 0 and n < 0: n = -2"
        )
        testCalculateExpression(inputExpression = "root(0, 0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "root(0, 2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "root(0, 2.3)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "root(2, -2)",
            expectedResult = "0.707106781186548"
        )
        testCalculateExpression(
            inputExpression = "root(2, 0)",
            expectedResult = "Illegal root(x, n) for x > 0 and n = 0: x = 2"
        )
        testCalculateExpression(inputExpression = "root(2,2)", expectedResult = "1.4142135623731")
        println("OK")

        print("\t\tTesting the execution of the exp function... ")
        testCalculateExpression(
            inputExpression = "exp(-10000000000000000000000)",
            expectedResult = "Overflow"
        )
        testCalculateExpression(inputExpression = "exp(-1)", expectedResult = "0.367879441171442")
        testCalculateExpression(inputExpression = "exp(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "exp(1)", expectedResult = "2.71828182845905")
        testCalculateExpression(
            inputExpression = "exp(10000000000000000)",
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
            inputExpression = "ln(-1)",
            expectedResult = "Illegal ln(x) for x <= 0: x = -1"
        )
        testCalculateExpression(
            inputExpression = "ln(0)",
            expectedResult = "Illegal ln(x) for x <= 0: x = 0"
        )
        testCalculateExpression(inputExpression = "ln(1 ÷ e^1000)", expectedResult = "-1000")
        testCalculateExpression(inputExpression = "ln(1÷e)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "ln(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "ln(e)", expectedResult = "1")
        testCalculateExpression(inputExpression = "ln(e^1000)", expectedResult = "1000")
        println("OK")

        print("\t\tTesting the execution of the log function... ")
        testCalculateExpression(
            inputExpression = "log(-1, -1)",
            expectedResult = "Illegal log(b, x) for b <= 0: b = -1"
        )
        testCalculateExpression(
            inputExpression = "log(0, -1)",
            expectedResult = "Illegal log(b, x) for b <= 0: b = 0"
        )
        testCalculateExpression(
            inputExpression = "log(1, -1)",
            expectedResult = "Illegal log(b, x) for b = 1"
        )
        testCalculateExpression(
            inputExpression = "log(3, -1)",
            expectedResult = "Illegal log(b, x) for x <= 0: x = -1"
        )
        testCalculateExpression(
            inputExpression = "log(3, 0)",
            expectedResult = "Illegal log(b, x) for x <= 0: x = 0"
        )
        testCalculateExpression(
            inputExpression = "log(3, 4)",
            expectedResult = "1.26185950714291"
        )
        println("OK")

        print("\t\tTesting the execution of the log2 function... ")
        testCalculateExpression(
            inputExpression = "log2(-1)",
            expectedResult = "Illegal log2(x) for x <= 0: x = -1"
        )
        testCalculateExpression(
            inputExpression = "log2(0)",
            expectedResult = "Illegal log2(x) for x <= 0: x = 0"
        )
        testCalculateExpression(inputExpression = "log2(1÷2^1000)", expectedResult = "-1000")
        testCalculateExpression(inputExpression = "log2(1÷2)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "log2(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "log2(2)", expectedResult = "1")
        testCalculateExpression(inputExpression = "log2(2^1000)", expectedResult = "1000")
        println("OK")

        print("\t\tTesting the execution of the lg function... ")
        testCalculateExpression(
            inputExpression = "lg(-1)",
            expectedResult = "Illegal lg(x) for x <= 0: x = -1"
        )
        testCalculateExpression(
            inputExpression = "lg(0)",
            expectedResult = "Illegal lg(x) for x <= 0: x = 0"
        )
        testCalculateExpression(inputExpression = "lg(1÷10^1000)", expectedResult = "-1000")
        testCalculateExpression(inputExpression = "lg(1÷10)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "lg(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "lg(10)", expectedResult = "1")
        testCalculateExpression(inputExpression = "lg(10^1000)", expectedResult = "1000")
        println("OK")

        println("\tTesting the execution of logarithmic functions... OK")
    }

    @Test
    fun testTrigonometricFunctions() {
        println("\tTesting the execution of trigonometric functions...")

        print("\t\tTesting the execution of the sin function... ")
        testCalculateExpression(inputExpression = "sin(-1000001π)", expectedResult = "0")
        testCalculateExpression(inputExpression = "sin(-1000001π÷2)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "sin(-2π)", expectedResult = "0")
        testCalculateExpression(inputExpression = "sin(-π)", expectedResult = "0")
        testCalculateExpression(inputExpression = "sin(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "sin(π÷2)", expectedResult = "1")
        testCalculateExpression(inputExpression = "sin(π)", expectedResult = "0")
        testCalculateExpression(inputExpression = "sin(3π÷2)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "sin(2π)", expectedResult = "0")
        testCalculateExpression(inputExpression = "sin(1000001π÷2)", expectedResult = "1")
        testCalculateExpression(inputExpression = "sin(1000001π)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the cos function... ")
        testCalculateExpression(inputExpression = "cos(-1000001π)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "cos(-1000001π÷2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "cos(-2π)", expectedResult = "1")
        testCalculateExpression(inputExpression = "cos(-π)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "cos(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "cos(π÷2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "cos(π)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "cos(3π÷2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "cos(2π)", expectedResult = "1")
        testCalculateExpression(inputExpression = "cos(1000001π÷2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "cos(1000001π)", expectedResult = "-1")
        println("OK")

        print("\t\tTesting the execution of the tan function... ")
        testCalculateExpression(inputExpression = "tan(-1000001π)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "tan(-1000001π÷2)",
            expectedResult = "Illegal tan(x) for x = π * k / 2, where k is odd: " +
                    "x = -1570797.89759122"
        )
        testCalculateExpression(inputExpression = "tan(-2π)", expectedResult = "0")
        testCalculateExpression(inputExpression = "tan(-π)", expectedResult = "0")
        testCalculateExpression(inputExpression = "tan(0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "tan(π÷2)",
            expectedResult = "Illegal tan(x) for x = π * k / 2, where k is odd: " +
                    "x = 1.5707963267949"
        )
        testCalculateExpression(inputExpression = "tan(π)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "tan(3π÷2)",
            expectedResult = "Illegal tan(x) for x = π * k / 2, where k is odd: " +
                    "x = 4.71238898038469"
        )
        testCalculateExpression(inputExpression = "tan(2π)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "tan(1000001π÷2)",
            expectedResult = "Illegal tan(x) for x = π * k / 2, where k is odd: " +
                    "x = 1570797.89759122"
        )
        testCalculateExpression(inputExpression = "tan(1000001π)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the cot function... ")
        testCalculateExpression(
            inputExpression = "cot(-1000001π)",
            expectedResult = "Illegal cot(x) for x = π * k, where k is an integer: " +
                    "x = -3141595.79518245"
        )
        testCalculateExpression(inputExpression = "cot(-1000001π÷2)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "cot(-2π)",
            expectedResult = "Illegal cot(x) for x = π * k, where k is an integer: " +
                    "x = -6.28318530717959"
        )
        testCalculateExpression(
            inputExpression = "cot(0)",
            expectedResult = "Illegal cot(x) for x = π * k, where k is an integer: x = 0"
        )
        testCalculateExpression(inputExpression = "cot(π÷2)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "cot(π)",
            expectedResult = "Illegal cot(x) for x = π * k, where k is an integer: " +
                    "x = 3.14159265358979"
        )
        testCalculateExpression(inputExpression = "cot(3π÷2)", expectedResult = "0")
        testCalculateExpression(inputExpression = "cot(1000001π÷2)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the sec function... ")
        testCalculateExpression(inputExpression = "sec(-1000001π)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "sec(-1000001π÷2)",
            expectedResult = "Illegal sec(x) for x = π * k / 2, where k is odd: " +
                    "x = -1570797.89759122"
        )
        testCalculateExpression(inputExpression = "sec(-2π)", expectedResult = "1")
        testCalculateExpression(inputExpression = "sec(0)", expectedResult = "1")
        testCalculateExpression(
            inputExpression = "sec(π÷2)",
            expectedResult = "Illegal sec(x) for x = π * k / 2, where k is odd: " +
                    "x = 1.5707963267949"
        )
        testCalculateExpression(inputExpression = "sec(π)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "sec(3π÷2)",
            expectedResult = "Illegal sec(x) for x = π * k / 2, where k is odd: " +
                    "x = 4.71238898038469"
        )
        testCalculateExpression(
            inputExpression = "sec(1000001π÷2)",
            expectedResult = "Illegal sec(x) for x = π * k / 2, where k is odd: " +
                    "x = 1570797.89759122"
        )
        println("OK")

        print("\t\tTesting the execution of the cosec function... ")
        testCalculateExpression(
            inputExpression = "cosec(-1000001π)",
            expectedResult = "Illegal cosec(x) for x = π * k, where k is an integer: " +
                    "x = -3141595.79518245"
        )
        testCalculateExpression(inputExpression = "cosec(-1000001π÷2)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "cosec(-2π)",
            expectedResult = "Illegal cosec(x) for x = π * k, where k is an integer: " +
                    "x = -6.28318530717959"
        )
        testCalculateExpression(
            inputExpression = "cosec(0)",
            expectedResult = "Illegal cosec(x) for x = π * k, where k is an integer: x = 0"
        )
        testCalculateExpression(inputExpression = "cosec(π÷2)", expectedResult = "1")
        testCalculateExpression(
            inputExpression = "cosec(π)",
            expectedResult = "Illegal cosec(x) for x = π * k, where k is an integer: " +
                    "x = 3.14159265358979"
        )
        testCalculateExpression(inputExpression = "cosec(3π÷2)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "cosec(1000001π÷2)", expectedResult = "1")
        println("OK")

        println("\tTesting the execution of trigonometric functions... OK")
    }

    @Test
    fun testTrigonometricFunctionsWithOtherAngleUnits() {
        println("\tTesting the execution of trigonometric functions with other angle units...")

        print("\t\tTesting the execution of the sin function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "sin(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "sin(90)", expectedResult = "1")
        testCalculateExpression(inputExpression = "sin(180)", expectedResult = "0")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "sin(300)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "sin(400)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the cos function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "cos(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "cos(90)", expectedResult = "0")
        testCalculateExpression(inputExpression = "cos(180)", expectedResult = "-1")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "cos(300)", expectedResult = "0")
        testCalculateExpression(inputExpression = "cos(400)", expectedResult = "1")
        println("OK")

        print("\t\tTesting the execution of the tan function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "tan(0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "tan(90)",
            expectedResult = "Illegal tan(x) for x = 90° * k, where k is odd: x = 90"
        )
        testCalculateExpression(inputExpression = "tan(180)", expectedResult = "0")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(
            inputExpression = "tan(300)",
            expectedResult = "Illegal tan(x) for x = 100g * k, where k is odd: x = 300"
        )
        testCalculateExpression(inputExpression = "tan(400)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the cot function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(
            inputExpression = "cot(0)",
            expectedResult = "Illegal cot(x) for x = 180° * k, where k is an integer: x = 0"
        )
        testCalculateExpression(inputExpression = "cot(90)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "cot(180)",
            expectedResult = "Illegal cot(x) for x = 180° * k, where k is an integer: x = 180"
        )
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "cot(300)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "cot(400)",
            expectedResult = "Illegal cot(x) for x = 200g * k, where k is an integer: x = 400"
        )
        println("OK")

        print("\t\tTesting the execution of the sec function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "sec(0)", expectedResult = "1")
        testCalculateExpression(
            inputExpression = "sec(90)",
            expectedResult = "Illegal sec(x) for x = 90° * k, where k is odd: x = 90"
        )
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "sec(200)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "sec(300)",
            expectedResult = "Illegal sec(x) for x = 100g * k, where k is odd: x = 300"
        )
        println("OK")

        print("\t\tTesting the execution of the cosec function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(
            inputExpression = "cosec(0)",
            expectedResult = "Illegal cosec(x) for x = 180° * k, where k is an integer: x = 0"
        )
        testCalculateExpression(inputExpression = "cosec(90)", expectedResult = "1")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(
            inputExpression = "cosec(200)",
            expectedResult = "Illegal cosec(x) for x = 200g * k, where k is an integer: x = 200"
        )
        testCalculateExpression(inputExpression = "cosec(300)", expectedResult = "-1")
        println("OK")

        println("\tTesting the execution of trigonometric functions with other angle units... OK")
    }

    @Test
    fun testInverseTrigonometricFunctions() {
        println("\tTesting the execution of inverse trigonometric functions...")

        print("\t\tTesting the execution of the asin function... ")
        testCalculateExpression(
            inputExpression = "asin(-2)",
            expectedResult = "Illegal asin(x) for x < -1: x = -2"
        )
        testCalculateExpression(inputExpression = "asin(-1)", expectedResult = "-1.5707963267949")
        testCalculateExpression(inputExpression = "asin(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "asin(1)", expectedResult = "1.5707963267949")
        testCalculateExpression(
            inputExpression = "asin(2)",
            expectedResult = "Illegal asin(x) for x > 1: x = 2"
        )
        println("OK")

        print("\t\tTesting the execution of the acos function... ")
        testCalculateExpression(
            inputExpression = "acos(-2)",
            expectedResult = "Illegal acos(x) for x < -1: x = -2"
        )
        testCalculateExpression(inputExpression = "acos(-1)", expectedResult = "3.14159265358979")
        testCalculateExpression(inputExpression = "acos(0)", expectedResult = "1.5707963267949")
        testCalculateExpression(inputExpression = "acos(1)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "acos(2)",
            expectedResult = "Illegal acos(x) for x > 1: x = 2"
        )
        println("OK")

        print("\t\tTesting the execution of the atan function... ")
        testCalculateExpression(
            inputExpression = "atan(-10^50)",
            expectedResult = "-1.5707963267949"
        )
        testCalculateExpression(
            inputExpression = "atan(-1)",
            expectedResult = "-0.785398163397448"
        )
        testCalculateExpression(inputExpression = "atan(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "atan(1)", expectedResult = "0.785398163397448")
        testCalculateExpression(
            inputExpression = "atan(10^50)",
            expectedResult = "1.5707963267949"
        )
        println("OK")

        print("\t\tTesting the execution of the acot function... ")
        testCalculateExpression(
            inputExpression = "acot(-10^50)",
            expectedResult = "3.14159265358979"
        )
        testCalculateExpression(inputExpression = "acot(-1)", expectedResult = "2.35619449019234")
        testCalculateExpression(inputExpression = "acot(0)", expectedResult = "1.5707963267949")
        testCalculateExpression(inputExpression = "acot(1)", expectedResult = "0.785398163397448")
        testCalculateExpression(inputExpression = "acot(10^50)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the asec function... ")
        testCalculateExpression(inputExpression = "asec(-2)", expectedResult = "2.0943951023932")
        testCalculateExpression(inputExpression = "asec(-1)", expectedResult = "3.14159265358979")
        testCalculateExpression(
            inputExpression = "asec(0)",
            expectedResult = "Illegal asec(x) for x > -1 and x < 1: x = 0"
        )
        testCalculateExpression(inputExpression = "asec(1)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "asec(2)",
            expectedResult = "1.0471975511966"
        )
        println("OK")

        print("\t\tTesting the execution of the acosec function... ")
        testCalculateExpression(
            inputExpression = "acosec(-2)",
            expectedResult = "-0.523598775598299"
        )
        testCalculateExpression(
            inputExpression = "acosec(-1)",
            expectedResult = "-1.5707963267949"
        )
        testCalculateExpression(
            inputExpression = "acosec(0)",
            expectedResult = "Illegal acosec(x) for x > -1 and x < 1: x = 0"
        )
        testCalculateExpression(
            inputExpression = "acosec(1)",
            expectedResult = "1.5707963267949"
        )
        testCalculateExpression(
            inputExpression = "acosec(2)",
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

        print("\t\tTesting the execution of the asin function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "asin(-1)", expectedResult = "-90")
        testCalculateExpression(inputExpression = "asin(0)", expectedResult = "0")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "asin(1)", expectedResult = "100")
        println("OK")

        print("\t\tTesting the execution of the acos function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "acos(-1)", expectedResult = "180")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "acos(0)", expectedResult = "100")
        testCalculateExpression(inputExpression = "acos(1)", expectedResult = "0")
        println("OK")

        print("\t\tTesting the execution of the atan function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "atan(-1)", expectedResult = "-45")
        testCalculateExpression(inputExpression = "atan(0)", expectedResult = "0")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "atan(1)", expectedResult = "50")
        println("OK")

        print("\t\tTesting the execution of the acot function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "acot(-1)", expectedResult = "135")
        testCalculateExpression(inputExpression = "acot(0)", expectedResult = "90")
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "acot(1)", expectedResult = "50")
        println("OK")

        print("\t\tTesting the execution of the asec function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "asec(-2)", expectedResult = "120")
        testCalculateExpression(inputExpression = "asec(-1)", expectedResult = "180")
        testCalculateExpression(
            inputExpression = "asec(0)",
            expectedResult = "Illegal asec(x) for x > -1 and x < 1: x = 0"
        )
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "asec(1)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "asec(2)",
            expectedResult = "66.6666666666667"
        )
        println("OK")

        print("\t\tTesting the execution of the acosec function... ")
        calculator.angleUnit = AngleUnit.DEGREE
        testCalculateExpression(inputExpression = "acosec(-2)", expectedResult = "-30")
        testCalculateExpression(inputExpression = "acosec(-1)", expectedResult = "-90")
        testCalculateExpression(
            inputExpression = "acosec(0)",
            expectedResult = "Illegal acosec(x) for x > -1 and x < 1: x = 0"
        )
        calculator.angleUnit = AngleUnit.GRADIAN
        testCalculateExpression(inputExpression = "acosec(1)", expectedResult = "100")
        testCalculateExpression(
            inputExpression = "acosec(2)",
            expectedResult = "33.3333333333333"
        )
        println("OK")

        println(
            "\tTesting the execution of inverse trigonometric functions with " +
                    "other angle units... OK"
        )
    }

    @Test
    fun testHyperbolicFunctions() {
        println("\tTesting the execution of hyperbolic functions...")

        print("\t\tTesting the execution of the sinh function... ")
        testCalculateExpression(
            inputExpression = "sinh(-10)",
            expectedResult = "-11013.2328747034"
        )
        testCalculateExpression(inputExpression = "sinh(-1)", expectedResult = "-1.1752011936438")
        testCalculateExpression(inputExpression = "sinh(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "sinh(1)", expectedResult = "1.1752011936438")
        testCalculateExpression(inputExpression = "sinh(10)", expectedResult = "11013.2328747034")
        println("OK")

        print("\t\tTesting the execution of the cosh function... ")
        testCalculateExpression(inputExpression = "cosh(-10)", expectedResult = "11013.2329201033")
        testCalculateExpression(inputExpression = "cosh(-1)", expectedResult = "1.54308063481524")
        testCalculateExpression(inputExpression = "cosh(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "cosh(1)", expectedResult = "1.54308063481524")
        testCalculateExpression(inputExpression = "cosh(10)", expectedResult = "11013.2329201033")
        println("OK")

        print("\t\tTesting the execution of the tanh function... ")
        testCalculateExpression(inputExpression = "tanh(-100)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "tanh(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "tanh(100)", expectedResult = "1")
        println("OK")

        print("\t\tTesting the execution of the coth function... ")
        testCalculateExpression(inputExpression = "coth(-100)", expectedResult = "-1")
        testCalculateExpression(
            inputExpression = "coth(0)",
            expectedResult = "Illegal coth(x) for x = 0"
        )
        testCalculateExpression(inputExpression = "coth(100)", expectedResult = "1")
        println("OK")

        print("\t\tTesting the execution of the sech function... ")
        testCalculateExpression(
            inputExpression = "sech(-10)",
            expectedResult = "0.000090799859338"
        )
        testCalculateExpression(inputExpression = "sech(-1)", expectedResult = "0.648054273663885")
        testCalculateExpression(inputExpression = "sech(0)", expectedResult = "1")
        testCalculateExpression(inputExpression = "sech(1)", expectedResult = "0.648054273663885")
        testCalculateExpression(inputExpression = "sech(10)", expectedResult = "0.000090799859338")
        println("OK")

        print("\t\tTesting the execution of the csch function... ")
        testCalculateExpression(
            inputExpression = "csch(-10)",
            expectedResult = "-0.000090799859712"
        )
        testCalculateExpression(
            inputExpression = "csch(-1)",
            expectedResult = "-0.850918128239322"
        )
        testCalculateExpression(
            inputExpression = "csch(0)",
            expectedResult = "Illegal csch(x) for x = 0"
        )
        testCalculateExpression(
            inputExpression = "csch(1)",
            expectedResult = "0.850918128239322"
        )
        testCalculateExpression(
            inputExpression = "csch(10)",
            expectedResult = "0.000090799859712"
        )
        println("OK")

        println("\tTesting the execution of hyperbolic functions... OK")
    }

    @Test
    fun testInverseHyperbolicFunctions() {
        println("\tTesting the execution of inverse hyperbolic functions...")

        print("\t\tTesting the execution of the asinh function... ")
        testCalculateExpression(
            inputExpression = "asinh(-100)",
            expectedResult = "-5.29834236561059"
        )
        testCalculateExpression(inputExpression = "asinh(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "asinh(1)", expectedResult = "0.881373587019543")
        println("OK")

        print("\t\tTesting the execution of the acosh function... ")
        testCalculateExpression(
            inputExpression = "acosh(0)",
            expectedResult = "Illegal acosh(x) for x < 1: x = 0"
        )
        testCalculateExpression(inputExpression = "acosh(1)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "acosh(100)",
            expectedResult = "5.29829236561048"
        )
        println("OK")

        print("\t\tTesting the execution of the atanh function... ")
        testCalculateExpression(
            inputExpression = "atanh(-2)",
            expectedResult = "Illegal atanh(x) for x <= -1: x = -2"
        )
        testCalculateExpression(
            inputExpression = "atanh(-1)",
            expectedResult = "Illegal atanh(x) for x <= -1: x = -1"
        )
        testCalculateExpression(
            inputExpression = "atanh(-0.5)",
            expectedResult = "-0.549306144334055"
        )
        testCalculateExpression(inputExpression = "atanh(0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "atanh(0.5)",
            expectedResult = "0.549306144334055"
        )
        testCalculateExpression(
            inputExpression = "atanh(1)",
            expectedResult = "Illegal atanh(x) for x >= 1: x = 1"
        )
        testCalculateExpression(
            inputExpression = "atanh(2)",
            expectedResult = "Illegal atanh(x) for x >= 1: x = 2"
        )
        println("OK")

        print("\t\tTesting the execution of the acoth function... ")
        testCalculateExpression(
            inputExpression = "acoth(-2)",
            expectedResult = "-0.549306144334055"
        )
        testCalculateExpression(
            inputExpression = "acoth(-1)",
            expectedResult = "Illegal acoth(x) for x >= -1 or x <= 1: x = -1"
        )
        testCalculateExpression(
            inputExpression = "acoth(0)",
            expectedResult = "Illegal acoth(x) for x >= -1 or x <= 1: x = 0"
        )
        testCalculateExpression(
            inputExpression = "acoth(1)",
            expectedResult = "Illegal acoth(x) for x >= -1 or x <= 1: x = 1"
        )
        testCalculateExpression(
            inputExpression = "acoth(2)",
            expectedResult = "0.549306144334055"
        )
        println("OK")

        print("\t\tTesting the execution of the asech function... ")
        testCalculateExpression(
            inputExpression = "asech(0)",
            expectedResult = "Illegal asech(x) for x <= 0: x = 0"
        )
        testCalculateExpression(
            inputExpression = "asech(0.5)",
            expectedResult = "1.31695789692482"
        )
        testCalculateExpression(inputExpression = "asech(1)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "asech(2)",
            expectedResult = "Illegal asech(x) for x > 1: x = 2"
        )
        println("OK")

        print("\t\tTesting the execution of the acsch function... ")
        testCalculateExpression(
            inputExpression = "acsch(-2)",
            expectedResult = "-0.481211825059603"
        )
        testCalculateExpression(
            inputExpression = "acsch(-1)",
            expectedResult = "-0.881373587019543"
        )
        testCalculateExpression(
            inputExpression = "acsch(-0.5)",
            expectedResult = "-1.44363547517881"
        )
        testCalculateExpression(
            inputExpression = "acsch(0)",
            expectedResult = "Illegal acsch(x) for x = 0"
        )
        testCalculateExpression(
            inputExpression = "acsch(0.5)",
            expectedResult = "1.44363547517881"
        )
        testCalculateExpression(
            inputExpression = "acsch(1)",
            expectedResult = "0.881373587019543"
        )
        testCalculateExpression(
            inputExpression = "acsch(2)",
            expectedResult = "0.481211825059603"
        )
        println("OK")

        println("\tTesting the execution of inverse hyperbolic functions... OK")
    }

    @Test
    fun testRoundingFunctions() {
        println("\tTesting the execution of rounding functions...")

        print("\t\tTesting the execution of the ceil function... ")
        testCalculateExpression(inputExpression = "ceil(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "ceil(1.1)", expectedResult = "2")
        testCalculateExpression(inputExpression = "ceil(1.5)", expectedResult = "2")
        testCalculateExpression(inputExpression = "ceil(1.7)", expectedResult = "2")
        testCalculateExpression(inputExpression = "ceil(-1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "ceil(-1.1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "ceil(-1.5)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "ceil(-1.7)", expectedResult = "-1")
        println("OK")

        print("\t\tTesting the execution of the floor function... ")
        testCalculateExpression(inputExpression = "floor(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "floor(1.1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "floor(1.5)", expectedResult = "1")
        testCalculateExpression(inputExpression = "floor(1.7)", expectedResult = "1")
        testCalculateExpression(inputExpression = "floor(-1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "floor(-1.1)", expectedResult = "-2")
        testCalculateExpression(inputExpression = "floor(-1.5)", expectedResult = "-2")
        testCalculateExpression(inputExpression = "floor(-1.7)", expectedResult = "-2")
        println("OK")

        print("\t\tTesting the execution of the round function... ")
        testCalculateExpression(inputExpression = "round(1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "round(1.1)", expectedResult = "1")
        testCalculateExpression(inputExpression = "round(1.5)", expectedResult = "2")
        testCalculateExpression(inputExpression = "round(1.7)", expectedResult = "2")
        testCalculateExpression(inputExpression = "round(-1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "round(-1.1)", expectedResult = "-1")
        testCalculateExpression(inputExpression = "round(-1.5)", expectedResult = "-2")
        testCalculateExpression(inputExpression = "round(-1.7)", expectedResult = "-2")
        println("OK")

        println("\tTesting the execution of rounding functions... OK")
    }

    @Test
    fun testOtherFunction() {
        println("\tTesting the execution of other functions...")

        print("\t\tTesting the execution of the abs function... ")
        testCalculateExpression(inputExpression = "abs(-10^100)", expectedResult = "1*10^+100")
        testCalculateExpression(inputExpression = "abs(-12)", expectedResult = "12")
        testCalculateExpression(inputExpression = "abs(0)", expectedResult = "0")
        testCalculateExpression(inputExpression = "abs(12)", expectedResult = "12")
        testCalculateExpression(inputExpression = "abs(10^100)", expectedResult = "1*10^+100")
        println("OK")

        print("\t\tTesting the execution of the fraction function... ")
        testCalculateExpression(inputExpression = "fraction(1.1)", expectedResult = "0.1")
        testCalculateExpression(
            inputExpression = "fraction(π)",
            expectedResult = "0.141592653589793"
        )
        testCalculateExpression(inputExpression = "fraction(1)", expectedResult = "0")
        testCalculateExpression(inputExpression = "fraction(0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "fraction(e)",
            expectedResult = "0.718281828459045"
        )
        println("OK")

        print("\t\tTesting the execution of the hypot function... ")
        testCalculateExpression(inputExpression = "hypot(0,0)", expectedResult = "0")
        testCalculateExpression(
            inputExpression = "hypot(-1,-1)",
            expectedResult = "1.4142135623731"
        )
        testCalculateExpression(inputExpression = "hypot(3,4)", expectedResult = "5")
        testCalculateExpression(inputExpression = "hypot(12,5)", expectedResult = "13")
        testCalculateExpression(
            inputExpression = "hypot(4,4)",
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
            inputExpression = "sin()",
            expectedResult = "The function 'sin' expects 1 arguments, but given 0"
        )
        testCalculateExpression(
            inputExpression = "sin(1,2)",
            expectedResult = "The function 'sin' expects 1 arguments, but given 2"
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
        testCalculateExpression(inputExpression = "1.2*8÷2.1", expectedResult = "4.57142857142857")
        testCalculateExpression(inputExpression = "+-+++1.2", expectedResult = "-1.2")
        testCalculateExpression(
            inputExpression = "2^3^9",
            expectedResult = "1.49074943748314*10^+5925"
        )
        testCalculateExpression(inputExpression = "2!!%!", expectedResult = "0.988844203263913")
        testCalculateExpression(inputExpression = "--+1.0+-+-+-++-1.3", expectedResult = "2.3")
        testCalculateExpression(
            inputExpression = "-7^+-2!!^-7-+-4%*--4(--1.0*+-1.0)÷(-7^+-2!)",
            expectedResult = "6.85508744956883"
        )
        testCalculateExpression(inputExpression = "(1+2)*3", expectedResult = "9")
        testCalculateExpression(inputExpression = "(1-2)e", expectedResult = "-2.71828182845905")
        testCalculateExpression(
            inputExpression = "(((-7)^(+2)!!)^-7-+-4%)*(--4(--1.0*+-1.0)÷(-7^+-2!))",
            expectedResult = "7.84000000028899"
        )
        testCalculateExpression(
            inputExpression = "log(27^2,8!)8+sin(1+2)",
            expectedResult = "13.0114207345659"
        )

        println("\tTesting the execution of complex expressions... OK")
    }

    private fun testCalculateExpression(inputExpression: String, expectedResult: String) {
        val actualResult =
            when (val result = calculator.calculateExpression(inputExpression)) {
                is Nothing -> ""
                is Number -> result.toString(15, 9)
                is Error -> result.e.localizedMessage
                else -> result.toString()
            }

        assertEquals(
            "The actual result is not equal to the expected one",
            expectedResult, actualResult
        )
    }


    companion object {
        @BeforeClass
        @JvmStatic
        fun start() {
            println(
                """
                |Testing methods of the Calculator and Number classes,
                |as well as functions from the CalculatorContext class...
                """.trimMargin().trimIndent()
            )
        }

        @AfterClass
        @JvmStatic
        fun finish() {
            println(
                """
                |Testing methods of the Calculator and Number classes,
                |as well as functions from the CalculatorContext class... OK
                """.trimMargin().trimIndent()
            )
            println()
        }
    }
}
