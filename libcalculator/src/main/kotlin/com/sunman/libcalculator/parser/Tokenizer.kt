package com.sunman.libcalculator.parser

import com.sunman.libcalculator.E
import com.sunman.libcalculator.PI
import com.sunman.libcalculator.SQRT_CHAR
import com.sunman.libcalculator.SyntaxException
import java.io.Closeable

/**
 * Reads characters from the [String] and converts them into tokens
 * Each token is an instance of the [Token] class.
 *
 * @param expression string where will the characters be read from.
 */
internal class Tokenizer(expression: String) : Closeable {

    private val charStream = CharStream(expression, bufferSize = 1)


    /**
     * Reads and returns a single token.
     *
     * @throws SyntaxException If an illegal character occurs.
     * @throws java.io.IOException If an I/O error occurs.
     */
    fun next(): Token = when (val char = charStream.read()) {
        null -> Token.EOL
        in '0'..'9' -> state1(curLexem = char.toStringBuilder())
        '.' -> state2(curLexem = '.'.toStringBuilder())
        SQRT_CHAR -> Token.Identifier(lexem = "$SQRT_CHAR")
        PI -> Token.Constant(lexem = "$PI")
        E -> state4(curLexem = char.toStringBuilder())
        in 'a'..'z' -> state5(curLexem = char.toStringBuilder())
        '+' -> Token.Operator.Plus
        '-' -> Token.Operator.Minus
        '*' -> Token.Operator.Multiplication
        '/' -> Token.Operator.Division
        '^' -> Token.Operator.Power
        '!' -> Token.Operator.Factorial
        '%' -> Token.Operator.Percent
        ',' -> Token.Comma
        '(' -> Token.Parentheses.Open
        ')' -> Token.Parentheses.Close
        ' ', '\t' -> Token.Space(lexem = char.toString())
        else -> throw SyntaxException("Illegal character '$char'")
    }

    override fun close() {
        charStream.close()
    }

    private tailrec fun state1(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token.Number(lexem = curLexem.toString())
            in '0'..'9' -> state1(curLexem = curLexem.append(char))
            '.' -> state3(curLexem = curLexem.append(char))

            SQRT_CHAR, PI, E, in 'a'..'z', '+', '-', '*',
            '/', '^', '!', '%', ',', '(', ')', ' ', '\t' -> {
                charStream.unread(char)
                Token.Number(lexem = curLexem.toString())
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
            null -> Token.Number(lexem = curLexem.toString())
            in '0'..'9' -> state3(curLexem = curLexem.append(char))

            SQRT_CHAR, PI, E, in 'a'..'z', '+', '-', '*', '/',
            '^', '!', '%', ',', '(', ')', ' ', '\t' -> {
                charStream.unread(char)
                Token.Number(lexem = curLexem.toString())
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private fun state4(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token.Constant(lexem = "$E")
            in 'a'..'d', in 'f'..'z' -> state5(curLexem = curLexem.append(char))

            '.', in '0'..'9', SQRT_CHAR, PI, E, '+', '-', '*', '/',
            '^', '!', '%', ',', '(', ')', ' ', '\t' -> {
                charStream.unread(char)
                Token.Constant(lexem = "$E")
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private tailrec fun state5(curLexem: StringBuilder): Token =
        when (val char = charStream.read()) {
            null -> Token.Identifier(lexem = curLexem.toString())

            in '0'..'9', in 'a'..'z' -> state5(curLexem = curLexem.append(char))

            '.', SQRT_CHAR, PI, E, '+', '-', '*', '/',
            '^', '!', '%', ',', '(', ')', ' ', '\t' -> {
                charStream.unread(char)
                Token.Identifier(lexem = curLexem.toString())
            }

            else -> throw SyntaxException("Illegal character '$char'")
        }

    private fun Char.toStringBuilder() = StringBuilder(toString())
}
