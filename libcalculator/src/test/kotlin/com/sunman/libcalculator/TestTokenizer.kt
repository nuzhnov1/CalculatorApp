package com.sunman.libcalculator

import com.sunman.libcalculator.parser.*
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.BeforeClass
import org.junit.Test

internal class TestTokenizer {

    @Test
    fun testState0() {
        print("\tTesting of the zero state of a tokenizer (the 'next' method)... ")

        testReadSingleToken(inputExpression = "", expectedToken = Token.EOL)
        testReadSingleToken(
            inputExpression = "$SQRT_CHAR",
            expectedToken = Token.Identifier(lexem = "$SQRT_CHAR")
        )
        testReadSingleToken(inputExpression = "$PI", expectedToken = Token.Constant(lexem = "$PI"))
        testReadSingleToken(inputExpression = "/", expectedToken = Token.Operator.Division)
        testReadSingleToken(inputExpression = ",", expectedToken = Token.Comma)
        testReadSingleToken(inputExpression = "(", expectedToken = Token.Parentheses.Open)
        testReadSingleToken(inputExpression = " ", expectedToken = Token.Space(lexem = " "))

        testReadInvalidTokens(inputExpression = "@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState1() {
        print("\tTesting of the 1st state of a tokenizer (the 'state1' method)... ")

        testReadSingleToken(inputExpression = "0110", expectedToken = Token.Number(lexem = "0110"))

        testReadMultipleTokens(
            inputExpression = "0123456789$PI",
            expectedTokens = listOf(
                Token.Number(lexem = "0123456789"),
                Token.Constant(lexem = "$PI")
            )
        )

        testReadInvalidTokens(inputExpression = "123@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState2() {
        print("\tTesting of the 2nd state of a tokenizer (the 'state2' method)... ")
        testReadInvalidTokens(inputExpression = ".@", expectedMessage = "Illegal character '.'")
        println("OK")
    }

    @Test
    fun testState3() {
        print("\tTesting of the 3rd state of a tokenizer (the 'state3' method)... ")
        testReadSingleToken(inputExpression = "1.1", expectedToken = Token.Number(lexem = "1.1"))

        testReadMultipleTokens(
            inputExpression = ".1+",
            expectedTokens = listOf(Token.Number(lexem = ".1"), Token.Operator.Plus)
        )
        testReadMultipleTokens(
            inputExpression = "1.$E",
            expectedTokens = listOf(Token.Number(lexem = "1."), Token.Constant(lexem = "$E"))
        )

        testReadInvalidTokens(inputExpression = "1.@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState4() {
        print("\tTesting of the 4th state of a tokenizer (the 'state4' method)... ")

        testReadSingleToken(inputExpression = "$E", expectedToken = Token.Constant(lexem = "$E"))
        testReadSingleToken(inputExpression = EXP, expectedToken = Token.Identifier(lexem = EXP))
        testReadSingleToken(inputExpression = "$E$PI", expectedToken = Token.Constant(lexem = "$E"))

        testReadMultipleTokens(
            inputExpression = "$E$PI$PI",
            expectedTokens = listOf(
                Token.Constant(lexem = "$E"),
                Token.Constant(lexem = "$PI"),
                Token.Constant(lexem = "$PI")
            )
        )

        testReadInvalidTokens(inputExpression = "$E@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState5() {
        print("\tTesting of the 5th state of a tokenizer (the 'state5' method)... ")

        testReadSingleToken(
            inputExpression = "val",
            expectedToken = Token.Identifier(lexem = "val")
        )
        testReadSingleToken(inputExpression = "a1", expectedToken = Token.Identifier(lexem = "a1"))

        testReadMultipleTokens(
            inputExpression = "ab${E}1+",
            expectedTokens = listOf(Token.Identifier(lexem = "abe1"), Token.Operator.Plus)
        )
        testReadMultipleTokens(
            inputExpression = "a1(",
            expectedTokens = listOf(Token.Identifier(lexem = "a1"), Token.Parentheses.Open)
        )

        testReadInvalidTokens(inputExpression = "${E}1@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testExpression1() {
        print("\tTesting the reading expression: '+-+1/-2*3${E}1'... ")
        testReadMultipleTokens(
            inputExpression = "+-+1/-2*3${E}1",
            expectedTokens = listOf(
                Token.Operator.Plus,
                Token.Operator.Minus,
                Token.Operator.Plus,
                Token.Number(lexem = "1"),
                Token.Operator.Division,
                Token.Operator.Minus,
                Token.Number(lexem = "2"),
                Token.Operator.Multiplication,
                Token.Number(lexem = "3"),
                Token.Constant(lexem = "$E"),
                Token.Number(lexem = "1")
            )
        )
        println("OK")
    }

    @Test
    fun testExpression2() {
        print("\tTesting the reading expression: '$SIN(1)$PI$E$PI'... ")
        testReadMultipleTokens(
            inputExpression = "$SIN(1)$PI$E$PI",
            expectedTokens = listOf(
                Token.Identifier(lexem = SIN),
                Token.Parentheses.Open,
                Token.Number(lexem = "1"),
                Token.Parentheses.Close,
                Token.Constant(lexem = "$PI"),
                Token.Constant(lexem = "$E"),
                Token.Constant(lexem = "$PI")
            )
        )
        println("OK")
    }

    @Test
    fun testExpression3() {
        print("\tTesting the reading expression: '+--+1 ++++--- 3${E}1 +-+ 3${E}a'... ")
        testReadMultipleTokens(
            inputExpression = "+--+1 ++++--- 3${E}1 +-+ 3${E}a",
            expectedTokens = listOf(
                Token.Operator.Plus,
                Token.Operator.Minus,
                Token.Operator.Minus,
                Token.Operator.Plus,
                Token.Number(lexem = "1"),
                Token.Space(lexem = " "),
                Token.Operator.Plus,
                Token.Operator.Plus,
                Token.Operator.Plus,
                Token.Operator.Plus,
                Token.Operator.Minus,
                Token.Operator.Minus,
                Token.Operator.Minus,
                Token.Space(lexem = " "),
                Token.Number(lexem = "3"),
                Token.Constant(lexem = "$E"),
                Token.Number(lexem = "1"),
                Token.Space(lexem = " "),
                Token.Operator.Plus,
                Token.Operator.Minus,
                Token.Operator.Plus,
                Token.Space(lexem = " "),
                Token.Number(lexem = "3"),
                Token.Identifier(lexem = "${E}a")
            )
        )
        println("OK")
    }

    @Test
    fun testLongIdentifier() {
        print("\tTesting the reading a long identifier... ")

        val longIdentifier = "a".repeat(10000)
        testReadSingleToken(
            inputExpression = longIdentifier,
            expectedToken = Token.Identifier(lexem = longIdentifier)
        )

        println("OK")
    }

    private fun testReadSingleToken(inputExpression: String, expectedToken: Token) {
        val tokenStream = Tokenizer(inputExpression)
        val token = tokenStream.next()

        tokenStream.use {
            assertEquals(
                /* message = */ "The actual token is not equal to the expected one",
                /* expected = */ expectedToken, /* actual = */ token
            )
        }
    }

    private fun testReadMultipleTokens(inputExpression: String, expectedTokens: List<Token>) {
        val tokenStream = Tokenizer(inputExpression)
        val tokens = mutableListOf<Token>()

        tokenStream.use {
            var token = it.next()

            while (token != Token.EOL) {
                tokens.add(token)
                token = it.next()
            }
        }

        assertEquals(
            /* message = */ "The actual count of tokens is not equal to the expected one",
            /* expected = */ expectedTokens.count(), /* actual = */ tokens.count()
        )

        expectedTokens.zip(tokens).forEach {
            assertEquals(
                /* message = */ "The actual token is not equal to the expected one",
                /* expected = */ it.first, /* actual = */ it.second
            )
        }
    }

    private fun testReadInvalidTokens(inputExpression: String, expectedMessage: String) {
        val tokenStream = Tokenizer(inputExpression)

        tokenStream.use {
            val exception = assertThrows(SyntaxException::class.java) {
                var token = tokenStream.next()

                while (token != Token.EOL) {
                    token = tokenStream.next()
                }
            }

            assertEquals(
                /* message = */ "The actual exception message is not equal to the expected one",
                /* expected = */ expectedMessage, /* actual = */ exception.message
            )
        }
    }


    companion object {
        @BeforeClass
        @JvmStatic
        fun start() {
            println("Testing methods of the Tokenizer class...")
        }

        @AfterClass
        @JvmStatic
        fun finish() {
            println("Testing methods of the Tokenizer class... OK")
            println()
        }
    }
}
