package com.rahees.unicalc.ui.screens.converter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahees.unicalc.ui.components.ConversionResultCard
import com.rahees.unicalc.ui.components.UnitPicker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConverterScreen(
    categoryName: String,
    onBackClick: () -> Unit,
    viewModel: ConverterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val isExpanded = screenWidthDp > 840
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
    Column(modifier = Modifier.fillMaxSize().padding(scaffoldPadding)) {
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

        if (isExpanded) {
            // Tablet: converter and all units side by side
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Converter side
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    ConverterContent(
                        state, viewModel, showAllUnitsToggle = false,
                        onResultLongClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("result", state.result))
                            scope.launch { snackbarHostState.showSnackbar("Result copied to clipboard") }
                        }
                    )
                }

                // All units side (always visible on tablets)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "All Conversions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyColumn {
                        items(state.allConversions) { (unit, value) ->
                            ConversionResultCard(
                                unitName = unit.name,
                                unitSymbol = unit.symbol,
                                convertedValue = value
                            )
                        }
                    }
                }
            }
        } else {
            // Phone: vertical scrolling layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                ConverterContent(
                    state, viewModel, showAllUnitsToggle = true,
                    onResultLongClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("result", state.result))
                        scope.launch { snackbarHostState.showSnackbar("Result copied to clipboard") }
                    }
                )
            }
        }
    }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConverterContent(
    state: ConverterUiState,
    viewModel: ConverterViewModel,
    showAllUnitsToggle: Boolean,
    onResultLongClick: (() -> Unit)? = null
) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {},
                    onLongClick = { onResultLongClick?.invoke() }
                )
        ) {
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
    }

    if (showAllUnitsToggle) {
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
