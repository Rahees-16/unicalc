package com.rahees.unicalc.ui.screens.currency

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Currency Converter") },
            actions = {
                if (state.isFromCache) {
                    Icon(
                        Icons.Default.CloudOff,
                        contentDescription = "Offline",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                IconButton(onClick = { viewModel.fetchRates() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = state.inputValue,
                onValueChange = { viewModel.onInputChange(it) },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            CurrencyDropdown(
                label = "From",
                selectedCode = state.fromCurrency,
                onCurrencySelected = { viewModel.onFromCurrencyChange(it) },
                searchQuery = state.searchQuery,
                onSearchChange = { viewModel.onSearchQueryChange(it) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilledTonalButton(onClick = { viewModel.swapCurrencies() }) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Swap")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Swap")
                }
            }

            CurrencyDropdown(
                label = "To",
                selectedCode = state.toCurrency,
                onCurrencySelected = { viewModel.onToCurrencyChange(it) },
                searchQuery = state.searchQuery,
                onSearchChange = { viewModel.onSearchQueryChange(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.result.isNotEmpty()) {
                val fromInfo = CurrencyData.getInfo(state.fromCurrency)
                val toInfo = CurrencyData.getInfo(state.toCurrency)

                Text(
                    text = "${state.inputValue} ${fromInfo?.flag ?: ""} ${state.fromCurrency}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${state.result} ${toInfo?.flag ?: ""} ${state.toCurrency}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (state.lastUpdated.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Last updated: ${state.lastUpdated}${if (state.isFromCache) " (cached)" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Popular Currencies",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CurrencyData.popularCodes.forEach { code ->
                    val info = CurrencyData.getInfo(code)
                    AssistChip(
                        onClick = { viewModel.onToCurrencyChange(code) },
                        label = { Text("${info?.flag ?: ""} $code") }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(
    label: String,
    selectedCode: String,
    onCurrencySelected: (String) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedInfo = CurrencyData.getInfo(selectedCode)

    val filteredCurrencies = if (searchQuery.isBlank()) {
        CurrencyData.currencies
    } else {
        CurrencyData.currencies.filter {
            it.code.contains(searchQuery, ignoreCase = true) ||
                    it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedInfo?.let { "${it.flag} ${it.code} - ${it.name}" } ?: selectedCode,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                onSearchChange("")
            }
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search currency...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                singleLine = true
            )

            filteredCurrencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text("${currency.flag} ${currency.code} - ${currency.name}") },
                    onClick = {
                        onCurrencySelected(currency.code)
                        expanded = false
                        onSearchChange("")
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
