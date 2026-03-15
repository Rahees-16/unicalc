package com.rahees.unicalc.ui.screens.calculator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CalculatorUiState(
    val expression: String = "",
    val result: String = "",
    val isScientific: Boolean = false,
    val history: List<Pair<String, String>> = emptyList()
)

@HiltViewModel
class CalculatorViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val evaluator = ExpressionEvaluator()

    fun onButtonClick(label: String) {
        when (label) {
            "C" -> clear()
            "\u232B" -> backspace()
            "=" -> calculate()
            "sin" -> appendFunction("sin(")
            "cos" -> appendFunction("cos(")
            "tan" -> appendFunction("tan(")
            "log" -> appendFunction("log(")
            "ln" -> appendFunction("ln(")
            "\u221A" -> appendFunction("sqrt(")
            "x\u00B2" -> appendText("^2")
            "x\u02B8" -> appendText("^")
            "\u03C0" -> appendText("\u03C0")
            "e" -> appendText("e")
            "n!" -> appendText("!")
            else -> appendText(label)
        }
    }

    fun toggleScientific() {
        _uiState.update { it.copy(isScientific = !it.isScientific) }
    }

    private fun clear() {
        _uiState.update { it.copy(expression = "", result = "") }
    }

    private fun backspace() {
        _uiState.update {
            val newExpr = if (it.expression.isNotEmpty()) {
                it.expression.dropLast(1)
            } else ""
            it.copy(expression = newExpr, result = tryEvaluate(newExpr))
        }
    }

    private fun appendText(text: String) {
        _uiState.update {
            val newExpr = it.expression + text
            it.copy(expression = newExpr, result = tryEvaluate(newExpr))
        }
    }

    private fun appendFunction(funcText: String) {
        _uiState.update {
            val newExpr = it.expression + funcText
            it.copy(expression = newExpr)
        }
    }

    private fun calculate() {
        val state = _uiState.value
        if (state.expression.isEmpty()) return

        try {
            val result = evaluator.evaluate(state.expression)
            val resultStr = formatResult(result)
            val newHistory = listOf(state.expression to resultStr) + state.history.take(49)
            _uiState.update {
                it.copy(
                    expression = resultStr,
                    result = "",
                    history = newHistory
                )
            }
        } catch (_: Exception) {
            _uiState.update { it.copy(result = "Error") }
        }
    }

    private fun tryEvaluate(expression: String): String {
        if (expression.isEmpty()) return ""
        return try {
            val result = evaluator.evaluate(expression)
            formatResult(result)
        } catch (_: Exception) {
            ""
        }
    }

    private fun formatResult(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return if (value > 0) "\u221E" else "-\u221E"
        return if (value == value.toLong().toDouble() && kotlin.math.abs(value) < 1e15) {
            value.toLong().toString()
        } else {
            val formatted = String.format("%.10g", value)
            formatted.trimEnd('0').trimEnd('.')
        }
    }
}
