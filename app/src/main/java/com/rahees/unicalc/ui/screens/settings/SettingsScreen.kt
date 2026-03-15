package com.rahees.unicalc.ui.screens.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Settings") }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Theme section
            SettingsSection(title = "Theme") {
                val themeOptions = listOf(
                    "system" to "System default",
                    "light" to "Light",
                    "dark" to "Dark"
                )
                themeOptions.forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setThemeMode(value) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.themeMode == value,
                            onClick = { viewModel.setThemeMode(value) }
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Decimal places
            SettingsSection(title = "Decimal Places") {
                val options = listOf(2, 4, 6, 8)
                options.forEach { places ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setDecimalPlaces(places) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.decimalPlaces == places,
                            onClick = { viewModel.setDecimalPlaces(places) }
                        )
                        Text(
                            text = "$places decimal places",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Default currency pair
            SettingsSection(title = "Default Currency Pair") {
                val commonPairs = listOf(
                    "USD" to "EUR",
                    "USD" to "GBP",
                    "EUR" to "GBP",
                    "USD" to "JPY",
                    "USD" to "INR",
                    "EUR" to "INR"
                )
                commonPairs.forEach { (from, to) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setDefaultFromCurrency(from)
                                viewModel.setDefaultToCurrency(to)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.defaultFromCurrency == from && state.defaultToCurrency == to,
                            onClick = {
                                viewModel.setDefaultFromCurrency(from)
                                viewModel.setDefaultToCurrency(to)
                            }
                        )
                        Text(
                            text = "$from / $to",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Language
            val context = LocalContext.current
            var showLanguageDialog by remember { mutableStateOf(false) }

            val currentLocale = remember {
                val locales = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.resources.configuration.locales
                } else {
                    @Suppress("DEPRECATION")
                    android.os.LocaleList.forLanguageTags(
                        context.resources.configuration.locales.toLanguageTags()
                    )
                }
                locales.get(0)?.displayLanguage ?: "System Default"
            }

            SettingsSection(title = "Language") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
                                intent.data = Uri.parse("package:${context.packageName}")
                                context.startActivity(intent)
                            } else {
                                showLanguageDialog = true
                            }
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "App Language: $currentLocale",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            if (showLanguageDialog) {
                AlertDialog(
                    onDismissRequest = { showLanguageDialog = false },
                    title = { Text("Language") },
                    text = {
                        Text("On this Android version, please change the app language in your device's system settings:\nSettings > Apps > UniCalc > Language")
                    },
                    confirmButton = {
                        TextButton(onClick = { showLanguageDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "UniCalc v1.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}
