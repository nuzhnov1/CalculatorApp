package com.sunman.libcalculator.internal

import java.io.Closeable
import java.io.PushbackReader
import java.io.StringReader

/**
 * Reads characters and also allows to return
 * them to the input by placing them on the LIFO stack.
 *
 * @param expression where will the characters be read from.
 */
internal class CharStream(expression: String) : Closeable {

    private val pushbackReader = PushbackReader(StringReader(expression), 1)


    /**
     * Reads a single character or the end of file (EOF).
     * If EOF has been read, the method returns null.
     */
    fun read(): Char? {
        val code = pushbackReader.read()

        return if (code < 0) {
            null
        } else {
            code.toChar()
        }
    }

    /**
     * Return a single character to the input by placing them on the LIFO stack.
     *
     * @throws java.io.IOException If the pushback buffer is full, or if some other I/O error occurs.
     */
    fun unread(char: Char) {
        pushbackReader.unread(char.code)
    }

    override fun close() {
        pushbackReader.close()
    }
}
