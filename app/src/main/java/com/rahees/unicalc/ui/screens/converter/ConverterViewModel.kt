package com.rahees.unicalc.ui.screens.converter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.unicalc.data.local.ConversionDao
import com.rahees.unicalc.data.local.ConversionEntity
import com.rahees.unicalc.data.local.FavoriteEntity
import com.rahees.unicalc.domain.UnitCategory
import com.rahees.unicalc.domain.UnitConverter
import com.rahees.unicalc.domain.UnitData
import com.rahees.unicalc.domain.UnitDef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConverterUiState(
    val category: UnitCategory = UnitCategory.LENGTH,
    val units: List<UnitDef> = emptyList(),
    val fromUnit: UnitDef? = null,
    val toUnit: UnitDef? = null,
    val inputValue: String = "1",
    val result: String = "",
    val allConversions: List<Pair<UnitDef, String>> = emptyList(),
    val showAllUnits: Boolean = false,
    val isFavorite: Boolean = false
)

@HiltViewModel
class ConverterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val conversionDao: ConversionDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConverterUiState())
    val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()

    private val _inputFlow = MutableStateFlow("1")

    init {
        val categoryName = savedStateHandle.get<String>("category") ?: UnitCategory.LENGTH.name
        val category = try {
            UnitCategory.valueOf(categoryName)
        } catch (_: Exception) {
            UnitCategory.LENGTH
        }
        val units = UnitData.units[category] ?: emptyList()
        val fromUnit = units.firstOrNull()
        val toUnit = units.getOrNull(1) ?: units.firstOrNull()

        _uiState.value = ConverterUiState(
            category = category,
            units = units,
            fromUnit = fromUnit,
            toUnit = toUnit,
            inputValue = "1"
        )

        convert()
        checkFavorite()
        setupDebounce()
    }

    @OptIn(FlowPreview::class)
    private fun setupDebounce() {
        viewModelScope.launch {
            _inputFlow.debounce(300).collect {
                convert()
            }
        }
    }

    fun onInputChange(value: String) {
        _uiState.update { it.copy(inputValue = value) }
        _inputFlow.value = value
    }

    fun onFromUnitChange(unit: UnitDef) {
        _uiState.update { it.copy(fromUnit = unit) }
        convert()
        checkFavorite()
    }

    fun onToUnitChange(unit: UnitDef) {
        _uiState.update { it.copy(toUnit = unit) }
        convert()
        checkFavorite()
    }

    fun swapUnits() {
        _uiState.update {
            it.copy(fromUnit = it.toUnit, toUnit = it.fromUnit)
        }
        convert()
        checkFavorite()
    }

    fun toggleShowAllUnits() {
        _uiState.update { it.copy(showAllUnits = !it.showAllUnits) }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val state = _uiState.value
            val from = state.fromUnit ?: return@launch
            val to = state.toUnit ?: return@launch

            if (state.isFavorite) {
                conversionDao.deleteFavoriteByUnits(state.category.name, from.name, to.name)
            } else {
                conversionDao.insertFavorite(
                    FavoriteEntity(
                        category = state.category.name,
                        fromUnit = from.name,
                        toUnit = to.name
                    )
                )
            }
            checkFavorite()
        }
    }

    private fun checkFavorite() {
        viewModelScope.launch {
            val state = _uiState.value
            val from = state.fromUnit ?: return@launch
            val to = state.toUnit ?: return@launch
            val count = conversionDao.isFavorite(state.category.name, from.name, to.name)
            _uiState.update { it.copy(isFavorite = count > 0) }
        }
    }

    private fun convert() {
        val state = _uiState.value
        val from = state.fromUnit ?: return
        val to = state.toUnit ?: return
        val inputValue = state.inputValue.toDoubleOrNull() ?: return

        val result = UnitConverter.convert(inputValue, from, to)
        val resultStr = formatResult(result)

        val allConversions = state.units.map { unit ->
            val converted = UnitConverter.convert(inputValue, from, unit)
            unit to formatResult(converted)
        }

        _uiState.update {
            it.copy(result = resultStr, allConversions = allConversions)
        }

        viewModelScope.launch {
            conversionDao.insertConversion(
                ConversionEntity(
                    fromValue = inputValue,
                    fromUnit = from.symbol,
                    toValue = result,
                    toUnit = to.symbol,
                    category = state.category.name
                )
            )
        }
    }

    private fun formatResult(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        return if (value == value.toLong().toDouble() && kotlin.math.abs(value) < 1e15) {
            value.toLong().toString()
        } else {
            val formatted = String.format("%.8g", value)
            formatted.trimEnd('0').trimEnd('.')
        }
    }
}
