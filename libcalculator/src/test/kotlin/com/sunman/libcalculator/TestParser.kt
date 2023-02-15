package com.sunman.libcalculator

import com.sunman.libcalculator.parser.*
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.BeforeClass
import org.junit.Test

internal class TestParser {

    @Test
    fun testParseOfSimpleExpressions() {
        println(
            "\tTesting the parsing of simple expressions, such as: '1 + 1', '2 * 2', etc..."
        )

        print("\t\tTesting the parsing of valid simple expressions... ")
        testParseExpression(inputExpression = "", expectedPostfixRecord = "")
        testParseExpression(inputExpression = " 5   ", expectedPostfixRecord = "5")
        testParseExpression(inputExpression = " $E ", expectedPostfixRecord = "$E")
        testParseExpression(inputExpression = "+1.0", expectedPostfixRecord = "1.0")
        testParseExpression(inputExpression = "-5", expectedPostfixRecord = "5 u-")
        testParseExpression(inputExpression = "(1 )", expectedPostfixRecord = "1")
        testParseExpression(inputExpression = "1-1", expectedPostfixRecord = "1 1 -")
        testParseExpression(inputExpression = "$E + $PI", expectedPostfixRecord = "$E $PI +")
        testParseExpression(inputExpression = "1.0 - 2", expectedPostfixRecord = "1.0 2 -")
        testParseExpression(inputExpression = "2.0*3.2", expectedPostfixRecord = "2.0 3.2 *")
        testParseExpression(inputExpression = ".1/1.", expectedPostfixRecord = ".1 1. /")
        testParseExpression(inputExpression = "1.0/a", expectedPostfixRecord = "1.0 a /")
        testParseExpression(inputExpression = "$E$E", expectedPostfixRecord = "$E $E *")
        testParseExpression(inputExpression = "$E$PI", expectedPostfixRecord = "$E $PI *")
        testParseExpression(inputExpression = "$PI $PI", expectedPostfixRecord = "$PI $PI *")
        testParseExpression(inputExpression = "24 $E", expectedPostfixRecord = "24 $E *")
        testParseExpression(inputExpression = "24$EXP", expectedPostfixRecord = "24 $EXP *")
        testParseExpression(inputExpression = "(23)(24)", expectedPostfixRecord = "23 24 *")
        testParseExpression(inputExpression = "10 ! $PI", expectedPostfixRecord = "10 ! $PI *")
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
        testParseExpression(inputExpression = "1.2*8/2.1", expectedPostfixRecord = "1.2 8 * 2.1 /")
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
        testParseExpression(inputExpression = "1-2$E", expectedPostfixRecord = "1 2 $E * -")
        testParseExpression(inputExpression = "1-+-2$E", expectedPostfixRecord = "1 2 u- $E * -")
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
            inputExpression = "-7^+-2!!^-7 - +-4% * --4var / another",
            expectedPostfixRecord = "7 2 ! ! 7 u- ^ u- ^ u- 4 % u- 4 u- u- * var * another / -"
        )
        println("OK")

        print("\t\tTesting the operator priorities in invalid expressions without parenthesis... ")
        testParseInvalidExpression(
            inputExpression = "-7+",
            expectedMessage = "Expected expression, got end of line"
        )
        testParseInvalidExpression(
            inputExpression = "abc/%",
            expectedMessage = "Expected expression, got '%'"
        )
        testParseInvalidExpression(
            inputExpression = "+/abc",
            expectedMessage = "Expected expression, got '/'"
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
        testParseExpression(inputExpression = "(1-2)$E", expectedPostfixRecord = "1 2 - $E *")
        testParseExpression(
            inputExpression = "(((-7)^(+2)!!)^-7-+-4%)*(--4var/another)",
            expectedPostfixRecord = "7 u- 2 ! ! ^ 7 u- ^ 4 % u- - 4 u- u- var * another / *"
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
            expectedPostfixRecord = "procedure $INVOKE"
        )
        testParseExpression(
            inputExpression = "function()+1",
            expectedPostfixRecord = "function $INVOKE 1 +"
        )
        testParseExpression(
            inputExpression = "function()2",
            expectedPostfixRecord = "function $INVOKE 2 *"
        )
        testParseExpression(
            inputExpression = "$SIN(0)",
            expectedPostfixRecord = "$SIN 0 $PUT_ARG $INVOKE"
        )
        testParseExpression(
            inputExpression = "$SIN(0) + function()2",
            expectedPostfixRecord = "$SIN 0 $PUT_ARG $INVOKE function $INVOKE 2 * +"
        )
        testParseExpression(
            inputExpression = "$LOG(2, 1)",
            expectedPostfixRecord = "$LOG 2 $PUT_ARG 1 $PUT_ARG $INVOKE"
        )
        testParseExpression(
            inputExpression = "$LOG(5, 1)7 + 5function(1, 2, 3, 4)",
            expectedPostfixRecord = "$LOG 5 $PUT_ARG 1 $PUT_ARG $INVOKE 7 * 5 " +
                    "function 1 $PUT_ARG 2 $PUT_ARG 3 $PUT_ARG 4 $PUT_ARG $INVOKE * +"
        )
        testParseExpression(
            inputExpression = "$LOG(27^2,8!)8+function(1+2)",
            expectedPostfixRecord = "$LOG 27 2 ^ $PUT_ARG 8 ! $PUT_ARG $INVOKE 8 " +
                    "* function 1 2 + $PUT_ARG $INVOKE +"
        )
        testParseExpression(
            inputExpression = "$LOG((2+3)1,2)a+function(((1+2))(2-3))5",
            expectedPostfixRecord = "$LOG 2 3 + 1 * $PUT_ARG 2 $PUT_ARG $INVOKE a " +
                    "* function 1 2 + 2 3 - * $PUT_ARG $INVOKE 5 * +"
        )
        println("OK")

        print("\t\tTesting the function calls in invalid expressions... ")
        testParseInvalidExpression(
            inputExpression = "$LOG(2+3",
            expectedMessage = "Expected ')', got end of line"
        )
        testParseInvalidExpression(
            inputExpression = "$LOG(2+3,1",
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

        testParseExpression(inputExpression = expression, expectedPostfixRecord = "1")

        println("OK")
    }

    private fun testParseExpression(inputExpression: String, expectedPostfixRecord: String) {
        val parser = Parser()

        val actualPostfixRecord = parser
            .parse(inputExpression)
            .joinToString(separator = " ") { it.toTestString() }

        assertEquals(
            /* message = */ "The actual postfix record is not equal to the expected one",
            /* expected = */ expectedPostfixRecord, /* actual = */ actualPostfixRecord
        )
    }

    private fun testParseInvalidExpression(inputExpression: String, expectedMessage: String) {
        val parser = Parser()

        val exception =
            assertThrows(expectedMessage, SyntaxException::class.java) { 
                parser.parse(inputExpression)
            }

        assertEquals(
            /* message = */ "The actual exception message is not equal to the expected one",
            /* expected = */ expectedMessage, /* actual = */ exception.message
        )
    }
    
    private fun PostfixToken.toTestString() = when (this) {
        is PostfixToken.Number -> lexem
        is PostfixToken.Identifier -> lexem
        is PostfixToken.Constant -> lexem
        
        PostfixToken.Operation.Negation -> "u-"
        PostfixToken.Operation.Addition -> "+"
        PostfixToken.Operation.Subtraction -> "-"
        PostfixToken.Operation.Multiplication -> "*"
        PostfixToken.Operation.Division -> "/"
        PostfixToken.Operation.Power -> "^"
        PostfixToken.Operation.Factorial -> "!"
        PostfixToken.Operation.Percent -> "%"
        
        PostfixToken.Action.Invoke -> INVOKE
        PostfixToken.Action.PutArgument -> PUT_ARG
    }


    companion object {
        const val INVOKE = "INVOKE"
        const val PUT_ARG = "PUT_ARG"
        
        
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
