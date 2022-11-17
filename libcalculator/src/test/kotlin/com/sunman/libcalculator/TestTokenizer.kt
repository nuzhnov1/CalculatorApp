package com.sunman.libcalculator

import com.sunman.libcalculator.internal.Token
import com.sunman.libcalculator.internal.Tokenizer
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test

internal class TestTokenizer {

    @Test
    fun testState0() {
        print("\tTesting of the zero state of a tokenizer (the 'next' method)... ")

        testReadSingleToken("", Token(Token.Kind.EOL, ""))
        testReadSingleToken("√", Token(Token.Kind.IDENT, "√"))
        testReadSingleToken("π", Token(Token.Kind.CONSTANT, "π"))
        testReadSingleToken("÷", Token(Token.Kind.OP, "÷"))
        testReadSingleToken(",", Token(Token.Kind.COMMA, ","))
        testReadSingleToken("(", Token(Token.Kind.PARENTHESES, "("))

        testReadInvalidTokens("@", "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState1() {
        print("\tTesting of the 1st state of a tokenizer (the 'state1' method)... ")

        testReadSingleToken("0110", Token(Token.Kind.NUMBER, "0110"))

        testReadMultipleTokens(
            "0123456789π", listOf(
                Token(Token.Kind.NUMBER, "0123456789"),
                Token(Token.Kind.CONSTANT, "π")
            )
        )

        testReadInvalidTokens("123@", "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState2() {
        print("\tTesting of the 2nd state of a tokenizer (the 'state2' method)... ")
        testReadInvalidTokens(".@", "Illegal character '.'")
        println("OK")
    }

    @Test
    fun testState3() {
        print("\tTesting of the 3rd state of a tokenizer (the 'state3' method)... ")
        testReadSingleToken("1.1", Token(Token.Kind.NUMBER, "1.1"))

        testReadMultipleTokens(
            ".1+", listOf(
                Token(Token.Kind.NUMBER, ".1"),
                Token(Token.Kind.OP, "+")
            )
        )
        testReadMultipleTokens(
            "1.e", listOf(
                Token(Token.Kind.NUMBER, "1."),
                Token(Token.Kind.CONSTANT, "e")
            )
        )

        testReadInvalidTokens("1.@", "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState4() {
        print("\tTesting of the 4th state of a tokenizer (the 'state4' method)... ")

        testReadSingleToken("e", Token(Token.Kind.CONSTANT, "e"))
        testReadSingleToken("exp", Token(Token.Kind.IDENT, "exp"))
        testReadSingleToken("eπ", Token(Token.Kind.CONSTANT, "e"))

        testReadMultipleTokens(
            "eππ", listOf(
                Token(Token.Kind.CONSTANT, "e"),
                Token(Token.Kind.CONSTANT, "π"),
                Token(Token.Kind.CONSTANT, "π")
            )
        )

        testReadInvalidTokens("e@", "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testState5() {
        print("\tTesting of the 5th state of a tokenizer (the 'state5' method)... ")

        testReadSingleToken("val", Token(Token.Kind.IDENT, "val"))
        testReadSingleToken("a1", Token(Token.Kind.IDENT, "a1"))

        testReadMultipleTokens(
            "abe1+", listOf(
                Token(Token.Kind.IDENT, "abe1"),
                Token(Token.Kind.OP, "+")
            )
        )
        testReadMultipleTokens(
            "a1(", listOf(
                Token(Token.Kind.IDENT, "a1"),
                Token(Token.Kind.PARENTHESES, "(")
            )
        )

        testReadInvalidTokens("e1@", "Illegal character '@'")

        println("OK")
    }

    @Test
    fun testExpression1() {
        print("\tTesting the reading expression: '+-+1÷-2*3e1'... ")
        testReadMultipleTokens(
            "+-+1÷-2*3e1", listOf(
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.NUMBER, "1"),
                Token(Token.Kind.OP, "÷"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.NUMBER, "2"),
                Token(Token.Kind.OP, "*"),
                Token(Token.Kind.NUMBER, "3"),
                Token(Token.Kind.CONSTANT, "e"),
                Token(Token.Kind.NUMBER, "1")
            )
        )
        println("OK")
    }

    @Test
    fun testExpression2() {
        print("\tTesting the reading expression: 'sin(1)πeπ'... ")
        testReadMultipleTokens(
            "sin(1)πeπ", listOf(
                Token(Token.Kind.IDENT, "sin"),
                Token(Token.Kind.PARENTHESES, "("),
                Token(Token.Kind.NUMBER, "1"),
                Token(Token.Kind.PARENTHESES, ")"),
                Token(Token.Kind.CONSTANT, "π"),
                Token(Token.Kind.CONSTANT, "e"),
                Token(Token.Kind.CONSTANT, "π")
            )
        )
        println("OK")
    }

    @Test
    fun testExpression3() {
        print("\tTesting the reading expression: '+--+1++++---3e1+-+3ea'... ")
        testReadMultipleTokens(
            "+--+1++++---3e1+-+3ea", listOf(
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.NUMBER, "1"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.NUMBER, "3"),
                Token(Token.Kind.CONSTANT, "e"),
                Token(Token.Kind.NUMBER, "1"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.NUMBER, "3"),
                Token(Token.Kind.IDENT, "ea")
            )
        )
        println("OK")
    }

    @Test
    @Ignore("Long execution")
    fun testLongIdentifier() {
        print("\tTesting the reading a long identifier... ")

        val longIdentifier = "a".repeat(10000)
        testReadSingleToken(longIdentifier, Token(Token.Kind.IDENT, longIdentifier))

        println("OK")
    }

    private fun testReadSingleToken(inputString: String, expectedToken: Token) {
        val tokenStream = Tokenizer(inputString)
        val token = tokenStream.next()

        tokenStream.use {
            assertEquals(
                "The actual token is not equal to the expected one",
                expectedToken, token
            )
        }
    }

    private fun testReadMultipleTokens(inputString: String, expectedTokens: List<Token>) {
        val tokenStream = Tokenizer(inputString)
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

    private fun testReadInvalidTokens(inputString: String, expectedMessage: String) {
        val tokenStream = Tokenizer(inputString)

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
