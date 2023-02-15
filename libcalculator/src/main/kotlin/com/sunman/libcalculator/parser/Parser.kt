package com.sunman.libcalculator.parser

import com.sunman.libcalculator.SyntaxException

/**
 * Reads tokens, parsing expressions and returns a collection of postfix tokens.
 */
internal class Parser {

    private lateinit var tokenizer: Tokenizer
    private lateinit var postfixRecord: MutableList<PostfixToken>
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
    fun parse(expression: String): Collection<PostfixToken> {
        tokenizer = Tokenizer(expression)
        postfixRecord = mutableListOf()
        curToken = readNextToken()
        symbolsStack = ArrayDeque()

        if (curToken !is Token.EOL) {
            tokenizer.use { parseExpression() }
        }

        return postfixRecord
    }

    private fun parseExpression() {
        symbolsStack.addFirst(NonTerminal.Expression)

        while (!symbolsStack.isEmpty()) {
            when (val symbol = symbolsStack.removeFirst()) {
                // Processing of regular non-terminals:

                NonTerminal.Expression -> {
                    symbolsStack.addFirst(NonTerminal.ExpressionRest)
                    symbolsStack.addFirst(NonTerminal.ExpressionPriority5)
                }

                NonTerminal.ExpressionRest -> parseExpressionRest()
                NonTerminal.ExpressionPriority5 -> parseExpressionPriority5()
                NonTerminal.ExpressionPriority5Rest -> parseExpressionPriority5Rest()
                NonTerminal.ExpressionPriority4 -> parseExpressionPriority4()
                NonTerminal.ExpressionPriority3 -> parseExpressionPriority3()
                NonTerminal.ExpressionPriority3Rest -> parseExpressionPriority3Rest()
                NonTerminal.ExpressionPriority2 -> parseExpressionPriority2()
                NonTerminal.ExpressionPriority2Rest -> parseExpressionPriority2Rest()
                NonTerminal.ExpressionPriority1 -> parseExpressionPriority1()
                NonTerminal.FunctionCallOrNothing -> parseFunctionCallOrNothing()
                NonTerminal.ActualFunctionArgumentsList -> parseActualFunctionArgumentsList()
                NonTerminal.ActualFunctionArgumentsListRest -> parseActualFunctionArgumentsListRest()

                // Processing of non-terminals of actions:

                NonTerminal.Action.CreateAdditionPostfixToken -> postfixRecord.add(
                    PostfixToken.Operation.Addition
                )
                NonTerminal.Action.CreateSubtractionPostfixToken -> postfixRecord.add(
                    PostfixToken.Operation.Subtraction
                )
                NonTerminal.Action.CreateMultiplicationPostfixToken -> postfixRecord.add(
                    PostfixToken.Operation.Multiplication
                )
                NonTerminal.Action.CreateDivisionPostfixToken -> postfixRecord.add(
                    PostfixToken.Operation.Division
                )
                NonTerminal.Action.CreatePowerPostfixToken -> postfixRecord.add(
                    PostfixToken.Operation.Power
                )
                NonTerminal.Action.CreateNegationPostfixToken -> postfixRecord.add(
                    PostfixToken.Operation.Negation
                )
                NonTerminal.Action.CreateInvokePostfixToken -> postfixRecord.add(
                    PostfixToken.Action.Invoke
                )
                NonTerminal.Action.CreatePutArgumentPostfixToken -> postfixRecord.add(
                    PostfixToken.Action.PutArgument
                )

                // Processing of terminals (tokens):

                is Token -> {
                    if (curToken != symbol) {
                        errorReport(
                            expectedInput = symbol.toReportString(),
                            actualInput = curToken.toReportString()
                        )
                    } else {
                        curToken = readNextToken()
                    }
                }
            }
        }

        if (curToken !is Token.EOL) {
            errorReport(
                expectedInput = Token.EOL.toReportString(),
                actualInput = curToken.toReportString()
            )
        }
    }

    private fun parseExpressionRest() {
        when (curToken) {
            is Token.Operator.Plus -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.ExpressionRest)
                symbolsStack.addFirst(NonTerminal.Action.CreateAdditionPostfixToken)
                symbolsStack.addFirst(NonTerminal.ExpressionPriority5)
            }

            is Token.Operator.Minus -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.ExpressionRest)
                symbolsStack.addFirst(NonTerminal.Action.CreateSubtractionPostfixToken)
                symbolsStack.addFirst(NonTerminal.ExpressionPriority5)
            }

            else -> return
        }
    }

    private fun parseExpressionPriority5() {
        symbolsStack.addFirst(NonTerminal.ExpressionPriority5Rest)
        symbolsStack.addFirst(NonTerminal.ExpressionPriority4)
    }

    private fun parseExpressionPriority5Rest() {
        when (curToken) {
            is Token.Operator.Multiplication -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.ExpressionPriority5Rest)
                symbolsStack.addFirst(NonTerminal.Action.CreateMultiplicationPostfixToken)
                symbolsStack.addFirst(NonTerminal.ExpressionPriority4)
            }

            is Token.Operator.Division -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.ExpressionPriority5Rest)
                symbolsStack.addFirst(NonTerminal.Action.CreateDivisionPostfixToken)
                symbolsStack.addFirst(NonTerminal.ExpressionPriority4)
            }

            is Token.Number, is Token.Constant, is Token.Identifier, Token.Parentheses.Open -> {
                symbolsStack.addFirst(NonTerminal.ExpressionPriority5Rest)
                symbolsStack.addFirst(NonTerminal.Action.CreateMultiplicationPostfixToken)
                symbolsStack.addFirst(NonTerminal.ExpressionPriority3)
            }

            else -> return
        }
    }

    private fun parseExpressionPriority4() {
        when (curToken) {
            Token.Operator.Plus -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.ExpressionPriority4)
            }

            Token.Operator.Minus -> {
                curToken = readNextToken()
                symbolsStack.addFirst(NonTerminal.Action.CreateNegationPostfixToken)
                symbolsStack.addFirst(NonTerminal.ExpressionPriority4)
            }

            else -> symbolsStack.addFirst(NonTerminal.ExpressionPriority3)
        }
    }

    private fun parseExpressionPriority3() {
        symbolsStack.addFirst(NonTerminal.ExpressionPriority3Rest)
        symbolsStack.addFirst(NonTerminal.ExpressionPriority2)
    }

    private fun parseExpressionPriority3Rest() {
        if (curToken is Token.Operator.Power) {
            curToken = readNextToken()
            symbolsStack.addFirst(NonTerminal.ExpressionPriority3Rest)
            symbolsStack.addFirst(NonTerminal.Action.CreatePowerPostfixToken)
            symbolsStack.addFirst(NonTerminal.ExpressionPriority4)
        }
    }

    private fun parseExpressionPriority2() {
        symbolsStack.addFirst(NonTerminal.ExpressionPriority2Rest)
        symbolsStack.addFirst(NonTerminal.ExpressionPriority1)
    }

    private fun parseExpressionPriority2Rest() {
        while (curToken is Token.Operator.Factorial || curToken is Token.Operator.Percent) {
            if (curToken is Token.Operator.Factorial) {
                postfixRecord.add(PostfixToken.Operation.Factorial)
            } else {
                postfixRecord.add(PostfixToken.Operation.Percent)
            }

            curToken = readNextToken()
        }
    }

    private fun parseExpressionPriority1() {
        when (curToken) {
            is Token.Number -> {
                val number = curToken as Token.Number

                postfixRecord.add(PostfixToken.Number(lexem = number.lexem))
                curToken = readNextToken()
            }

            is Token.Constant -> {
                val constant = curToken as Token.Constant

                postfixRecord.add(PostfixToken.Constant(lexem = constant.lexem))
                curToken = readNextToken()
            }

            is Token.Identifier -> {
                val identifier = curToken as Token.Identifier

                postfixRecord.add(PostfixToken.Identifier(lexem = identifier.lexem))
                curToken = readNextToken()
                parseFunctionCallOrNothing()
            }

            Token.Parentheses.Open -> {
                curToken = readNextToken()
                symbolsStack.addFirst(Token.Parentheses.Close)
                symbolsStack.addFirst(NonTerminal.Expression)
            }

            else -> errorReport(
                expectedInput = "expression",
                actualInput = curToken.toReportString()
            )
        }
    }

    private fun parseFunctionCallOrNothing() {
        if (curToken == Token.Parentheses.Open) {
            curToken = readNextToken()

            if (curToken == Token.Parentheses.Close) {
                curToken = readNextToken()
                postfixRecord.add(PostfixToken.Action.Invoke)
            } else {
                symbolsStack.addFirst(NonTerminal.Action.CreateInvokePostfixToken)
                symbolsStack.addFirst(Token.Parentheses.Close)
                symbolsStack.addFirst(NonTerminal.ActualFunctionArgumentsList)
            }
        }
    }

    private fun parseActualFunctionArgumentsList() {
        symbolsStack.addFirst(NonTerminal.ActualFunctionArgumentsListRest)
        symbolsStack.addFirst(NonTerminal.Action.CreatePutArgumentPostfixToken)
        symbolsStack.addFirst(NonTerminal.Expression)
    }

    private fun parseActualFunctionArgumentsListRest() {
        if (curToken == Token.Comma) {
            curToken = readNextToken()
            symbolsStack.addFirst(NonTerminal.ActualFunctionArgumentsListRest)
            symbolsStack.addFirst(NonTerminal.Action.CreatePutArgumentPostfixToken)
            symbolsStack.addFirst(NonTerminal.Expression)
        }
    }

    private fun readNextToken(): Token {
        var curToken = tokenizer.next()

        // Skipping the whitespace tokens:
        while (curToken is Token.Space) {
            curToken = tokenizer.next()
        }

        return curToken
    }

    private fun errorReport(expectedInput: String, actualInput: String): Nothing =
        throw SyntaxException("Expected $expectedInput, got $actualInput")

    private fun Token.toReportString() = when (this) {
        Token.EOL -> "${Token.EOL}"
        else -> "'$this'"
    }


    /**
     * Sealed interface of non-terminals of the calculator grammar.
     */
    private sealed interface NonTerminal : GrammarSymbol {
        object Expression : NonTerminal
        object ExpressionRest : NonTerminal
        object ExpressionPriority5 : NonTerminal
        object ExpressionPriority5Rest : NonTerminal
        object ExpressionPriority4 : NonTerminal
        object ExpressionPriority3 : NonTerminal
        object ExpressionPriority3Rest : NonTerminal
        object ExpressionPriority2 : NonTerminal
        object ExpressionPriority2Rest : NonTerminal
        object ExpressionPriority1 : NonTerminal
        object FunctionCallOrNothing : NonTerminal
        object ActualFunctionArgumentsList : NonTerminal
        object ActualFunctionArgumentsListRest : NonTerminal

        sealed interface Action : NonTerminal {
            object CreateAdditionPostfixToken : Action
            object CreateSubtractionPostfixToken : Action
            object CreateNegationPostfixToken : Action
            object CreateMultiplicationPostfixToken : Action
            object CreateDivisionPostfixToken : Action
            object CreatePowerPostfixToken : Action
            object CreateInvokePostfixToken : Action
            object CreatePutArgumentPostfixToken : Action
        }
    }
}
