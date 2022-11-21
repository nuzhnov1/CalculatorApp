package com.sunman.libcalculator.internal

import com.sunman.libcalculator.SyntaxException
import java.io.Closeable

/**
 * Reads characters from the [String], converts them into tokens
 * and allows to return the read token to the input by placing them on the LIFO stack.
 * Each token is an instance of the [Token] class.
 *
 * @param expression string where will the characters be read from.
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
        null -> Token(kind = Token.Kind.EOL, lexem = "")
        in '0'..'9' -> state1(curLexem = char.toStringBuilder())
        '.' -> state2(curLexem = ".".toStringBuilder())
        '√' -> Token(kind = Token.Kind.IDENT, lexem = "√")
        'π' -> Token(kind = Token.Kind.CONSTANT, lexem = "π")
        'e' -> state4(curLexem = char.toStringBuilder())
        in 'a'..'d', in 'f'..'z' -> state5(curLexem = char.toStringBuilder())
        '+', '-', '*', '÷', '^', '!', '%' -> Token(kind = Token.Kind.OP, lexem = char.toString())
        ',' -> Token(kind = Token.Kind.COMMA, lexem = ",")
        '(', ')' -> Token(kind = Token.Kind.PARENTHESES, lexem = char.toString())
        ' ', '\t' -> Token(kind = Token.Kind.SPACE, lexem = char.toString())
        else -> throw SyntaxException("Illegal character '$char'")
    }

    override fun close() {
        charStream.close()
    }

    private tailrec fun state1(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token(kind = Token.Kind.NUMBER, lexem = curLexem.toString())
            in '0'..'9' -> state1(curLexem = curLexem.append(char))
            '.' -> state3(curLexem = curLexem.append(char))

            '√', 'π', in 'a'..'z', '+', '-', '*', '÷', '^', '!', '%',
            ',', '(', ')', ' ', '\t' -> {
                charStream.unread(char)
                Token(kind = Token.Kind.NUMBER, lexem = curLexem.toString())
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private fun state2(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            in '0'..'9' -> state3(curLexem = curLexem.append(char))
            else -> throw SyntaxException("Illegal character '.'")
        }

    private tailrec fun state3(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token(kind = Token.Kind.NUMBER, lexem = curLexem.toString())
            in '0'..'9' -> state3(curLexem = curLexem.append(char))

            '√', 'π', in 'a'..'z', '+', '-', '*', '÷', '^', '!', '%',
            ',', '(', ')', ' ', '\t' -> {
                charStream.unread(char)
                Token(kind = Token.Kind.NUMBER, lexem = curLexem.toString())
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private fun state4(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token(kind = Token.Kind.CONSTANT, lexem = "e")
            in 'a'..'d', in 'f'..'z' -> state5(curLexem = curLexem.append(char))

            '.', in '0'..'9', '√', 'π', 'e', '+', '-', '*', '÷',
            '^', '!', '%', ',', '(', ')', ' ', '\t' -> {
                charStream.unread(char)
                Token(kind = Token.Kind.CONSTANT, lexem = "e")
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private tailrec fun state5(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token(kind = Token.Kind.IDENT, lexem = curLexem.toString())

            in '0'..'9', in 'a'..'z' -> state5(curLexem = curLexem.append(char))

            '.', '√', 'π', '+', '-', '*', '÷', '^', '!', '%',
            ',', '(', ')', ' ', '\t' -> {
                charStream.unread(char)
                Token(kind = Token.Kind.IDENT, lexem = curLexem.toString())
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }


    private fun Char.toStringBuilder() = StringBuilder(toString())
    private fun String.toStringBuilder() = StringBuilder(this)
}
