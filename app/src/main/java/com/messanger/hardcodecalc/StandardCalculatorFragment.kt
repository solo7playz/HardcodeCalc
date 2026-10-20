package com.messanger.hardcodecalc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Stack

class StandardCalculatorFragment : Fragment() {

    private lateinit var input: EditText
    private lateinit var result: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_standard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input = view.findViewById(R.id.standardInput)
        result = view.findViewById(R.id.standardResult)

        val btn0 = view.findViewById<Button>(R.id.btn0)
        val btn1 = view.findViewById<Button>(R.id.btn1)
        val btn2 = view.findViewById<Button>(R.id.btn2)
        val btn3 = view.findViewById<Button>(R.id.btn3)
        val btn4 = view.findViewById<Button>(R.id.btn4)
        val btn5 = view.findViewById<Button>(R.id.btn5)
        val btn6 = view.findViewById<Button>(R.id.btn6)
        val btn7 = view.findViewById<Button>(R.id.btn7)
        val btn8 = view.findViewById<Button>(R.id.btn8)
        val btn9 = view.findViewById<Button>(R.id.btn9)
        val btnDot = view.findViewById<Button>(R.id.btnDot)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)
        val btnSub = view.findViewById<Button>(R.id.btnSub)
        val btnMul = view.findViewById<Button>(R.id.btnMul)
        val btnDiv = view.findViewById<Button>(R.id.btnDiv)
        val btnEquals = view.findViewById<Button>(R.id.btnEquals)
        val btnClear = view.findViewById<Button>(R.id.btnClear)
        val btnBackspace = view.findViewById<Button>(R.id.btnBackspace)
        val btnParenOpen = view.findViewById<Button>(R.id.btnParenOpen)
        val btnParenClose = view.findViewById<Button>(R.id.btnParenClose)
        val btnPow = view.findViewById<Button>(R.id.btnPow)
        val btnPercent = view.findViewById<Button>(R.id.btnPercent)
        val btnFactorial = view.findViewById<Button>(R.id.btnFactorial)
        val btnSqrt = view.findViewById<Button>(R.id.btnSqrt)

        btn0.setOnClickListener { appendChar("0") }
        btn1.setOnClickListener { appendChar("1") }
        btn2.setOnClickListener { appendChar("2") }
        btn3.setOnClickListener { appendChar("3") }
        btn4.setOnClickListener { appendChar("4") }
        btn5.setOnClickListener { appendChar("5") }
        btn6.setOnClickListener { appendChar("6") }
        btn7.setOnClickListener { appendChar("7") }
        btn8.setOnClickListener { appendChar("8") }
        btn9.setOnClickListener { appendChar("9") }
        btnDot.setOnClickListener { appendChar(".") }
        btnAdd.setOnClickListener { appendChar("+") }
        btnSub.setOnClickListener { appendChar("-") }
        btnMul.setOnClickListener { appendChar("*") }
        btnDiv.setOnClickListener { appendChar("/") }
        btnParenOpen.setOnClickListener { appendChar("(") }
        btnParenClose.setOnClickListener { appendChar(")") }
        btnPow.setOnClickListener { appendChar("^") }
        btnPercent.setOnClickListener { appendChar("%") }
        btnSqrt.setOnClickListener { appendChar("sqrt(") }

        btnFactorial.setOnClickListener { appendChar("!") }

        btnClear.setOnClickListener {
            input.text.clear()
            result.text = "Результат: "
        }

        btnBackspace.setOnClickListener {
            val currentText = input.text.toString()
            if (currentText.isNotEmpty()) {
                var deleteCount = 1
                if (currentText.endsWith("sqrt(")) {
                    deleteCount = 5
                }
                val newText = currentText.dropLast(deleteCount.coerceAtMost(currentText.length))
                input.text.clear()
                input.append(newText)
            }
        }

        btnEquals.setOnClickListener {
            evaluateExpression()
        }
    }

    private fun appendChar(char: String) {
        input.append(char)
    }

    private fun factorial(n: Int): Double {
        if (n <= 1) return 1.0
        var result = 1.0
        for (i in 2..n) {
            result *= i
        }
        return result
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        val chars = expr.toCharArray()

        while (i < chars.size) {
            if (chars[i].isWhitespace()) {
                i++
                continue
            }

            if (chars[i].isDigit() || (chars[i] == '.' && i + 1 < chars.size && chars[i + 1].isDigit())) {
                var j = i
                while (j < chars.size && (chars[j].isDigit() || chars[j] == '.')) {
                    j++
                }
                tokens.add(String(chars.copyOfRange(i, j)))
                i = j
                continue
            }

            if (chars[i] == '(' || chars[i] == ')' || chars[i] == '!' ||
                chars[i] == '+' || chars[i] == '-' || chars[i] == '*' ||
                chars[i] == '/' || chars[i] == '%' || chars[i] == '^') {
                tokens.add(chars[i].toString())
                i++
                continue
            }

            if (chars[i] == 's' && expr.regionMatches(i, "sqrt(", 0, 5)) {
                tokens.add("sqrt")
                i += 5
                continue
            }

            i++
        }

        return tokens
    }

    private data class Parser(val tokens: List<String>, var pos: Int = 0)

    private fun parseExpression(p: Parser): Double {
        var result = parseTerm(p)

        while (p.pos < p.tokens.size && (p.tokens[p.pos] == "+" || p.tokens[p.pos] == "-")) {
            val op = p.tokens[p.pos++]
            val right = parseTerm(p)
            result = if (op == "+") result + right else result - right
        }

        return result
    }

    private fun parseTerm(p: Parser): Double {
        var result = parsePower(p)

        while (p.pos < p.tokens.size && (p.tokens[p.pos] == "*" || p.tokens[p.pos] == "/" || p.tokens[p.pos] == "%")) {
            val op = p.tokens[p.pos++]
            val right = parsePower(p)
            result = when (op) {
                "*" -> result * right
                "/" -> result / right
                "%" -> result % right
                else -> result
            }
        }

        return result
    }

    private fun parsePower(p: Parser): Double {
        val base = parseUnary(p)

        if (p.pos < p.tokens.size && p.tokens[p.pos] == "^") {
            p.pos++
            val exponent = parseUnary(p)
            return Math.pow(base, exponent)
        }

        return base
    }

    private fun parseUnary(p: Parser): Double {
        if (p.pos < p.tokens.size && p.tokens[p.pos] == "-") {
            p.pos++
            return -parseUnary(p)
        }

        if (p.pos < p.tokens.size && p.tokens[p.pos] == "+") {
            p.pos++
            return parseUnary(p)
        }

        return parsePostfix(p)
    }

    private fun parsePostfix(p: Parser): Double {
        var result = parsePrimary(p)

        while (p.pos < p.tokens.size && p.tokens[p.pos] == "!") {
            p.pos++
            result = factorial(result.toInt())
        }

        return result
    }

    private fun parsePrimary(p: Parser): Double {
        if (p.pos >= p.tokens.size) {
            throw IllegalArgumentException("Unexpected end")
        }

        val token = p.tokens[p.pos]

        if (token == "(") {
            p.pos++
            val result = parseExpression(p)
            if (p.pos < p.tokens.size && p.tokens[p.pos] == ")") {
                p.pos++
            }
            return result
        }

        if (token == "sqrt") {
            p.pos++
            if (p.pos < p.tokens.size && p.tokens[p.pos] == "(") {
                p.pos++
            }
            val value = parseExpression(p)
            if (p.pos < p.tokens.size && p.tokens[p.pos] == ")") {
                p.pos++
            }
            return Math.sqrt(value)
        }

        if (token.startsWith("sqrt")) {
            p.pos++
            return Math.sqrt(parsePrimary(p))
        }

        if (token.matches("-?\\d+(\\.\\d+)?".toRegex())) {
            p.pos++
            return token.toDouble()
        }

        throw IllegalArgumentException("Unexpected token: $token")
    }

    private fun evaluateExpression() {
        val expression = input.text.toString().trim()
        if (expression.isEmpty()) return

        try {
            val tokens = tokenize(expression)
            val parser = Parser(tokens)
            val resultValue = parseExpression(parser)

            val formattedResult = if (resultValue == resultValue.toLong().toDouble()) {
                resultValue.toLong().toString()
            } else {
                String.format("%.10f", resultValue).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
            }

            result.text = "Результат: $formattedResult"
        } catch (e: Exception) {
            result.text = "Ошибка вычисления"
        }
    }
}
