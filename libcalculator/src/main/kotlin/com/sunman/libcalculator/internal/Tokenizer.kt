package com.sunman.libcalculator.internal

import com.sunman.libcalculator.SyntaxException
import java.io.Closeable

/**
 * Reads characters from the [String], converts them into tokens
 * and allows to return the read token to the input by placing them on the LIFO stack.
 * Each token is an instance of the [Token] class.
 *
 * @param expression where will the characters be read from.
 */
internal class Tokenizer(expression: String) : Closeable {

    private val charStream = CharStream(expression)


    /**
     * Reads a single token.
     *
     * @throws SyntaxException If an illegal character occurs.
     * @throws java.io.IOException If an I/O error occurs.
     */
    fun next(): Token = when (val char = charStream.read()) {
        null -> Token(Token.Kind.EOL, "")
        in '0'..'9' -> state1(char.toStringBuilder())
        '.' -> state2(".".toStringBuilder())
        '√' -> Token(Token.Kind.IDENT, "√")
        'π' -> Token(Token.Kind.CONSTANT, "π")
        'e' -> state4(char.toStringBuilder())
        in 'a'..'d', in 'f'..'z' -> state5(char.toStringBuilder())
        '+', '-', '*', '÷', '^', '!', '%' -> Token(Token.Kind.OP, char.toString())
        ',' -> Token(Token.Kind.COMMA, ",")
        '(', ')' -> Token(Token.Kind.PARENTHESES, char.toString())
        else -> throw SyntaxException("Illegal character '$char'")
    }

    override fun close() {
        charStream.close()
    }

    private tailrec fun state1(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token(Token.Kind.NUMBER, curLexem.toString())
            in '0'..'9' -> state1(curLexem.append(char))
            '.' -> state3(curLexem.append(char))

            '√', 'π', in 'a'..'z', '+', '-', '*', '÷', '^', '!', '%',
            ',', '(', ')' -> {
                charStream.unread(char)
                Token(Token.Kind.NUMBER, curLexem.toString())
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private fun state2(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            in '0'..'9' -> state3(curLexem.append(char))
            else -> throw SyntaxException("Illegal character '.'")
        }

    private tailrec fun state3(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token(Token.Kind.NUMBER, curLexem.toString())
            in '0'..'9' -> state3(curLexem.append(char))

            '√', 'π', in 'a'..'z', '+', '-', '*', '÷', '^', '!', '%',
            ',', '(', ')' -> {
                charStream.unread(char)
                Token(Token.Kind.NUMBER, curLexem.toString())
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private fun state4(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token(Token.Kind.CONSTANT, "e")
            in 'a'..'d', in 'f'..'z' -> state5(curLexem.append(char))

            '.', in '0'..'9', '√', 'π', 'e', '+', '-', '*', '÷',
            '^', '!', '%', ',', '(', ')' -> {
                charStream.unread(char)
                Token(Token.Kind.CONSTANT, "e")
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private tailrec fun state5(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token(Token.Kind.IDENT, curLexem.toString())

            in '0'..'9', in 'a'..'z' -> state5(curLexem.append(char))

            '.', '√', 'π', '+', '-', '*', '÷', '^', '!', '%',
            ',', '(', ')' -> {
                charStream.unread(char)
                Token(Token.Kind.IDENT, curLexem.toString())
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }


    private fun Char.toStringBuilder() = StringBuilder(toString())
    private fun String.toStringBuilder() = StringBuilder(this)
}
