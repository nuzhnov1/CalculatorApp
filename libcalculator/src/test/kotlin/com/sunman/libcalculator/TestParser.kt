package com.sunman.libcalculator

import com.sunman.libcalculator.internal.Parser
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test

internal class TestParser {

    @Test
    fun testParseSimpleExpressions() {
        println("\tTesting the parsing of simple expressions, such as: '1 + 1', '2 * 2', etc...")

        print("\t\tTesting the parsing of valid simple expressions... ")
        testParseStatement("", "")
        testParseStatement("5", "5")
        testParseStatement("e", "e")
        testParseStatement("+1.0", "1.0")
        testParseStatement("-5", "5 u-")
        testParseStatement("(1)", "1")
        testParseStatement("1-1", "1 1 -")
        testParseStatement("e+π", "e π +")
        testParseStatement("1.0-2", "1.0 2 -")
        testParseStatement("2.0*3.2", "2.0 3.2 *")
        testParseStatement(".1÷1.", ".1 1. ÷")
        testParseStatement("1.0÷a", "1.0 a ÷")
        testParseStatement("ee", "e e *")
        testParseStatement("eπ", "e π *")
        testParseStatement("ππ", "π π *")
        testParseStatement("24e", "24 e *")
        testParseStatement("24exp", "24 exp *")
        testParseStatement("(23)(24)", "23 24 *")
        testParseStatement("10!π", "10 ! π *")
        testParseStatement("4.1^1.1", "4.1 1.1 ^")
        testParseStatement("5!", "5 !")
        testParseStatement("54%", "54 %")
        println("OK")

        print("\t\tTesting the parsing of invalid simple expressions... ")
        testParseInvalidStatement("*1", "Expected expression, got '*'")
        testParseInvalidStatement(")a", "Expected expression, got ')'")
        testParseInvalidStatement(",a", "Expected expression, got ','")
        println("OK")

        println("\tTesting the parsing of simple expressions, such as: '1 + 1', '2 * 2', etc... OK")
    }

    @Test
    fun testAssociationOfOperators() {
        print("\tTesting the association of operators in expressions... ")
        testParseStatement("9+1-2", "9 1 + 2 -")
        testParseStatement("1.2*8÷2.1", "1.2 8 * 2.1 ÷")
        testParseStatement("+-+++1.2", "1.2 u-")
        testParseStatement("2^3^9", "2 3 9 ^ ^")
        testParseStatement("2!!%!", "2 ! ! % !")
        println("OK")
    }

    @Test
    fun testPriorityOfOperators() {
        println("\tTesting the operator priorities...")

        print("\t\tTesting the operator priorities in valid expressions without parenthesis... ")
        testParseStatement("1+2*3", "1 2 3 * +")
        testParseStatement("1-2e", "1 2 e * -")
        testParseStatement("1-+-2e", "1 2 u- e * -")
        testParseStatement(
            "--+1.0+-+-+-++-1.3",
            "1.0 u- u- 1.3 u- u- u- u- +"
        )
        testParseStatement("--1.0*+-1.0", "1.0 u- u- 1.0 u- *")
        testParseStatement("-7^+-2!", "7 2 ! u- ^ u-")
        testParseStatement(
            "-7^+-2!!^-7-+-4%*--4var÷another",
            "7 2 ! ! 7 u- ^ u- ^ u- 4 % u- 4 u- u- * var * another ÷ -"
        )
        println("OK")

        print("\t\tTesting the operator priorities in invalid expressions without parenthesis... ")
        testParseInvalidStatement("-7+", "Expected expression, got end of line")
        testParseInvalidStatement("abc÷%", "Expected expression, got '%'")
        testParseInvalidStatement("+÷abc", "Expected expression, got '÷'")
        testParseInvalidStatement("-7^*2", "Expected expression, got '*'")
        testParseInvalidStatement("!", "Expected expression, got '!'")
        println("OK")

        print("\t\tTesting the operator priorities in valid expressions with parenthesis... ")
        testParseStatement("(1+2)*3", "1 2 + 3 *")
        testParseStatement("(1-2)e", "1 2 - e *")
        testParseStatement(
            "(((-7)^(+2)!!)^-7-+-4%)*(--4var÷another)",
            "7 u- 2 ! ! ^ 7 u- ^ 4 % u- - 4 u- u- var * another ÷ *"
        )
        println("OK")

        print("\t\tTesting the operator priorities in invalid expressions with parenthesis... ")
        testParseInvalidStatement("()", "Expected expression, got ')'")
        testParseInvalidStatement("(1", "Expected ')', got end of line")
        testParseInvalidStatement("(1+2*3", "Expected ')', got end of line")
        println("OK")

        println("\tTesting the operator priorities... OK")
    }

    @Test
    fun testFunctionCalls() {
        println("\tTesting the function calls...")

        print("\t\tTesting the function calls in valid expressions... ")
        testParseStatement("procedure()", "procedure invoke")
        testParseStatement("function()+1", "function invoke 1 +")
        testParseStatement("function()2", "function invoke 2 *")
        testParseStatement("sin(0)", "sin 0 put_arg invoke")
        testParseStatement(
            "sin(0)+function()2",
            "sin 0 put_arg invoke function invoke 2 * +"
        )
        testParseStatement("log(2,1)", "log 2 put_arg 1 put_arg invoke")
        testParseStatement(
            "log(5,1)7+5function(1,2,3,4)",
            "log 5 put_arg 1 put_arg invoke 7 * 5 function 1 put_arg 2 put_arg 3 put_arg 4 put_arg invoke * +"
        )
        testParseStatement(
            "log(27^2,8!)8+function(1+2)",
            "log 27 2 ^ put_arg 8 ! put_arg invoke 8 * function 1 2 + put_arg invoke +"
        )
        testParseStatement(
            "log((2+3)1,2)a+function(((1+2))(2-3))5",
            "log 2 3 + 1 * put_arg 2 put_arg invoke a * function 1 2 + 2 3 - * put_arg invoke 5 * +"
        )
        println("OK")

        print("\t\tTesting the function calls in invalid expressions... ")
        testParseInvalidStatement("log(2+3", "Expected ')', got end of line")
        testParseInvalidStatement("log(2+3,1", "Expected ')', got end of line")
        println("OK")

        println("\tTesting the function calls... OK")
    }

    @Test
    @Ignore("Long execution")
    fun testMultipleNestedExpressions() {
        print("\tCrash test: processing a set of nested expressions... ")

        val nestingDepth = 2000
        val expression = "(".repeat(nestingDepth) + "1" + ")".repeat(nestingDepth)

        testParseStatement(expression, "1")

        println("OK")
    }

    private fun testParseStatement(inputString: String, expectedPostfixRecord: String) {
        val parser = Parser()

        val actualPostfixRecord = parser
            .parse(inputString)
            .joinToString(separator = " ") { it.lexem }

        assertEquals(
            "The actual postfix record is not equal to the expected one",
            expectedPostfixRecord, actualPostfixRecord
        )
    }

    private fun testParseInvalidStatement(inputString: String, expectedMessage: String) {
        val parser = Parser()

        val exception =
            assertThrows(expectedMessage, SyntaxException::class.java) { parser.parse(inputString) }

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
