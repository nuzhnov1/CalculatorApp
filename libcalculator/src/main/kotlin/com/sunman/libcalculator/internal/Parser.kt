package com.sunman.libcalculator.internal

import com.sunman.libcalculator.SyntaxException

/**
 * Reads tokens, parsing expressions and returns a list of postfix tokens.
 */
internal class Parser {

    private lateinit var tokenizer: Tokenizer
    private lateinit var postfixRecord: MutableList<PostfixItem>
    private lateinit var curToken: Token
    private lateinit var symbolsStack: ArrayDeque<GrammarSymbol>


    /**
     * Parse a single expression, reads characters from the [expression] string,
     * and returns list of postfix tokens.
     *
     * @throws SyntaxException If a expression was read that
     * does not match the grammar of the calculator or contains illegal characters.
     * @throws java.io.IOException If an I/O error occurs.
     */
    fun parse(expression: String): List<PostfixItem> {
        tokenizer = Tokenizer(expression)
        postfixRecord = mutableListOf()
        curToken = readNextToken()
        symbolsStack = ArrayDeque()

        if (curToken.kind != Token.Kind.EOL) {
            tokenizer.use { parseExpression() }
        }

        return postfixRecord.toList()
    }

    private fun readNextToken(): Token {
        var curToken = tokenizer.next()

        // Skipping the whitespace tokens:
        while (curToken.kind == Token.Kind.SPACE) {
            curToken = tokenizer.next()
        }

        return curToken
    }

    private fun errorReport(expectedInput: String, actualToken: Token): Nothing {
        when (actualToken.kind) {
            Token.Kind.EOL -> throw SyntaxException("Expected $expectedInput, got end of line")
            else -> throw SyntaxException("Expected $expectedInput, got '${actualToken.lexem}'")
        }
    }

    private fun parseExpression() {
        symbolsStack.addFirst(NonTerminal.Expr)

        while (!symbolsStack.isEmpty()) {
            when (val symbol = symbolsStack.removeFirst()) {
                // Processing of regular non-terminals:

                NonTerminal.Expr -> {
                    symbolsStack.addFirst(NonTerminal.ExprRest)
                    symbolsStack.addFirst(NonTerminal.ExprPriority5)
                }

                NonTerminal.ExprRest -> parseExprRest()
                NonTerminal.ExprPriority5 -> parseExprPriority5()
                NonTerminal.ExprPriority5Rest -> parseExprPriority5Rest()
                NonTerminal.ExprPriority4 -> parseExprPriority4()
                NonTerminal.ExprPriority3 -> parseExprPriority3()
                NonTerminal.ExprPriority3Rest -> parseExprPriority3Rest()
                NonTerminal.ExprPriority2 -> parseExprPriority2()
                NonTerminal.ExprPriority2Rest -> parseExprPriority2Rest()
                NonTerminal.ExprPriority1 -> parseExprPriority1()
                NonTerminal.FunctionCallOrEpsilon -> parseFunctionCallOrEpsilon()
                NonTerminal.ActualArgumentsList -> parseActualArgumentsList()
                NonTerminal.ActualArgumentsListRest -> parseActualArgumentsListRest()

                // Processing of non-terminals of actions:

                NonTerminal.AddBinPlusToPostfixRecord -> postfixRecord.add(
                    PostfixItem(kind = PostfixItem.Kind.OP, lexem = "+")
                )
                NonTerminal.AddBinMinusToPostfixRecord -> postfixRecord.add(
                    PostfixItem(kind = PostfixItem.Kind.OP, lexem = "-")
                )
                NonTerminal.AddUnMinusToPostfixRecord -> postfixRecord.add(
                    PostfixItem(kind = PostfixItem.Kind.OP, lexem = "u-")
                )
                NonTerminal.AddMulToPostfixRecord -> postfixRecord.add(
                    PostfixItem(kind = PostfixItem.Kind.OP, lexem = "*")
                )
                NonTerminal.AddDivToPostfixRecord -> postfixRecord.add(
                    PostfixItem(kind = PostfixItem.Kind.OP, lexem = "÷")
                )
                NonTerminal.AddPowToPostfixRecord -> postfixRecord.add(
                    PostfixItem(kind = PostfixItem.Kind.OP, lexem = "^")
                )
                NonTerminal.AddInvokeActionToPostfixRecord -> postfixRecord.add(
                    PostfixItem(kind = PostfixItem.Kind.ACTION, lexem = "invoke")
                )
                NonTerminal.AddPutArgActionToPostfixRecord -> postfixRecord.add(
                    PostfixItem(kind = PostfixItem.Kind.ACTION, lexem = "put_arg")
                )

                // Processing of terminals (tokens):

                else -> {
                    val expectedToken = symbol as Token

                    if (curToken != expectedToken) {
                        errorReport(
                            expectedInput = "'${expectedToken.lexem}'",
                            actualToken = curToken
                        )
                    } else {
                        curToken = readNextToken()
                    }
                }
            }
        }

        if (curToken.kind != Token.Kind.EOL) {
            errorReport(expectedInput = "end of line", actualToken = curToken)
        }
    }

    private fun parseExprRest() {
        when (curToken.lexem) {
            "+" -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.ExprRest)
                symbolsStack.addFirst(NonTerminal.AddBinPlusToPostfixRecord)
                symbolsStack.addFirst(NonTerminal.ExprPriority5)
            }

            "-" -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.ExprRest)
                symbolsStack.addFirst(NonTerminal.AddBinMinusToPostfixRecord)
                symbolsStack.addFirst(NonTerminal.ExprPriority5)
            }
        }
    }

    private fun parseExprPriority5() {
        symbolsStack.addFirst(NonTerminal.ExprPriority5Rest)
        symbolsStack.addFirst(NonTerminal.ExprPriority4)
    }

    private fun parseExprPriority5Rest() {
        when (curToken.kind) {
            Token.Kind.OP -> {
                if (curToken.lexem == "*") {
                    curToken = readNextToken()
                    symbolsStack.addFirst(NonTerminal.ExprPriority5Rest)
                    symbolsStack.addFirst(NonTerminal.AddMulToPostfixRecord)
                    symbolsStack.addFirst(NonTerminal.ExprPriority4)
                } else if (curToken.lexem == "÷") {
                    curToken = readNextToken()
                    symbolsStack.addFirst(NonTerminal.ExprPriority5Rest)
                    symbolsStack.addFirst(NonTerminal.AddDivToPostfixRecord)
                    symbolsStack.addFirst(NonTerminal.ExprPriority4)
                }
            }

            Token.Kind.NUMBER, Token.Kind.IDENT, Token.Kind.CONSTANT -> {
                symbolsStack.addFirst(NonTerminal.ExprPriority5Rest)
                symbolsStack.addFirst(NonTerminal.AddMulToPostfixRecord)
                symbolsStack.addFirst(NonTerminal.ExprPriority3)
            }

            Token.Kind.PARENTHESES -> {
                if (curToken.lexem == "(") {
                    symbolsStack.addFirst(NonTerminal.ExprPriority5Rest)
                    symbolsStack.addFirst(NonTerminal.AddMulToPostfixRecord)
                    symbolsStack.addFirst(NonTerminal.ExprPriority3)
                }
            }

            else -> return
        }
    }

    private fun parseExprPriority4() {
        when (curToken.lexem) {
            "+" -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.ExprPriority4)
            }

            "-" -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.AddUnMinusToPostfixRecord)
                symbolsStack.addFirst(NonTerminal.ExprPriority4)
            }

            else -> symbolsStack.addFirst(NonTerminal.ExprPriority3)
        }
    }

    private fun parseExprPriority3() {
        symbolsStack.addFirst(NonTerminal.ExprPriority3Rest)
        symbolsStack.addFirst(NonTerminal.ExprPriority2)
    }

    private fun parseExprPriority3Rest() {
        if (curToken.lexem == "^") {
            curToken = readNextToken()
            symbolsStack.addFirst(NonTerminal.ExprPriority3Rest)
            symbolsStack.addFirst(NonTerminal.AddPowToPostfixRecord)
            symbolsStack.addFirst(NonTerminal.ExprPriority4)
        }
    }

    private fun parseExprPriority2() {
        symbolsStack.addFirst(NonTerminal.ExprPriority2Rest)
        symbolsStack.addFirst(NonTerminal.ExprPriority1)
    }

    private fun parseExprPriority2Rest() {
        while (curToken.lexem == "!" || curToken.lexem == "%") {
            postfixRecord.add(PostfixItem(PostfixItem.Kind.OP, curToken.lexem))
            curToken = readNextToken()
        }
    }

    private fun parseExprPriority1() {
        when (curToken.kind) {
            Token.Kind.NUMBER -> {
                postfixRecord.add(
                    PostfixItem(
                        kind = PostfixItem.Kind.NUMBER,
                        lexem = curToken.lexem
                    )
                )
                curToken = readNextToken()
            }

            Token.Kind.CONSTANT -> {
                postfixRecord.add(
                    PostfixItem(
                        kind = PostfixItem.Kind.CONSTANT,
                        lexem = curToken.lexem
                    )
                )
                curToken = readNextToken()
            }

            Token.Kind.IDENT -> {
                postfixRecord.add(
                    PostfixItem(
                        kind = PostfixItem.Kind.IDENT,
                        lexem = curToken.lexem
                    )
                )
                curToken = readNextToken()
                parseFunctionCallOrEpsilon()
            }

            Token.Kind.PARENTHESES -> {
                when (curToken.lexem) {
                    "(" -> {
                        curToken = readNextToken()
                        symbolsStack.addFirst(Token(kind = Token.Kind.PARENTHESES, lexem = ")"))
                        symbolsStack.addFirst(NonTerminal.Expr)
                    }

                    else -> errorReport(expectedInput = "expression", actualToken = curToken)
                }
            }

            else -> errorReport(expectedInput = "expression", actualToken = curToken)
        }
    }

    private fun parseFunctionCallOrEpsilon() {
        if (curToken.lexem == "(") {
            curToken = readNextToken()

            if (curToken.lexem == ")") {
                curToken = readNextToken()
                postfixRecord.add(PostfixItem(kind = PostfixItem.Kind.ACTION, lexem = "invoke"))
            } else {
                symbolsStack.addFirst(NonTerminal.AddInvokeActionToPostfixRecord)
                symbolsStack.addFirst(Token(kind = Token.Kind.PARENTHESES, lexem = ")"))
                symbolsStack.addFirst(NonTerminal.ActualArgumentsList)
            }
        }
    }

    private fun parseActualArgumentsList() {
        symbolsStack.addFirst(NonTerminal.ActualArgumentsListRest)
        symbolsStack.addFirst(NonTerminal.AddPutArgActionToPostfixRecord)
        symbolsStack.addFirst(NonTerminal.Expr)
    }

    private fun parseActualArgumentsListRest() {
        if (curToken.kind == Token.Kind.COMMA) {
            curToken = readNextToken()
            symbolsStack.addFirst(NonTerminal.ActualArgumentsListRest)
            symbolsStack.addFirst(NonTerminal.AddPutArgActionToPostfixRecord)
            symbolsStack.addFirst(NonTerminal.Expr)
        }
    }


    /**
     * Enumeration of non-terminals of the calculator grammar.
     */
    private enum class NonTerminal : GrammarSymbol {
        // Regular non-terminals:
        Expr, ExprRest, ExprPriority5, ExprPriority5Rest,
        ExprPriority4, ExprPriority3, ExprPriority3Rest,
        ExprPriority2, ExprPriority2Rest, ExprPriority1,
        FunctionCallOrEpsilon, ActualArgumentsList,
        ActualArgumentsListRest,

        // Non-terminals of actions:
        AddBinPlusToPostfixRecord, AddBinMinusToPostfixRecord,
        AddUnMinusToPostfixRecord,
        AddMulToPostfixRecord, AddDivToPostfixRecord,
        AddPowToPostfixRecord,
        AddInvokeActionToPostfixRecord,
        AddPutArgActionToPostfixRecord
    }
}
