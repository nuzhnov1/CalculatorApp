package com.sunman.libcalculator

import com.sunman.libcalculator.internal.Parser
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.BeforeClass
import org.junit.Test

internal class TestParser {

    @Test
    fun testParseOfSimpleExpressions() {
        println("\tTesting the parsing of simple expressions, such as: '1 + 1', '2 * 2', etc...")

        print("\t\tTesting the parsing of valid simple expressions... ")
        testParseExpression(inputExpression = "", expectedPostfixRecord = "")
        testParseExpression(inputExpression = " 5   ", expectedPostfixRecord = "5")
        testParseExpression(inputExpression = " e ", expectedPostfixRecord = "e")
        testParseExpression(inputExpression = "+1.0", expectedPostfixRecord = "1.0")
        testParseExpression(inputExpression = "-5", expectedPostfixRecord = "5 u-")
        testParseExpression(inputExpression = "(1 )", expectedPostfixRecord = "1")
        testParseExpression(inputExpression = "1-1", expectedPostfixRecord = "1 1 -")
        testParseExpression(inputExpression = "e + π", expectedPostfixRecord = "e π +")
        testParseExpression(inputExpression = "1.0 - 2", expectedPostfixRecord = "1.0 2 -")
        testParseExpression(inputExpression = "2.0*3.2", expectedPostfixRecord = "2.0 3.2 *")
        testParseExpression(inputExpression = ".1÷1.", expectedPostfixRecord = ".1 1. ÷")
        testParseExpression(inputExpression = "1.0÷a", expectedPostfixRecord = "1.0 a ÷")
        testParseExpression(inputExpression = "ee", expectedPostfixRecord = "e e *")
        testParseExpression(inputExpression = "eπ", expectedPostfixRecord = "e π *")
        testParseExpression(inputExpression = "π π", expectedPostfixRecord = "π π *")
        testParseExpression(inputExpression = "24 e", expectedPostfixRecord = "24 e *")
        testParseExpression(inputExpression = "24exp", expectedPostfixRecord = "24 exp *")
        testParseExpression(inputExpression = "(23)(24)", expectedPostfixRecord = "23 24 *")
        testParseExpression(inputExpression = "10 ! π", expectedPostfixRecord = "10 ! π *")
        testParseExpression(inputExpression = "4.1 ^ 1.1", expectedPostfixRecord = "4.1 1.1 ^")
        testParseExpression(inputExpression = "5!", expectedPostfixRecord = "5 !")
        testParseExpression(inputExpression = "54%", expectedPostfixRecord = "54 %")
        println("OK")

        print("\t\tTesting the parsing of invalid simple expressions... ")
        testParseInvalidExpression(
            inputExpression = "*1",
            expectedMessage = "Expected expression, got '*'"
        )
        testParseInvalidExpression(
            inputExpression = ")a",
            expectedMessage = "Expected expression, got ')'"
        )
        testParseInvalidExpression(
            inputExpression = ",a",
            expectedMessage = "Expected expression, got ','"
        )
        println("OK")

        println(
            "\tTesting the parsing of simple expressions, such as: '1 + 1', '2 * 2', etc... OK"
        )
    }

    @Test
    fun testAssociationOfOperators() {
        print("\tTesting the association of operators in expressions... ")
        testParseExpression(inputExpression = "9+1-2", expectedPostfixRecord = "9 1 + 2 -")
        testParseExpression(inputExpression = "1.2*8÷2.1", expectedPostfixRecord = "1.2 8 * 2.1 ÷")
        testParseExpression(inputExpression = "+-+++1.2", expectedPostfixRecord = "1.2 u-")
        testParseExpression(inputExpression = "2 ^3 ^ 9", expectedPostfixRecord = "2 3 9 ^ ^")
        testParseExpression(inputExpression = "2!!%!", expectedPostfixRecord = "2 ! ! % !")
        println("OK")
    }

    @Test
    fun testPriorityOfOperators() {
        println("\tTesting the operator priorities...")

        print("\t\tTesting the operator priorities in valid expressions without parenthesis... ")
        testParseExpression(inputExpression = "1+2*3", expectedPostfixRecord = "1 2 3 * +")
        testParseExpression(inputExpression = "1-2e", expectedPostfixRecord = "1 2 e * -")
        testParseExpression(inputExpression = "1-+-2e", expectedPostfixRecord = "1 2 u- e * -")
        testParseExpression(
            inputExpression = "--+1.0 +-+-+-++- 1.3",
            expectedPostfixRecord = "1.0 u- u- 1.3 u- u- u- u- +"
        )
        testParseExpression(
            inputExpression = "--1.0 * +-1.0",
            expectedPostfixRecord = "1.0 u- u- 1.0 u- *"
        )
        testParseExpression(inputExpression = "-7^+-2!", expectedPostfixRecord = "7 2 ! u- ^ u-")
        testParseExpression(
            inputExpression = "-7^+-2!!^-7 - +-4% * --4var ÷ another",
            expectedPostfixRecord = "7 2 ! ! 7 u- ^ u- ^ u- 4 % u- 4 u- u- * var * another ÷ -"
        )
        println("OK")

        print("\t\tTesting the operator priorities in invalid expressions without parenthesis... ")
        testParseInvalidExpression(
            inputExpression = "-7+",
            expectedMessage = "Expected expression, got end of line"
        )
        testParseInvalidExpression(
            inputExpression = "abc÷%",
            expectedMessage = "Expected expression, got '%'"
        )
        testParseInvalidExpression(
            inputExpression = "+÷abc",
            expectedMessage = "Expected expression, got '÷'"
        )
        testParseInvalidExpression(
            inputExpression = "-7^*2",
            expectedMessage = "Expected expression, got '*'"
        )
        testParseInvalidExpression(
            inputExpression = "!",
            expectedMessage = "Expected expression, got '!'"
        )
        println("OK")

        print("\t\tTesting the operator priorities in valid expressions with parenthesis... ")
        testParseExpression(inputExpression = "(1+2)*3", expectedPostfixRecord = "1 2 + 3 *")
        testParseExpression(inputExpression = "(1-2)e", expectedPostfixRecord = "1 2 - e *")
        testParseExpression(
            inputExpression = "(((-7)^(+2)!!)^-7-+-4%)*(--4var÷another)",
            expectedPostfixRecord = "7 u- 2 ! ! ^ 7 u- ^ 4 % u- - 4 u- u- var * another ÷ *"
        )
        println("OK")

        print("\t\tTesting the operator priorities in invalid expressions with parenthesis... ")
        testParseInvalidExpression(
            inputExpression = "()",
            expectedMessage = "Expected expression, got ')'"
        )
        testParseInvalidExpression(
            inputExpression = "(1",
            expectedMessage = "Expected ')', got end of line"
        )
        testParseInvalidExpression(
            inputExpression = "(1+2*3",
            expectedMessage = "Expected ')', got end of line"
        )
        println("OK")

        println("\tTesting the operator priorities... OK")
    }

    @Test
    fun testFunctionCalls() {
        println("\tTesting the function calls...")

        print("\t\tTesting the function calls in valid expressions... ")
        testParseExpression(
            inputExpression = "procedure()",
            expectedPostfixRecord = "procedure invoke"
        )
        testParseExpression(
            inputExpression = "function()+1",
            expectedPostfixRecord = "function invoke 1 +"
        )
        testParseExpression(
            inputExpression = "function()2",
            expectedPostfixRecord = "function invoke 2 *"
        )
        testParseExpression(
            inputExpression = "sin(0)",
            expectedPostfixRecord = "sin 0 put_arg invoke"
        )
        testParseExpression(
            inputExpression = "sin(0) + function()2",
            expectedPostfixRecord = "sin 0 put_arg invoke function invoke 2 * +"
        )
        testParseExpression(
            inputExpression = "log(2, 1)",
            expectedPostfixRecord = "log 2 put_arg 1 put_arg invoke"
        )
        testParseExpression(
            inputExpression = "log(5, 1)7 + 5function(1, 2, 3, 4)",
            expectedPostfixRecord = "log 5 put_arg 1 put_arg invoke 7 * 5 function 1 " +
                    "put_arg 2 put_arg 3 put_arg 4 put_arg invoke * +"
        )
        testParseExpression(
            inputExpression = "log(27^2,8!)8+function(1+2)",
            expectedPostfixRecord = "log 27 2 ^ put_arg 8 ! put_arg invoke 8 * function " +
                    "1 2 + put_arg invoke +"
        )
        testParseExpression(
            inputExpression = "log((2+3)1,2)a+function(((1+2))(2-3))5",
            expectedPostfixRecord = "log 2 3 + 1 * put_arg 2 put_arg invoke a * function " +
                    "1 2 + 2 3 - * put_arg invoke 5 * +"
        )
        println("OK")

        print("\t\tTesting the function calls in invalid expressions... ")
        testParseInvalidExpression(
            inputExpression = "log(2+3",
            expectedMessage = "Expected ')', got end of line"
        )
        testParseInvalidExpression(
            inputExpression = "log(2+3,1",
            expectedMessage = "Expected ')', got end of line"
        )
        println("OK")

        println("\tTesting the function calls... OK")
    }

    @Test
    fun testMultipleNestedExpressions() {
        print("\tCrash test: processing a set of nested expressions... ")

        val nestingDepth = 2000
        val expression = "(".repeat(nestingDepth) + "1" + ")".repeat(nestingDepth)

        testParseExpression(expression, "1")

        println("OK")
    }

    private fun testParseExpression(inputExpression: String, expectedPostfixRecord: String) {
        val parser = Parser()

        val actualPostfixRecord = parser
            .parse(inputExpression)
            .joinToString(separator = " ") { it.lexem }

        assertEquals(
            "The actual postfix record is not equal to the expected one",
            expectedPostfixRecord, actualPostfixRecord
        )
    }

    private fun testParseInvalidExpression(inputExpression: String, expectedMessage: String) {
        val parser = Parser()

        val exception =
            assertThrows(expectedMessage, SyntaxException::class.java) {
                parser.parse(inputExpression)
            }

        assertEquals(
            "The actual exception message is not equal to the expected one",
            expectedMessage, exception.message
        )
    }


    companion object {
        @BeforeClass
        @JvmStatic
        fun start() {
            println("Testing methods of the Parser class...")
        }

        @AfterClass
        @JvmStatic
        fun finish() {
            println("Testing methods of the Parser class... OK")
            println()
        }
    }
}
