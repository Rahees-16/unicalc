package com.rahees.unicalc.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CalcButton(
    val label: String,
    val type: ButtonType = ButtonType.NUMBER
)

enum class ButtonType {
    NUMBER, OPERATOR, FUNCTION, CLEAR, EQUALS
}

@Composable
fun CalculatorKeypad(
    isScientific: Boolean,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scientificButtons = listOf(
        CalcButton("sin", ButtonType.FUNCTION),
        CalcButton("cos", ButtonType.FUNCTION),
        CalcButton("tan", ButtonType.FUNCTION),
        CalcButton("log", ButtonType.FUNCTION),
        CalcButton("ln", ButtonType.FUNCTION),
        CalcButton("\u221A", ButtonType.FUNCTION),
        CalcButton("x\u00B2", ButtonType.FUNCTION),
        CalcButton("x\u02B8", ButtonType.FUNCTION),
        CalcButton("\u03C0", ButtonType.FUNCTION),
        CalcButton("e", ButtonType.FUNCTION),
        CalcButton("n!", ButtonType.FUNCTION),
        CalcButton("(", ButtonType.OPERATOR),
        CalcButton(")", ButtonType.OPERATOR),
    )

    val standardButtons = listOf(
        CalcButton("C", ButtonType.CLEAR),
        CalcButton("(", ButtonType.OPERATOR),
        CalcButton(")", ButtonType.OPERATOR),
        CalcButton("\u00F7", ButtonType.OPERATOR),

        CalcButton("7"),
        CalcButton("8"),
        CalcButton("9"),
        CalcButton("\u00D7", ButtonType.OPERATOR),

        CalcButton("4"),
        CalcButton("5"),
        CalcButton("6"),
        CalcButton("-", ButtonType.OPERATOR),

        CalcButton("1"),
        CalcButton("2"),
        CalcButton("3"),
        CalcButton("+", ButtonType.OPERATOR),

        CalcButton("%", ButtonType.OPERATOR),
        CalcButton("0"),
        CalcButton("."),
        CalcButton("\u232B", ButtonType.CLEAR),

        CalcButton("=", ButtonType.EQUALS),
    )

    Column(modifier = modifier) {
        if (isScientific) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                scientificButtons.take(5).forEach { button ->
                    FunctionButton(
                        label = button.label,
                        onClick = { onButtonClick(button.label) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                scientificButtons.drop(5).take(5).forEach { button ->
                    FunctionButton(
                        label = button.label,
                        onClick = { onButtonClick(button.label) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                scientificButtons.drop(10).forEach { button ->
                    FunctionButton(
                        label = button.label,
                        onClick = { onButtonClick(button.label) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(2.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            items(standardButtons) { button ->
                when (button.type) {
                    ButtonType.OPERATOR -> {
                        Button(
                            onClick = { onButtonClick(button.label) },
                            modifier = Modifier.height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Text(
                                text = button.label,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    ButtonType.CLEAR -> {
                        Button(
                            onClick = { onButtonClick(button.label) },
                            modifier = Modifier.height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Text(
                                text = button.label,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    ButtonType.EQUALS -> {
                        Button(
                            onClick = { onButtonClick(button.label) },
                            modifier = Modifier
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Text(
                                text = button.label,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        FilledTonalButton(
                            onClick = { onButtonClick(button.label) },
                            modifier = Modifier.height(56.dp),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Text(
                                text = button.label,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FunctionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        contentPadding = PaddingValues(2.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
