package com.sunman.libcalculator

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import java.math.MathContext

internal class TestCalculator {

    private val calculator = Calculator(MathContext.DECIMAL128)


    @Test
    fun testOutputOfNumbers() {
        print("\tTesting the outputs of numbers... ")
        testExecutionStatement("123456789", "123456789")
        testExecutionStatement("10^9", "1000000000")
        testExecutionStatement("10^10", "1*10^+10")
        testExecutionStatement("1.1*10^10", "11000000000")
        testExecutionStatement("10^10+0.1", "10000000000.1")
        testExecutionStatement("111111111111111", "111111111111111")
        testExecutionStatement("1111111111111111", "1.11111111111111*10^+15")
        testExecutionStatement("1111111111111115", "1.11111111111112*10^+15")
        testExecutionStatement("12345678912345678", "1.23456789123457*10^+16")
        println("OK")
    }

    @Test
    fun testOperators() {
        println("\tTesting the execution of operators...")

        print("\t\tTesting the execution of the unary plus operator... ")
        testExecutionStatement("+1", "1")
        testExecutionStatement("+0", "0")
        println("OK")

        print("\t\tTesting the execution of the unary minus operator... ")
        testExecutionStatement("-12", "-12")
        testExecutionStatement("-0", "0")
        testExecutionStatement("-10000.1", "-10000.1")
        println("OK")

        print("\t\tTesting the execution of the addition operator... ")
        testExecutionStatement("1+1", "2")
        testExecutionStatement("1+0", "1")
        testExecutionStatement("0+0", "0")
        println("OK")

        print("\t\tTesting the execution of the subtraction operator... ")
        testExecutionStatement("12.1-6.9", "5.2")
        testExecutionStatement("12.1-0.0", "12.1")
        testExecutionStatement("0.00000-0", "0")
        println("OK")

        print("\t\tTesting the execution of the multiplication operator... ")
        testExecutionStatement("6*2", "12")
        testExecutionStatement("0.0*0.100", "0")
        testExecutionStatement("0.000*1000000000000.1234", "0")
        testExecutionStatement("0.0*0.00000000", "0")
        testExecutionStatement("1.0*0.0001", "0.0001")
        testExecutionStatement("1.0*10000000.124", "10000000.124")
        testExecutionStatement("1.0*1.0000000000", "1")
        println("OK")

        print("\t\tTesting the execution of the division operator... ")
        testExecutionStatement("6÷3", "2")
        testExecutionStatement("1.2÷3", "0.4")
        testExecutionStatement("1.2÷0.4", "3")
        testExecutionStatement("1÷5", "0.2")
        testExecutionStatement("1÷3", "0.333333333333333")
        testExecutionStatement("0÷1", "0")
        testExecutionStatement("0÷1000.1", "0")
        testExecutionStatement("1÷0", "Division by zero")
        testExecutionStatement("0÷0", "Division undefined")
        println("OK")

        print("\t\tTesting the execution of the exponentiation operator... ")
        testExecutionStatement("(-2)^-2", "0.25")
        testExecutionStatement("(-2)^-3", "-0.125")
        testExecutionStatement(
            "(-2)^-0.5",
            "Illegal x^y for x < 0 and non-integer y: x = -2; y = -0.5"

        )
        testExecutionStatement("(-2)^0.0", "1")
        testExecutionStatement("(-2)^2", "4")
        testExecutionStatement("(-2)^3", "-8")
        testExecutionStatement("0^-1.2", "Illegal x^y for x = 0 and y < 0: y = -1.2")
        testExecutionStatement("0.0^-2", "Illegal x^y for x = 0 and y < 0: y = -2")
        testExecutionStatement("0.0^0.000", "1")
        testExecutionStatement("0.0^0.5", "0")
        testExecutionStatement("0.0^1", "0")
        testExecutionStatement("1^1000", "1")
        testExecutionStatement("1.1^2", "1.21")
        testExecutionStatement("1.21^0.5", "1.1")
        testExecutionStatement("2^-2", "0.25")
        testExecutionStatement("2^0", "1")
        testExecutionStatement("2^0.5", "1.4142135623731")
        testExecutionStatement("2^2", "4")
        testExecutionStatement("2^10000000000000", "Overflow")
        testExecutionStatement("2^-100000000000000000", "Overflow")
        println("OK")

        print("\t\tTesting the execution of the factorial operator... ")
        testExecutionStatement("(-0.5)!", "1.77245385090552")
        testExecutionStatement("(-1)!", "Illegal x! for x < 0, where x is integer: x = -1")
        testExecutionStatement("0!", "1")
        testExecutionStatement("0.5!", "0.886226925452758")
        testExecutionStatement("1!", "1")
        testExecutionStatement("10!", "3628800")
        println("OK")

        print("\t\tTesting the execution of the percent operator... ")
        testExecutionStatement("0%", "0")
        testExecutionStatement("1%", "0.01")
        testExecutionStatement("100%", "1")
        println("OK")

        println("\tTesting the execution of operators... OK")
    }

    @Test
    fun testPowerFunctions() {
        println("\tTesting the execution of power functions...")

        print("\t\tTesting the execution of the sqrt function... ")
        testExecutionStatement("√(-9)", "Illegal √(x) for x < 0: x = -9")
        testExecutionStatement("√(0)", "0")
        testExecutionStatement("√(1)", "1")
        testExecutionStatement("√(2)", "1.4142135623731")
        testExecutionStatement("√(9)", "3")
        println("OK")

        print("\t\tTesting the execution of the cbrt function... ")
        testExecutionStatement("cbrt(-8)", "-2")
        testExecutionStatement("cbrt(-2)", "-1.25992104989487")
        testExecutionStatement("cbrt(-1)", "-1")
        testExecutionStatement("cbrt(0)", "0")
        testExecutionStatement("cbrt(1)", "1")
        testExecutionStatement("cbrt(2)", "1.25992104989487")
        testExecutionStatement("cbrt(8)", "2")
        println("OK")

        print("\t\tTesting the execution of the root function... ")
        testExecutionStatement("root(-2,3)", "-1.25992104989487")
        testExecutionStatement(
            "root(-2,0)",
            "Illegal root(x, n) for x < 0 and even n: x = -2; n = 0"
        )
        testExecutionStatement(
            "root(-2,2)",
            "Illegal root(x, n) for x < 0 and even n: x = -2; n = 2"
        )
        testExecutionStatement(
            "root(-2,0.5)",
            "Illegal root(x, n) for x < 0 and non-integer n: x = -2; n = 0.5"
        )
        testExecutionStatement(
            "root(0,-2)",
            "Illegal root(x, n) for x = 0 and n < 0: n = -2"
        )
        testExecutionStatement("root(0,0)", "0")
        testExecutionStatement("root(0,2)", "0")
        testExecutionStatement("root(0,2.3)", "0")
        testExecutionStatement("root(2,-2)", "0.707106781186548")
        testExecutionStatement(
            "root(2,0)",
            "Illegal root(x, n) for x > 0 and n = 0: x = 2"
        )
        testExecutionStatement("root(2,2)", "1.4142135623731")
        println("OK")

        print("\t\tTesting the execution of the exp function... ")
        testExecutionStatement("exp(-10000000000000000000000)", "Overflow")
        testExecutionStatement("exp(-1)", "0.367879441171442")
        testExecutionStatement("exp(0)", "1")
        testExecutionStatement("exp(1)", "2.71828182845905")
        testExecutionStatement("exp(10000000000000000)", "Overflow")
        println("OK")

        println("\tTesting the execution of power functions... OK")
    }

    @Test
    fun testLogarithmicFunctions() {
        println("\tTesting the execution of logarithmic functions...")

        print("\t\tTesting the execution of the ln function... ")
        testExecutionStatement("ln(-1)", "Illegal ln(x) for x <= 0: x = -1")
        testExecutionStatement("ln(0)", "Illegal ln(x) for x <= 0: x = 0")
        testExecutionStatement("ln(1÷e^1000)", "-1000")
        testExecutionStatement("ln(1÷e)", "-1")
        testExecutionStatement("ln(1)", "0")
        testExecutionStatement("ln(e)", "1")
        testExecutionStatement("ln(e^1000)", "1000")
        println("OK")

        print("\t\tTesting the execution of the log function... ")
        testExecutionStatement(
            "log(-1,-1)",
            "Illegal log(b, x) for b <= 0: b = -1"
        )
        testExecutionStatement(
            "log(0,-1)",
            "Illegal log(b, x) for b <= 0: b = 0"
        )
        testExecutionStatement(
            "log(1,-1)",
            "Illegal log(b, x) for b = 1"
        )
        testExecutionStatement(
            "log(3,-1)",
            "Illegal log(b, x) for x <= 0: x = -1"
        )
        testExecutionStatement(
            "log(3,0)",
            "Illegal log(b, x) for x <= 0: x = 0"
        )
        testExecutionStatement("log(3,4)", "1.26185950714291")
        println("OK")

        print("\t\tTesting the execution of the log2 function... ")
        testExecutionStatement("log2(-1)", "Illegal log2(x) for x <= 0: x = -1")
        testExecutionStatement("log2(0)", "Illegal log2(x) for x <= 0: x = 0")
        testExecutionStatement("log2(1÷2^1000)", "-1000")
        testExecutionStatement("log2(1÷2)", "-1")
        testExecutionStatement("log2(1)", "0")
        testExecutionStatement("log2(2)", "1")
        testExecutionStatement("log2(2^1000)", "1000")
        println("OK")

        print("\t\tTesting the execution of the log10 function... ")
        testExecutionStatement("log10(-1)", "Illegal log10(x) for x <= 0: x = -1")
        testExecutionStatement("log10(0)", "Illegal log10(x) for x <= 0: x = 0")
        testExecutionStatement("log10(1÷10^1000)", "-1000")
        testExecutionStatement("log10(1÷10)", "-1")
        testExecutionStatement("log10(1)", "0")
        testExecutionStatement("log10(10)", "1")
        testExecutionStatement("log10(10^1000)", "1000")
        println("OK")

        println("\tTesting the execution of logarithmic functions... OK")
    }

    @Test
    fun testTrigonometricFunctions() {
        println("\tTesting the execution of trigonometric functions...")

        print("\t\tTesting the execution of the sin function... ")
        testExecutionStatement("sin(-1000001π)", "0")
        testExecutionStatement("sin(-1000001π÷2)", "-1")
        testExecutionStatement("sin(-2π)", "0")
        testExecutionStatement("sin(-π)", "0")
        testExecutionStatement("sin(0)", "0")
        testExecutionStatement("sin(π÷2)", "1")
        testExecutionStatement("sin(π)", "0")
        testExecutionStatement("sin(3π÷2)", "-1")
        testExecutionStatement("sin(2π)", "0")
        testExecutionStatement("sin(1000001π÷2)", "1")
        testExecutionStatement("sin(1000001π)", "0")
        println("OK")

        print("\t\tTesting the execution of the cos function... ")
        testExecutionStatement("cos(-1000001π)", "-1")
        testExecutionStatement("cos(-1000001π÷2)", "0")
        testExecutionStatement("cos(-2π)", "1")
        testExecutionStatement("cos(-π)", "-1")
        testExecutionStatement("cos(0)", "1")
        testExecutionStatement("cos(π÷2)", "0")
        testExecutionStatement("cos(π)", "-1")
        testExecutionStatement("cos(3π÷2)", "0")
        testExecutionStatement("cos(2π)", "1")
        testExecutionStatement("cos(1000001π÷2)", "0")
        testExecutionStatement("cos(1000001π)", "-1")
        println("OK")

        print("\t\tTesting the execution of the tan function... ")
        testExecutionStatement("tan(-1000001π)", "0")
        testExecutionStatement(
            "tan(-1000001π÷2)",
            "Illegal tan(x) for x = π * k / 2, where k is odd: x = -1570797.89759122"
        )
        testExecutionStatement("tan(-2π)", "0")
        testExecutionStatement("tan(-π)", "0")
        testExecutionStatement("tan(0)", "0")
        testExecutionStatement(
            "tan(π÷2)",
            "Illegal tan(x) for x = π * k / 2, where k is odd: x = 1.5707963267949"
        )
        testExecutionStatement("tan(π)", "0")
        testExecutionStatement(
            "tan(3π÷2)",
            "Illegal tan(x) for x = π * k / 2, where k is odd: x = 4.71238898038469"
        )
        testExecutionStatement("tan(2π)", "0")
        testExecutionStatement(
            "tan(1000001π÷2)",
            "Illegal tan(x) for x = π * k / 2, where k is odd: x = 1570797.89759122"
        )
        testExecutionStatement("tan(1000001π)", "0")
        println("OK")

        print("\t\tTesting the execution of the cot function... ")
        testExecutionStatement(
            "cot(-1000001π)",
            "Illegal cot(x) for x = π * k, where k is an integer: x = -3141595.79518245"
        )
        testExecutionStatement("cot(-1000001π÷2)", "0")
        testExecutionStatement(
            "cot(-2π)",
            "Illegal cot(x) for x = π * k, where k is an integer: x = -6.28318530717959"
        )
        testExecutionStatement(
            "cot(0)",
            "Illegal cot(x) for x = π * k, where k is an integer: x = 0"
        )
        testExecutionStatement("cot(π÷2)", "0")
        testExecutionStatement(
            "cot(π)",
            "Illegal cot(x) for x = π * k, where k is an integer: x = 3.14159265358979"
        )
        testExecutionStatement("cot(3π÷2)", "0")
        testExecutionStatement("cot(1000001π÷2)", "0")
        println("OK")

        println("\tTesting the execution of trigonometric functions... OK")
    }

    @Test
    fun testInverseTrigonometricFunctions() {
        println("\tTesting the execution of inverse trigonometric functions...")

        print("\t\tTesting the execution of the asin function... ")
        testExecutionStatement("asin(-2)", "Illegal asin(x) for x < -1: x = -2")
        testExecutionStatement("asin(-1)", "-1.5707963267949")
        testExecutionStatement("asin(0)", "0")
        testExecutionStatement("asin(1)", "1.5707963267949")
        testExecutionStatement("asin(2)", "Illegal asin(x) for x > 1: x = 2")
        println("OK")

        print("\t\tTesting the execution of the acos function... ")
        testExecutionStatement("acos(-2)", "Illegal acos(x) for x < -1: x = -2")
        testExecutionStatement("acos(-1)", "3.14159265358979")
        testExecutionStatement("acos(0)", "1.5707963267949")
        testExecutionStatement("acos(1)", "0")
        testExecutionStatement("acos(2)", "Illegal acos(x) for x > 1: x = 2")
        println("OK")

        print("\t\tTesting the execution of the atan function... ")
        testExecutionStatement("atan(-10^50)", "-1.5707963267949")
        testExecutionStatement("atan(-1)", "-0.785398163397448")
        testExecutionStatement("atan(0)", "0")
        testExecutionStatement("atan(1)", "0.785398163397448")
        testExecutionStatement("atan(10^50)", "1.5707963267949")
        println("OK")

        print("\t\tTesting the execution of the acot function... ")
        testExecutionStatement("acot(-10^50)", "3.14159265358979")
        testExecutionStatement("acot(-1)", "2.35619449019234")
        testExecutionStatement("acot(0)", "1.5707963267949")
        testExecutionStatement("acot(1)", "0.785398163397448")
        testExecutionStatement("acot(10^50)", "0")
        println("OK")

        println("\tTesting the execution of inverse trigonometric functions... OK")
    }

    @Test
    fun testHyperbolicFunctions() {
        println("\tTesting the execution of hyperbolic functions...")

        print("\t\tTesting the execution of the sinh function... ")
        testExecutionStatement("sinh(-10)", "-11013.2328747034")
        testExecutionStatement("sinh(-1)", "-1.1752011936438")
        testExecutionStatement("sinh(0)", "0")
        testExecutionStatement("sinh(1)", "1.1752011936438")
        testExecutionStatement("sinh(10)", "11013.2328747034")
        println("OK")

        print("\t\tTesting the execution of the cosh function... ")
        testExecutionStatement("cosh(-10)", "11013.2329201033")
        testExecutionStatement("cosh(-1)", "1.54308063481524")
        testExecutionStatement("cosh(0)", "1")
        testExecutionStatement("cosh(1)", "1.54308063481524")
        testExecutionStatement("cosh(10)", "11013.2329201033")
        println("OK")

        print("\t\tTesting the execution of the tanh function... ")
        testExecutionStatement("tanh(-100)", "-1")
        testExecutionStatement("tanh(0)", "0")
        testExecutionStatement("tanh(100)", "1")
        println("OK")

        print("\t\tTesting the execution of the coth function... ")
        testExecutionStatement("coth(-100)", "-1")
        testExecutionStatement("coth(0)", "Illegal coth(x) for x = 0")
        testExecutionStatement("coth(100)", "1")
        println("OK")

        println("\tTesting the execution of hyperbolic functions... OK")
    }

    @Test
    fun testInverseHyperbolicFunctions() {
        println("\tTesting the execution of inverse hyperbolic functions...")

        print("\t\tTesting the execution of the asinh function... ")
        testExecutionStatement("asinh(-100)", "-5.29834236561059")
        testExecutionStatement("asinh(0)", "0")
        testExecutionStatement("asinh(1)", "0.881373587019543")
        println("OK")

        print("\t\tTesting the execution of the acosh function... ")
        testExecutionStatement("acosh(0)", "Illegal acosh(x) for x < 1: x = 0")
        testExecutionStatement("acosh(1)", "0")
        testExecutionStatement("acosh(100)", "5.29829236561048")
        println("OK")

        print("\t\tTesting the execution of the atanh function... ")
        testExecutionStatement("atanh(-2)", "Illegal atanh(x) for x <= -1: x = -2")
        testExecutionStatement("atanh(-1)", "Illegal atanh(x) for x <= -1: x = -1")
        testExecutionStatement("atanh(-0.5)", "-0.549306144334055")
        testExecutionStatement("atanh(0)", "0")
        testExecutionStatement("atanh(0.5)", "0.549306144334055")
        testExecutionStatement("atanh(1)", "Illegal atanh(x) for x >= 1: x = 1")
        testExecutionStatement("atanh(2)", "Illegal atanh(x) for x >= 1: x = 2")
        println("OK")

        print("\t\tTesting the execution of the acoth function... ")
        testExecutionStatement("acoth(-2)", "-0.549306144334055")
        testExecutionStatement("acoth(-1)", "Illegal acoth(x) for x >= -1 or x <= 1: x = -1")
        testExecutionStatement("acoth(0)", "Illegal acoth(x) for x >= -1 or x <= 1: x = 0")
        testExecutionStatement("acoth(1)", "Illegal acoth(x) for x >= -1 or x <= 1: x = 1")
        testExecutionStatement("acoth(2)", "0.549306144334055")
        println("OK")

        println("\tTesting the execution of inverse hyperbolic functions... OK")
    }

    @Test
    fun testRoundingFunctions() {
        println("\tTesting the execution of rounding functions...")

        print("\t\tTesting the execution of the ceil function... ")
        testExecutionStatement("ceil(1)", "1")
        testExecutionStatement("ceil(1.1)", "2")
        testExecutionStatement("ceil(1.5)", "2")
        testExecutionStatement("ceil(1.7)", "2")
        testExecutionStatement("ceil(-1)", "-1")
        testExecutionStatement("ceil(-1.1)", "-1")
        testExecutionStatement("ceil(-1.5)", "-1")
        testExecutionStatement("ceil(-1.7)", "-1")
        println("OK")

        print("\t\tTesting the execution of the floor function... ")
        testExecutionStatement("floor(1)", "1")
        testExecutionStatement("floor(1.1)", "1")
        testExecutionStatement("floor(1.5)", "1")
        testExecutionStatement("floor(1.7)", "1")
        testExecutionStatement("floor(-1)", "-1")
        testExecutionStatement("floor(-1.1)", "-2")
        testExecutionStatement("floor(-1.5)", "-2")
        testExecutionStatement("floor(-1.7)", "-2")
        println("OK")

        print("\t\tTesting the execution of the round function... ")
        testExecutionStatement("round(1)", "1")
        testExecutionStatement("round(1.1)", "1")
        testExecutionStatement("round(1.5)", "2")
        testExecutionStatement("round(1.7)", "2")
        testExecutionStatement("round(-1)", "-1")
        testExecutionStatement("round(-1.1)", "-1")
        testExecutionStatement("round(-1.5)", "-2")
        testExecutionStatement("round(-1.7)", "-2")
        println("OK")

        println("\tTesting the execution of rounding functions... OK")
    }

    @Test
    fun testOtherFunction() {
        println("\tTesting the execution of other functions...")

        print("\t\tTesting the execution of the abs function... ")
        testExecutionStatement("abs(-10^100)", "1*10^+100")
        testExecutionStatement("abs(-12)", "12")
        testExecutionStatement("abs(0)", "0")
        testExecutionStatement("abs(12)", "12")
        testExecutionStatement("abs(10^100)", "1*10^+100")
        println("OK")

        print("\t\tTesting the execution of the fraction function... ")
        testExecutionStatement("fraction(1.1)", "0.1")
        testExecutionStatement("fraction(π)", "0.141592653589793")
        testExecutionStatement("fraction(1)", "0")
        testExecutionStatement("fraction(0)", "0")
        testExecutionStatement("fraction(e)", "0.718281828459045")
        println("OK")

        print("\t\tTesting the execution of the hypot function... ")
        testExecutionStatement("hypot(0,0)", "0")
        testExecutionStatement("hypot(-1,-1)", "1.4142135623731")
        testExecutionStatement("hypot(3,4)", "5")
        testExecutionStatement("hypot(12,5)", "13")
        testExecutionStatement("hypot(4,4)", "5.65685424949238")
        println("OK")

        println("\tTesting the execution of other functions... OK")
    }

    @Test
    fun testFunctionsWithErrors() {
        println("\tTesting the execution of functions with errors...")

        print("\t\tTesting the execution of functions with a lack or excess of arguments... ")
        testExecutionStatement(
            "sin()",
            "The function 'sin' expects 1 arguments, but given 0"
        )
        testExecutionStatement(
            "sin(1,2)",
            "The function 'sin' expects 1 arguments, but given 2"
        )
        println("OK")

        print("\t\tTesting the execution of an unknown function... ")
        testExecutionStatement("procedure()", "Unknown function 'procedure'")
        println("OK")

        println("\tTesting the execution of functions with errors... OK")
    }

    @Test
    fun testSpecialCases() {
        println("\tTesting special cases of the Calculator class...")

        print("\t\tTesting the execution of empty expressions... ")
        testExecutionStatement("", "")
        println("OK")

        println("\tTesting methods of the Calculator class... OK")
    }

    @Test
    fun testComplexExpressions() {
        println("\tTesting the execution of complex expressions... ")

        testExecutionStatement("10!(23)(24)", "2003097600")
        testExecutionStatement("9+1-2", "8")
        testExecutionStatement("1.2*8÷2.1", "4.57142857142857")
        testExecutionStatement("+-+++1.2", "-1.2")
        testExecutionStatement("2^3^9", "1.49074943748314*10^+5925")
        testExecutionStatement("2!!%!", "0.988844203263913")
        testExecutionStatement("--+1.0+-+-+-++-1.3", "2.3")
        testExecutionStatement(
            "-7^+-2!!^-7-+-4%*--4(--1.0*+-1.0)÷(-7^+-2!)",
            "6.85508744956883"
        )
        testExecutionStatement("(1+2)*3", "9")
        testExecutionStatement("(1-2)e", "-2.71828182845905")
        testExecutionStatement(
            "(((-7)^(+2)!!)^-7-+-4%)*(--4(--1.0*+-1.0)÷(-7^+-2!))",
            "7.84000000028899"
        )
        testExecutionStatement(
            "log(27^2,8!)8+sin(1+2)",
            "13.0114207345659"
        )

        println("\tTesting the execution of complex expressions... OK")
    }

    private fun testExecutionStatement(inputString: String, expectedResult: String) {
        val actualResult = when (val result = calculator.executeExpression(inputString)) {
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
            |as well as functions from the Functions.kt file...
            """.trimMargin().trimIndent()
            )
        }

        @AfterClass
        @JvmStatic
        fun finish() {
            println(
                """
            |Testing methods of the Calculator and Number classes,
            |as well as functions from the Functions.kt file... OK
            """.trimMargin().trimIndent()
            )
            println()
        }
    }
}
