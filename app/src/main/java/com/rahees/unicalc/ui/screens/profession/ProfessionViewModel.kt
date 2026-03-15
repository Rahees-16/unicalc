package com.rahees.unicalc.ui.screens.profession

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ConcreteState(
    val length: String = "",
    val width: String = "",
    val depth: String = "",
    val resultCubicMeters: String = "",
    val resultCubicYards: String = ""
)

data class BrickState(
    val wallArea: String = "",
    val result: String = ""
)

data class PaintState(
    val area: String = "",
    val coverageRate: String = "10",
    val result: String = ""
)

data class OhmState(
    val voltage: String = "",
    val current: String = "",
    val resistance: String = "",
    val result: String = ""
)

data class PowerCalcState(
    val voltage: String = "",
    val current: String = "",
    val power: String = "",
    val result: String = ""
)

data class WireGaugeEntry(
    val awg: String,
    val diameterMm: String,
    val maxAmps: String
)

data class RecipeIngredient(
    val name: String = "",
    val amount: String = "",
    val unit: String = "g"
)

data class RecipeState(
    val originalServings: String = "4",
    val desiredServings: String = "4",
    val ingredients: List<RecipeIngredient> = listOf(RecipeIngredient()),
    val scaledIngredients: List<RecipeIngredient> = emptyList()
)

data class ProfessionUiState(
    val selectedTab: Int = 0,
    val concrete: ConcreteState = ConcreteState(),
    val brick: BrickState = BrickState(),
    val paint: PaintState = PaintState(),
    val ohm: OhmState = OhmState(),
    val powerCalc: PowerCalcState = PowerCalcState(),
    val recipe: RecipeState = RecipeState()
)

@HiltViewModel
class ProfessionViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfessionUiState())
    val uiState: StateFlow<ProfessionUiState> = _uiState.asStateFlow()

    val wireGaugeTable = listOf(
        WireGaugeEntry("4/0", "11.68", "230"),
        WireGaugeEntry("3/0", "10.40", "200"),
        WireGaugeEntry("2/0", "9.27", "175"),
        WireGaugeEntry("1/0", "8.25", "150"),
        WireGaugeEntry("1", "7.35", "130"),
        WireGaugeEntry("2", "6.54", "115"),
        WireGaugeEntry("4", "5.19", "85"),
        WireGaugeEntry("6", "4.11", "65"),
        WireGaugeEntry("8", "3.26", "50"),
        WireGaugeEntry("10", "2.59", "35"),
        WireGaugeEntry("12", "2.05", "25"),
        WireGaugeEntry("14", "1.63", "20"),
        WireGaugeEntry("16", "1.29", "13"),
        WireGaugeEntry("18", "1.02", "10"),
        WireGaugeEntry("20", "0.81", "7"),
        WireGaugeEntry("22", "0.64", "5")
    )

    fun onTabChange(tab: Int) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    // Concrete Calculator
    fun onConcreteChange(length: String? = null, width: String? = null, depth: String? = null) {
        _uiState.update { state ->
            val concrete = state.concrete.copy(
                length = length ?: state.concrete.length,
                width = width ?: state.concrete.width,
                depth = depth ?: state.concrete.depth
            )
            val l = concrete.length.toDoubleOrNull()
            val w = concrete.width.toDoubleOrNull()
            val d = concrete.depth.toDoubleOrNull()
            val updated = if (l != null && w != null && d != null) {
                val cubicMeters = l * w * d
                val cubicYards = cubicMeters * 1.30795
                concrete.copy(
                    resultCubicMeters = String.format("%.3f", cubicMeters),
                    resultCubicYards = String.format("%.3f", cubicYards)
                )
            } else {
                concrete.copy(resultCubicMeters = "", resultCubicYards = "")
            }
            state.copy(concrete = updated)
        }
    }

    // Brick Calculator
    fun onBrickAreaChange(area: String) {
        _uiState.update { state ->
            val areaVal = area.toDoubleOrNull()
            val result = if (areaVal != null) {
                // Standard brick: 215mm x 65mm with 10mm mortar = 225mm x 75mm
                // Bricks per m2 = 1 / (0.225 * 0.075) = ~59.26
                val bricks = (areaVal * 59.26).toLong()
                "$bricks bricks (standard 215x65mm with 10mm mortar)"
            } else ""
            state.copy(brick = BrickState(wallArea = area, result = result))
        }
    }

    // Paint Calculator
    fun onPaintChange(area: String? = null, coverageRate: String? = null) {
        _uiState.update { state ->
            val paint = state.paint.copy(
                area = area ?: state.paint.area,
                coverageRate = coverageRate ?: state.paint.coverageRate
            )
            val a = paint.area.toDoubleOrNull()
            val c = paint.coverageRate.toDoubleOrNull()
            val result = if (a != null && c != null && c > 0) {
                val liters = a / c
                String.format("%.1f liters needed", liters)
            } else ""
            state.copy(paint = paint.copy(result = result))
        }
    }

    // Ohm's Law
    fun onOhmChange(voltage: String? = null, current: String? = null, resistance: String? = null) {
        _uiState.update { state ->
            val ohm = state.ohm.copy(
                voltage = voltage ?: state.ohm.voltage,
                current = current ?: state.ohm.current,
                resistance = resistance ?: state.ohm.resistance
            )
            val v = ohm.voltage.toDoubleOrNull()
            val i = ohm.current.toDoubleOrNull()
            val r = ohm.resistance.toDoubleOrNull()

            val result = when {
                v != null && i != null -> "R = ${String.format("%.4g", v / i)} \u03A9"
                v != null && r != null -> "I = ${String.format("%.4g", v / r)} A"
                i != null && r != null -> "V = ${String.format("%.4g", i * r)} V"
                else -> ""
            }
            state.copy(ohm = ohm.copy(result = result))
        }
    }

    // Power Calculator
    fun onPowerChange(voltage: String? = null, current: String? = null, power: String? = null) {
        _uiState.update { state ->
            val pc = state.powerCalc.copy(
                voltage = voltage ?: state.powerCalc.voltage,
                current = current ?: state.powerCalc.current,
                power = power ?: state.powerCalc.power
            )
            val v = pc.voltage.toDoubleOrNull()
            val i = pc.current.toDoubleOrNull()
            val p = pc.power.toDoubleOrNull()

            val result = when {
                v != null && i != null -> "P = ${String.format("%.4g", v * i)} W"
                p != null && v != null && v != 0.0 -> "I = ${String.format("%.4g", p / v)} A"
                p != null && i != null && i != 0.0 -> "V = ${String.format("%.4g", p / i)} V"
                else -> ""
            }
            state.copy(powerCalc = pc.copy(result = result))
        }
    }

    // Recipe
    fun onRecipeServingsChange(original: String? = null, desired: String? = null) {
        _uiState.update { state ->
            val recipe = state.recipe.copy(
                originalServings = original ?: state.recipe.originalServings,
                desiredServings = desired ?: state.recipe.desiredServings
            )
            state.copy(recipe = recipe.copy(scaledIngredients = scaleIngredients(recipe)))
        }
    }

    fun onIngredientChange(index: Int, name: String? = null, amount: String? = null, unit: String? = null) {
        _uiState.update { state ->
            val ingredients = state.recipe.ingredients.toMutableList()
            if (index < ingredients.size) {
                ingredients[index] = ingredients[index].copy(
                    name = name ?: ingredients[index].name,
                    amount = amount ?: ingredients[index].amount,
                    unit = unit ?: ingredients[index].unit
                )
            }
            val recipe = state.recipe.copy(ingredients = ingredients)
            state.copy(recipe = recipe.copy(scaledIngredients = scaleIngredients(recipe)))
        }
    }

    fun addIngredient() {
        _uiState.update { state ->
            val recipe = state.recipe.copy(
                ingredients = state.recipe.ingredients + RecipeIngredient()
            )
            state.copy(recipe = recipe)
        }
    }

    fun removeIngredient(index: Int) {
        _uiState.update { state ->
            if (state.recipe.ingredients.size > 1) {
                val recipe = state.recipe.copy(
                    ingredients = state.recipe.ingredients.toMutableList().also { it.removeAt(index) }
                )
                state.copy(recipe = recipe.copy(scaledIngredients = scaleIngredients(recipe)))
            } else state
        }
    }

    private fun scaleIngredients(recipe: RecipeState): List<RecipeIngredient> {
        val original = recipe.originalServings.toDoubleOrNull() ?: return emptyList()
        val desired = recipe.desiredServings.toDoubleOrNull() ?: return emptyList()
        if (original == 0.0) return emptyList()
        val factor = desired / original
        return recipe.ingredients.map { ing ->
            val amt = ing.amount.toDoubleOrNull()
            if (amt != null) {
                val scaled = amt * factor
                val scaledStr = if (scaled == scaled.toLong().toDouble()) {
                    scaled.toLong().toString()
                } else {
                    String.format("%.2f", scaled)
                }
                ing.copy(amount = scaledStr)
            } else {
                ing
            }
        }
    }
}
