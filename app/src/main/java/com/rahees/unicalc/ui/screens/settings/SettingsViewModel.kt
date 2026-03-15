package com.rahees.unicalc.ui.screens.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: String = "system",
    val decimalPlaces: Int = 4,
    val defaultFromCurrency: String = "USD",
    val defaultToCurrency: String = "EUR"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        val DECIMAL_PLACES_KEY = intPreferencesKey("decimal_places")
        val DEFAULT_FROM_CURRENCY_KEY = stringPreferencesKey("default_from_currency")
        val DEFAULT_TO_CURRENCY_KEY = stringPreferencesKey("default_to_currency")
    }

    val uiState: StateFlow<SettingsUiState> = dataStore.data.map { prefs ->
        SettingsUiState(
            themeMode = prefs[THEME_MODE_KEY] ?: "system",
            decimalPlaces = prefs[DECIMAL_PLACES_KEY] ?: 4,
            defaultFromCurrency = prefs[DEFAULT_FROM_CURRENCY_KEY] ?: "USD",
            defaultToCurrency = prefs[DEFAULT_TO_CURRENCY_KEY] ?: "EUR"
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            dataStore.edit { it[THEME_MODE_KEY] = mode }
        }
    }

    fun setDecimalPlaces(places: Int) {
        viewModelScope.launch {
            dataStore.edit { it[DECIMAL_PLACES_KEY] = places }
        }
    }

    fun setDefaultFromCurrency(currency: String) {
        viewModelScope.launch {
            dataStore.edit { it[DEFAULT_FROM_CURRENCY_KEY] = currency }
        }
    }

    fun setDefaultToCurrency(currency: String) {
        viewModelScope.launch {
            dataStore.edit { it[DEFAULT_TO_CURRENCY_KEY] = currency }
        }
    }
}
