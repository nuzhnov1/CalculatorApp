package com.sunman.libcalculator

import com.sunman.libcalculator.internal.tokenizer.CR
import com.sunman.libcalculator.internal.tokenizer.Token
import com.sunman.libcalculator.internal.tokenizer.Tokenizer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.StringReader

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Testing methods of the Tokenizer class")
internal class TestTokenizer {
    @BeforeAll
    fun start() {
        println("Testing methods of the Tokenizer class...")
    }

    @Test
    @DisplayName(
        """
        Testing of the zero state of a finite state machine (the 'next' method)
        """
    )
    fun testState0() {
        print("\tTesting of the zero state of a finite state machine (the 'next' method)... ")

        testReadSingleToken("", Token(Token.Kind.EOF, ""))
        testReadSingleToken("\n", Token(Token.Kind.EOL, "\n"))
        testReadSingleToken("$CR", Token(Token.Kind.EOL, "\n"))
        testReadSingleToken("*", Token(Token.Kind.OP, "*"))
        testReadSingleToken(",", Token(Token.Kind.COMMA, ","))
        testReadSingleToken("=", Token(Token.Kind.ASSIGN, "="))
        testReadSingleToken("(", Token(Token.Kind.PARENTHESES, "("))

        testReadInvalidTokens("\n\n@", "Illegal character '@'")

        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing of the 1st state of a finite state machine (the 'state1' method).
        Testing reading integer numbers
        """
    )
    fun testState1() {
        print("\tTesting of the 1st state of a finite state machine (the 'state1' method)... ")

        testReadSingleToken("0110", Token(Token.Kind.NUMBER, "0110"))

        testReadMultipleTokens(
            "0123456789$CR\n", listOf(
                Token(Token.Kind.NUMBER, "0123456789"),
                Token(Token.Kind.EOL, "\n")
            )
        )

        testReadInvalidTokens("123@", "Illegal character '@'")

        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing of the 2nd state of a finite state machine (the 'state2' method)
        """
    )
    fun testState2() {
        print("\tTesting of the 2nd state of a finite state machine (the 'state2' method)... ")
        testReadInvalidTokens(".@", "Illegal character '.'")
        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing of the 3rd state of a finite state machine (the 'state3' method).
        Testing reading float numbers
        """
    )
    fun testState3() {
        print("\tTesting of the 3rd state of a finite state machine (the 'state3' method)... ")
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
                Token(Token.Kind.IDENT, "e")
            )
        )

        testReadInvalidTokens("1.@", "Illegal character '@'")

        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing of the 4th state of a finite state machine (the 'state4' method).
        Testing reading identifiers
        """
    )
    fun testState4() {
        print("\tTesting of the 4th state of a finite state machine (the 'state4' method)... ")

        testReadSingleToken("_1", Token(Token.Kind.IDENT, "_1"))
        testReadSingleToken("val", Token(Token.Kind.IDENT, "val"))
        testReadSingleToken("e1", Token(Token.Kind.IDENT, "e1"))

        testReadMultipleTokens(
            "abe1+", listOf(
                Token(Token.Kind.IDENT, "abe1"),
                Token(Token.Kind.OP, "+")
            )
        )
        testReadMultipleTokens(
            "a1 \t\t\t \t\t \t", listOf(
                Token(Token.Kind.IDENT, "a1"),
                Token(Token.Kind.SPACES, " \t\t\t \t\t \t"),
            )
        )

        testReadInvalidTokens("e1.", "Illegal character '.'")

        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing of the 5th state of a finite state machine (the 'state5' method)
        """
    )
    fun testState5() {
        print("\tTesting of the 5th state of a finite state machine (the 'state5' method)... ")

        testReadSingleToken("/", Token(Token.Kind.OP, "/"))

        testReadMultipleTokens(
            "/1", listOf(
                Token(Token.Kind.OP, "/"),
                Token(Token.Kind.NUMBER, "1"),
            )
        )
        testReadMultipleTokens(
            "/ \t", listOf(
                Token(Token.Kind.OP, "/"),
                Token(Token.Kind.SPACES, " \t"),
            )
        )

        testReadInvalidTokens("/.", "Illegal character '.'")

        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing of the 6th state of a finite state machine (the 'state6' method).
        Testing reading commands
        """
    )
    fun testState6() {
        print("\tTesting of the 6th state of a finite state machine (the 'state6' method)... ")

        testReadSingleToken("/exit", Token(Token.Kind.COMMAND, "/exit"))
        testReadSingleToken("/help", Token(Token.Kind.COMMAND, "/help"))
        testReadSingleToken("/help1", Token(Token.Kind.COMMAND, "/help1"))

        testReadMultipleTokens(
            "/exit+", listOf(
                Token(Token.Kind.COMMAND, "/exit"),
                Token(Token.Kind.OP, "+"),
            )
        )

        testReadInvalidTokens("/exit#", "Illegal character '#'")

        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing of the 7th state of a finite state machine (the 'state7' method).
        Testing reading spaces
        """
    )
    fun testState7() {
        print("\tTesting of the 7th state of a finite state machine (the 'state7' method)... ")

        testReadSingleToken("   ", Token(Token.Kind.SPACES, "   "))

        testReadMultipleTokens(
            " /", listOf(
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.OP, "/"),
            )
        )

        testReadInvalidTokens("   $", "Illegal character '$'")

        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing the reading expression: '+-+1  / -2 * 3e1\n'
        """
    )
    fun testExpression1() {
        print("\tTesting the reading expression: '+-+1  / -2 * 3e1\\n'... ")
        testReadMultipleTokens(
            "+-+1  / -2 * 3e1\n", listOf(
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.NUMBER, "1"),
                Token(Token.Kind.SPACES, "  "),
                Token(Token.Kind.OP, "/"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.NUMBER, "2"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.OP, "*"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.NUMBER, "3"),
                Token(Token.Kind.IDENT, "e1"),
                Token(Token.Kind.EOL, "\n")
            )
        )
        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing the reading expression: '+-+1  /exit \t -2 / 3E-a1b(1)2\n'
        """
    )
    fun testExpression2() {
        print("\tTesting the reading expression: '+-+1  /exit \\t -2 / 3E-a1b(1)2\\n'... ")
        testReadMultipleTokens(
            "+-+1  /exit \t -2 / 3E-a1b(1)2\n", listOf(
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.NUMBER, "1"),
                Token(Token.Kind.SPACES, "  "),
                Token(Token.Kind.COMMAND, "/exit"),
                Token(Token.Kind.SPACES, " \t "),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.NUMBER, "2"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.OP, "/"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.NUMBER, "3"),
                Token(Token.Kind.IDENT, "E"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.IDENT, "a1b"),
                Token(Token.Kind.PARENTHESES, "("),
                Token(Token.Kind.NUMBER, "1"),
                Token(Token.Kind.PARENTHESES, ")"),
                Token(Token.Kind.NUMBER, "2"),
                Token(Token.Kind.EOL, "\n")
            )
        )
        println("OK")
    }

    @Test
    @DisplayName(
        """
        Testing the reading expression: '+--+1 ++++ ---3e1 +-+ 3eA'
        """
    )
    fun testExpression3() {
        print("\tTesting the reading expression: '+--+1 ++++ ---3e1 +-+ 3eA'... ")
        testReadMultipleTokens(
            "+--+1 ++++ ---3e1 +-+ 3eA", listOf(
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.NUMBER, "1"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.NUMBER, "3"),
                Token(Token.Kind.IDENT, "e1"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.OP, "-"),
                Token(Token.Kind.OP, "+"),
                Token(Token.Kind.SPACES, " "),
                Token(Token.Kind.NUMBER, "3"),
                Token(Token.Kind.IDENT, "eA"),
            )
        )
        println("OK")
    }

    @Test
    @Disabled
    @DisplayName(
        """
        Testing the reading a long identifier
        """
    )
    fun testLongIdentifier() {
        print("\tTesting the reading a long identifier... ")

        val longIdentifier = "a".repeat(10000)
        testReadSingleToken(longIdentifier, Token(Token.Kind.IDENT, longIdentifier))

        println("OK")
    }

    @AfterAll
    fun finish() {
        println("Testing methods of the Tokenizer class... OK")
        println()
    }


    private fun testReadSingleToken(inputString: String, expectedToken: Token) {
        val tokenStream = Tokenizer(StringReader(inputString))
        val token = tokenStream.next()

        tokenStream.use {
            assertEquals(
                expectedToken, token,
                "The actual token is not equal to the expected one"
            )
        }
    }

    private fun testReadMultipleTokens(inputString: String, expectedTokens: List<Token>) {
        val tokenStream = Tokenizer(StringReader(inputString))
        val tokens = mutableListOf<Token>()

        tokenStream.use { it.forEach { token -> tokens.add(token) } }

        assertEquals(
            expectedTokens.count(), tokens.count(),
            "The actual count of tokens is not equal to the expected one"
        )

        expectedTokens.zip(tokens).forEach {
            assertEquals(
                it.first, it.second,
                "The actual token is not equal to the expected one"
            )
        }
    }

    private fun testReadInvalidTokens(inputString: String, expectedMessage: String) {
        val tokenStream = Tokenizer(StringReader(inputString))

        tokenStream.use {
            val exception = assertThrows<SyntaxException> { tokenStream.forEach { _ -> } }

            assertEquals(
                expectedMessage, exception.message,
                "The actual exception message is not equal to the expected one"
            )
        }
    }
}
