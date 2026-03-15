package com.rahees.unicalc.ui.screens.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.unicalc.data.remote.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CurrencyUiState(
    val fromCurrency: String = "USD",
    val toCurrency: String = "EUR",
    val inputValue: String = "1",
    val result: String = "",
    val rates: Map<String, Double> = emptyMap(),
    val lastUpdated: String = "",
    val isFromCache: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        fetchRates()
    }

    fun fetchRates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = currencyRepository.getRates(_uiState.value.fromCurrency)
                _uiState.update {
                    it.copy(
                        rates = result.rates,
                        lastUpdated = result.date,
                        isFromCache = result.isFromCache,
                        isLoading = false,
                        error = null
                    )
                }
                convert()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to fetch rates"
                    )
                }
            }
        }
    }

    fun onInputChange(value: String) {
        _uiState.update { it.copy(inputValue = value) }
        convert()
    }

    fun onFromCurrencyChange(code: String) {
        _uiState.update { it.copy(fromCurrency = code) }
        fetchRates()
    }

    fun onToCurrencyChange(code: String) {
        _uiState.update { it.copy(toCurrency = code) }
        convert()
    }

    fun swapCurrencies() {
        _uiState.update {
            it.copy(fromCurrency = it.toCurrency, toCurrency = it.fromCurrency)
        }
        fetchRates()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun convert() {
        val state = _uiState.value
        val inputVal = state.inputValue.toDoubleOrNull() ?: return

        val rate = if (state.fromCurrency == state.toCurrency) {
            1.0
        } else {
            state.rates[state.toCurrency] ?: return
        }

        val result = inputVal * rate
        val resultStr = if (result == result.toLong().toDouble() && kotlin.math.abs(result) < 1e15) {
            result.toLong().toString()
        } else {
            String.format("%.2f", result)
        }

        _uiState.update { it.copy(result = resultStr) }
    }
}
