package com.rahees.unicalc.ui.screens.converter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahees.unicalc.ui.components.ConversionResultCard
import com.rahees.unicalc.ui.components.UnitPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    categoryName: String,
    onBackClick: () -> Unit,
    viewModel: ConverterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text(state.category.displayName) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { viewModel.toggleFavorite() }) {
                    Icon(
                        imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle favorite",
                        tint = if (state.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = state.inputValue,
                onValueChange = { viewModel.onInputChange(it) },
                label = { Text("Enter value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            UnitPicker(
                label = "From",
                units = state.units,
                selectedUnit = state.fromUnit,
                onUnitSelected = { viewModel.onFromUnitChange(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                FilledTonalButton(onClick = { viewModel.swapUnits() }) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Swap units")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Swap")
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            UnitPicker(
                label = "To",
                units = state.units,
                selectedUnit = state.toUnit,
                onUnitSelected = { viewModel.onToUnitChange(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Result display
            if (state.result.isNotEmpty()) {
                Text(
                    text = state.result,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = state.toUnit?.let { "${it.name} (${it.symbol})" } ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { viewModel.toggleShowAllUnits() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (state.showAllUnits) "Hide all units" else "Show all units",
                    fontSize = 14.sp
                )
            }

            AnimatedVisibility(visible = state.showAllUnits) {
                Column {
                    state.allConversions.forEach { (unit, value) ->
                        ConversionResultCard(
                            unitName = unit.name,
                            unitSymbol = unit.symbol,
                            convertedValue = value
                        )
                    }
                }
            }
        }
    }
}
