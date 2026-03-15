package com.rahees.unicalc.ui.screens.calculator

import kotlin.math.PI
import kotlin.math.E
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class ExpressionEvaluator {

    fun evaluate(expression: String): Double {
        val sanitized = expression
            .replace("\u00D7", "*")
            .replace("\u00F7", "/")
            .replace("\u03C0", PI.toString())
            .replace("(?<![a-zA-Z])e(?![a-zA-Z])".toRegex(), E.toString())
            .replace("%", "/100")
        val tokens = tokenize(sanitized)
        val pos = intArrayOf(0)
        val result = parseExpression(tokens, pos)
        if (pos[0] < tokens.size) throw IllegalArgumentException("Unexpected token: ${tokens[pos[0]]}")
        return result
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < expr.length) {
            val c = expr[i]
            when {
                c.isWhitespace() -> i++
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.' || expr[i] == 'E' || expr[i] == 'e' ||
                                (i > 0 && (expr[i - 1] == 'E' || expr[i - 1] == 'e') && (expr[i] == '+' || expr[i] == '-')))) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(sb.toString())
                }
                c.isLetter() -> {
                    val sb = StringBuilder()
                    while (i < expr.length && expr[i].isLetter()) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(sb.toString())
                }
                c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '!' -> {
                    tokens.add(c.toString())
                    i++
                }
                else -> i++
            }
        }
        return tokens
    }

    private fun parseExpression(tokens: List<String>, pos: IntArray): Double {
        var result = parseTerm(tokens, pos)
        while (pos[0] < tokens.size && (tokens[pos[0]] == "+" || tokens[pos[0]] == "-")) {
            val op = tokens[pos[0]]
            pos[0]++
            val right = parseTerm(tokens, pos)
            result = if (op == "+") result + right else result - right
        }
        return result
    }

    private fun parseTerm(tokens: List<String>, pos: IntArray): Double {
        var result = parsePower(tokens, pos)
        while (pos[0] < tokens.size && (tokens[pos[0]] == "*" || tokens[pos[0]] == "/")) {
            val op = tokens[pos[0]]
            pos[0]++
            val right = parsePower(tokens, pos)
            result = if (op == "*") result * right else result / right
        }
        return result
    }

    private fun parsePower(tokens: List<String>, pos: IntArray): Double {
        var result = parseUnaryPostfix(tokens, pos)
        while (pos[0] < tokens.size && tokens[pos[0]] == "^") {
            pos[0]++
            val right = parseUnaryPostfix(tokens, pos)
            result = result.pow(right)
        }
        return result
    }

    private fun parseUnaryPostfix(tokens: List<String>, pos: IntArray): Double {
        var result = parseUnary(tokens, pos)
        while (pos[0] < tokens.size && tokens[pos[0]] == "!") {
            pos[0]++
            result = factorial(result)
        }
        return result
    }

    private fun parseUnary(tokens: List<String>, pos: IntArray): Double {
        if (pos[0] < tokens.size && tokens[pos[0]] == "-") {
            pos[0]++
            return -parseAtom(tokens, pos)
        }
        if (pos[0] < tokens.size && tokens[pos[0]] == "+") {
            pos[0]++
        }
        return parseAtom(tokens, pos)
    }

    private fun parseAtom(tokens: List<String>, pos: IntArray): Double {
        if (pos[0] >= tokens.size) throw IllegalArgumentException("Unexpected end of expression")

        val token = tokens[pos[0]]

        // Function call
        if (token.all { it.isLetter() } && token.isNotEmpty()) {
            return when (token) {
                "sin" -> { pos[0]++; sin(parseFunctionArg(tokens, pos)) }
                "cos" -> { pos[0]++; cos(parseFunctionArg(tokens, pos)) }
                "tan" -> { pos[0]++; tan(parseFunctionArg(tokens, pos)) }
                "log" -> { pos[0]++; log10(parseFunctionArg(tokens, pos)) }
                "ln" -> { pos[0]++; ln(parseFunctionArg(tokens, pos)) }
                "sqrt" -> { pos[0]++; sqrt(parseFunctionArg(tokens, pos)) }
                else -> {
                    // Try to parse as number (might be scientific notation result like "3.14...")
                    pos[0]++
                    token.toDoubleOrNull() ?: throw IllegalArgumentException("Unknown function: $token")
                }
            }
        }

        // Number
        val num = token.toDoubleOrNull()
        if (num != null) {
            pos[0]++
            return num
        }

        // Parenthesized expression
        if (token == "(") {
            pos[0]++
            val result = parseExpression(tokens, pos)
            if (pos[0] < tokens.size && tokens[pos[0]] == ")") {
                pos[0]++
            }
            return result
        }

        throw IllegalArgumentException("Unexpected token: $token")
    }

    private fun parseFunctionArg(tokens: List<String>, pos: IntArray): Double {
        if (pos[0] < tokens.size && tokens[pos[0]] == "(") {
            pos[0]++
            val result = parseExpression(tokens, pos)
            if (pos[0] < tokens.size && tokens[pos[0]] == ")") {
                pos[0]++
            }
            return result
        }
        return parseAtom(tokens, pos)
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n != n.toLong().toDouble() || n > 170) {
            return Double.NaN
        }
        var result = 1.0
        for (i in 2..n.toLong()) {
            result *= i
        }
        return result
    }
}
