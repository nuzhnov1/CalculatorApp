package com.sunman.libcalculator

import com.sunman.libcalculator.internal.Token
import com.sunman.libcalculator.internal.Tokenizer
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.BeforeClass
import org.junit.Test

internal class TestTokenizer {

    @Test
    fun testState0() {
        print("\tTesting of the zero state of a tokenizer (the 'next' method)... ")

        testReadSingleToken(inputExpression = "", expectedToken = Token(Token.Kind.EOL, ""))
        testReadSingleToken(
            inputExpression = "√",
            expectedToken = Token(kind = Token.Kind.IDENT, lexem = "√")
        )
        testReadSingleToken(
            inputExpression = "π",
            expectedToken = Token(kind = Token.Kind.CONSTANT, lexem = "π")
        )
        testReadSingleToken(
            inputExpression = "÷",
            expectedToken = Token(kind = Token.Kind.OP, lexem = "÷")
        )
        testReadSingleToken(
            inputExpression = ",",
            expectedToken = Token(kind = Token.Kind.COMMA, lexem = ",")
        )
        testReadSingleToken(
            inputExpression = "(",
            expectedToken = Token(kind = Token.Kind.PARENTHESES, lexem = "(")
        )
        testReadSingleToken(
            inputExpression = " ",
            expectedToken = Token(kind = Token.Kind.SPACE, lexem = " ")
        )

        testReadInvalidTokens(inputExpression = "@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState1() {
        print("\tTesting of the 1st state of a tokenizer (the 'state1' method)... ")

        testReadSingleToken(
            inputExpression = "0110",
            expectedToken = Token(kind = Token.Kind.NUMBER, lexem = "0110")
        )

        testReadMultipleTokens(
            inputExpression = "0123456789π",
            expectedTokens = listOf(
                Token(kind = Token.Kind.NUMBER, lexem = "0123456789"),
                Token(kind = Token.Kind.CONSTANT, lexem = "π")
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
        testReadSingleToken(
            inputExpression = "1.1",
            expectedToken = Token(kind = Token.Kind.NUMBER, lexem = "1.1")
        )

        testReadMultipleTokens(
            inputExpression = ".1+",
            expectedTokens = listOf(
                Token(kind = Token.Kind.NUMBER, lexem = ".1"),
                Token(kind = Token.Kind.OP, lexem = "+")
            )
        )
        testReadMultipleTokens(
            inputExpression = "1.e",
            expectedTokens = listOf(
                Token(kind = Token.Kind.NUMBER, lexem = "1."),
                Token(kind = Token.Kind.CONSTANT, lexem = "e")
            )
        )

        testReadInvalidTokens(inputExpression = "1.@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState4() {
        print("\tTesting of the 4th state of a tokenizer (the 'state4' method)... ")

        testReadSingleToken(
            inputExpression = "e",
            expectedToken = Token(kind = Token.Kind.CONSTANT, lexem = "e")
        )
        testReadSingleToken(
            inputExpression = "exp",
            expectedToken = Token(kind = Token.Kind.IDENT, lexem = "exp")
        )
        testReadSingleToken(
            inputExpression = "eπ",
            expectedToken = Token(kind = Token.Kind.CONSTANT, lexem = "e")
        )

        testReadMultipleTokens(
            inputExpression = "eππ",
            expectedTokens = listOf(
                Token(kind = Token.Kind.CONSTANT, lexem = "e"),
                Token(kind = Token.Kind.CONSTANT, lexem = "π"),
                Token(kind = Token.Kind.CONSTANT, lexem = "π")
            )
        )

        testReadInvalidTokens(inputExpression = "e@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState5() {
        print("\tTesting of the 5th state of a tokenizer (the 'state5' method)... ")

        testReadSingleToken(
            inputExpression = "val",
            expectedToken = Token(kind = Token.Kind.IDENT, lexem = "val")
        )
        testReadSingleToken(
            inputExpression = "a1",
            expectedToken = Token(kind = Token.Kind.IDENT, lexem = "a1")
        )

        testReadMultipleTokens(
            inputExpression = "abe1+",
            expectedTokens = listOf(
                Token(Token.Kind.IDENT, "abe1"),
                Token(Token.Kind.OP, "+")
            )
        )
        testReadMultipleTokens(
            inputExpression = "a1(",
            expectedTokens = listOf(
                Token(kind = Token.Kind.IDENT, lexem = "a1"),
                Token(kind = Token.Kind.PARENTHESES, lexem = "(")
            )
        )

        testReadInvalidTokens(inputExpression = "e1@", expectedMessage = "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testExpression1() {
        print("\tTesting the reading expression: '+-+1÷-2*3e1'... ")
        testReadMultipleTokens(
            inputExpression = "+-+1÷-2*3e1",
            expectedTokens = listOf(
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.OP, lexem = "-"),
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.NUMBER, lexem = "1"),
                Token(kind = Token.Kind.OP, lexem = "÷"),
                Token(kind = Token.Kind.OP, lexem = "-"),
                Token(kind = Token.Kind.NUMBER, lexem = "2"),
                Token(kind = Token.Kind.OP, lexem = "*"),
                Token(kind = Token.Kind.NUMBER, lexem = "3"),
                Token(kind = Token.Kind.CONSTANT, lexem = "e"),
                Token(kind = Token.Kind.NUMBER, lexem = "1")
            )
        )
        println("OK")
    }

    @Test
    fun testExpression2() {
        print("\tTesting the reading expression: 'sin(1)πeπ'... ")
        testReadMultipleTokens(
            inputExpression = "sin(1)πeπ",
            expectedTokens = listOf(
                Token(kind = Token.Kind.IDENT, lexem = "sin"),
                Token(kind = Token.Kind.PARENTHESES, lexem = "("),
                Token(kind = Token.Kind.NUMBER, lexem = "1"),
                Token(kind = Token.Kind.PARENTHESES, lexem = ")"),
                Token(kind = Token.Kind.CONSTANT, lexem = "π"),
                Token(kind = Token.Kind.CONSTANT, lexem = "e"),
                Token(kind = Token.Kind.CONSTANT, lexem = "π")
            )
        )
        println("OK")
    }

    @Test
    fun testExpression3() {
        print("\tTesting the reading expression: '+--+1++++---3e1+-+3ea'... ")
        testReadMultipleTokens(
            inputExpression = "+--+1 ++++--- 3e1+-+3ea",
            expectedTokens = listOf(
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.OP, lexem = "-"),
                Token(kind = Token.Kind.OP, lexem = "-"),
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.NUMBER, lexem = "1"),
                Token(kind = Token.Kind.SPACE, lexem = " "),
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.OP, lexem = "-"),
                Token(kind = Token.Kind.OP, lexem = "-"),
                Token(kind = Token.Kind.OP, lexem = "-"),
                Token(kind = Token.Kind.SPACE, lexem = " "),
                Token(kind = Token.Kind.NUMBER, lexem = "3"),
                Token(kind = Token.Kind.CONSTANT, lexem = "e"),
                Token(kind = Token.Kind.NUMBER, lexem = "1"),
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.OP, lexem = "-"),
                Token(kind = Token.Kind.OP, lexem = "+"),
                Token(kind = Token.Kind.NUMBER, lexem = "3"),
                Token(kind = Token.Kind.IDENT, lexem = "ea")
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
            expectedToken = Token(kind = Token.Kind.IDENT, lexem = longIdentifier)
        )

        println("OK")
    }

    private fun testReadSingleToken(inputExpression: String, expectedToken: Token) {
        val tokenStream = Tokenizer(inputExpression)
        val token = tokenStream.next()

        tokenStream.use {
            assertEquals(
                "The actual token is not equal to the expected one",
                expectedToken, token
            )
        }
    }

    private fun testReadMultipleTokens(inputExpression: String, expectedTokens: List<Token>) {
        val tokenStream = Tokenizer(inputExpression)
        val tokens = mutableListOf<Token>()

        tokenStream.use {
            var token = it.next()

            while (token.kind != Token.Kind.EOL) {
                tokens.add(token)
                token = it.next()
            }
        }

        assertEquals(
            "The actual count of tokens is not equal to the expected one",
            expectedTokens.count(), tokens.count()
        )

        expectedTokens.zip(tokens).forEach {
            assertEquals(
                "The actual token is not equal to the expected one",
                it.first, it.second
            )
        }
    }

    private fun testReadInvalidTokens(inputExpression: String, expectedMessage: String) {
        val tokenStream = Tokenizer(inputExpression)

        tokenStream.use {
            val exception = assertThrows(SyntaxException::class.java) {
                var token = tokenStream.next()

                while (token.kind != Token.Kind.EOL) {
                    token = tokenStream.next()
                }
            }

            assertEquals(
                "The actual exception message is not equal to the expected one",
                expectedMessage, exception.message
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
