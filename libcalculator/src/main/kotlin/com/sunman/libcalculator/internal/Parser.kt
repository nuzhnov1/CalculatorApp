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
     * Parse a single expression, reads characters from the [expression],
     * and returns list of postfix tokens.
     *
     * @throws SyntaxException If a expression was read that
     * does not match the grammar of the calculator or contains illegal characters.
     * @throws java.io.IOException If an I/O error occurs.
     */
    fun parse(expression: String): List<PostfixItem> {
        tokenizer = Tokenizer(expression)
        postfixRecord = mutableListOf()
        curToken = tokenizer.next()
        symbolsStack = ArrayDeque()

        if (curToken.kind != Token.Kind.EOL) {
            tokenizer.use { parseExpression() }
        }

        return postfixRecord.toList()
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
                    PostfixItem(PostfixItem.Kind.OP, "+")
                )
                NonTerminal.AddBinMinusToPostfixRecord -> postfixRecord.add(
                    PostfixItem(PostfixItem.Kind.OP, "-")
                )
                NonTerminal.AddUnMinusToPostfixRecord -> postfixRecord.add(
                    PostfixItem(PostfixItem.Kind.OP, "u-")
                )
                NonTerminal.AddMulToPostfixRecord -> postfixRecord.add(
                    PostfixItem(PostfixItem.Kind.OP, "*")
                )
                NonTerminal.AddDivToPostfixRecord -> postfixRecord.add(
                    PostfixItem(PostfixItem.Kind.OP, "÷")
                )
                NonTerminal.AddPowToPostfixRecord -> postfixRecord.add(
                    PostfixItem(PostfixItem.Kind.OP, "^")
                )
                NonTerminal.AddInvokeActionToPostfixRecord -> postfixRecord.add(
                    PostfixItem(PostfixItem.Kind.ACTION, "invoke")
                )
                NonTerminal.AddPutArgActionToPostfixRecord -> postfixRecord.add(
                    PostfixItem(PostfixItem.Kind.ACTION, "put_arg")
                )

                // Processing of terminals (tokens):

                else -> {
                    val expectedToken = symbol as Token

                    if (curToken != expectedToken) {
                        errorReport("'${expectedToken.lexem}'", curToken)
                    } else {
                        curToken = tokenizer.next()
                    }
                }
            }
        }

        if (curToken.kind != Token.Kind.EOL) {
            errorReport("end of line", curToken)
        }
    }

    private fun parseExprRest() {
        when (curToken.lexem) {
            "+" -> {
                curToken = tokenizer.next()
                symbolsStack.addFirst(NonTerminal.ExprRest)
                symbolsStack.addFirst(NonTerminal.AddBinPlusToPostfixRecord)
                symbolsStack.addFirst(NonTerminal.ExprPriority5)
            }

            "-" -> {
                curToken = tokenizer.next()
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
                    curToken = tokenizer.next()
                    symbolsStack.addFirst(NonTerminal.ExprPriority5Rest)
                    symbolsStack.addFirst(NonTerminal.AddMulToPostfixRecord)
                    symbolsStack.addFirst(NonTerminal.ExprPriority4)
                } else if (curToken.lexem == "÷") {
                    curToken = tokenizer.next()
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
                curToken = tokenizer.next()
                symbolsStack.addFirst(NonTerminal.ExprPriority4)
            }

            "-" -> {
                curToken = tokenizer.next()
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
            curToken = tokenizer.next()
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
            curToken = tokenizer.next()
        }
    }

    private fun parseExprPriority1() {
        when (curToken.kind) {
            Token.Kind.NUMBER -> {
                postfixRecord.add(PostfixItem(PostfixItem.Kind.NUMBER, curToken.lexem))
                curToken = tokenizer.next()
            }

            Token.Kind.CONSTANT -> {
                postfixRecord.add(PostfixItem(PostfixItem.Kind.CONSTANT, curToken.lexem))
                curToken = tokenizer.next()
            }

            Token.Kind.IDENT -> {
                postfixRecord.add(PostfixItem(PostfixItem.Kind.IDENT, curToken.lexem))
                curToken = tokenizer.next()
                parseFunctionCallOrEpsilon()
            }

            Token.Kind.PARENTHESES -> {
                when (curToken.lexem) {
                    "(" -> {
                        curToken = tokenizer.next()
                        symbolsStack.addFirst(Token(Token.Kind.PARENTHESES, ")"))
                        symbolsStack.addFirst(NonTerminal.Expr)
                    }

                    else -> errorReport("expression", curToken)
                }
            }

            else -> errorReport("expression", curToken)
        }
    }

    private fun parseFunctionCallOrEpsilon() {
        if (curToken.lexem == "(") {
            curToken = tokenizer.next()

            if (curToken.lexem == ")") {
                curToken = tokenizer.next()
                postfixRecord.add(PostfixItem(PostfixItem.Kind.ACTION, "invoke"))
            } else {
                symbolsStack.addFirst(NonTerminal.AddInvokeActionToPostfixRecord)
                symbolsStack.addFirst(Token(Token.Kind.PARENTHESES, ")"))
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
            curToken = tokenizer.next()
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
