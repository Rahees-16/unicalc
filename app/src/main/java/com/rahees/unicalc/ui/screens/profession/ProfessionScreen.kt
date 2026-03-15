package com.rahees.unicalc.ui.screens.profession

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionScreen(
    onBackClick: () -> Unit,
    viewModel: ProfessionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val tabs = listOf("Construction", "Electrical", "Cooking")

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Profession Tools") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        TabRow(selectedTabIndex = state.selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = state.selectedTab == index,
                    onClick = { viewModel.onTabChange(index) },
                    text = { Text(title) }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (state.selectedTab) {
                0 -> ConstructionTab(state, viewModel)
                1 -> ElectricalTab(state, viewModel)
                2 -> CookingTab(state, viewModel)
            }
        }
    }
}

@Composable
private fun ConstructionTab(state: ProfessionUiState, viewModel: ProfessionViewModel) {
    // Concrete Volume Calculator
    CalculatorCard(title = "Concrete Volume Calculator") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.concrete.length,
                onValueChange = { viewModel.onConcreteChange(length = it) },
                label = { Text("Length (m)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = state.concrete.width,
                onValueChange = { viewModel.onConcreteChange(width = it) },
                label = { Text("Width (m)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.concrete.depth,
            onValueChange = { viewModel.onConcreteChange(depth = it) },
            label = { Text("Depth (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (state.concrete.resultCubicMeters.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${state.concrete.resultCubicMeters} m\u00B3",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${state.concrete.resultCubicYards} yd\u00B3",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Brick Calculator
    CalculatorCard(title = "Brick Calculator") {
        OutlinedTextField(
            value = state.brick.wallArea,
            onValueChange = { viewModel.onBrickAreaChange(it) },
            label = { Text("Wall area (m\u00B2)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (state.brick.result.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.brick.result,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Paint Calculator
    CalculatorCard(title = "Paint Coverage Calculator") {
        OutlinedTextField(
            value = state.paint.area,
            onValueChange = { viewModel.onPaintChange(area = it) },
            label = { Text("Area (m\u00B2)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.paint.coverageRate,
            onValueChange = { viewModel.onPaintChange(coverageRate = it) },
            label = { Text("Coverage rate (m\u00B2/L)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (state.paint.result.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.paint.result,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ElectricalTab(state: ProfessionUiState, viewModel: ProfessionViewModel) {
    // Ohm's Law
    CalculatorCard(title = "Ohm's Law (V = I \u00D7 R)") {
        Text(
            text = "Enter any 2 values to calculate the 3rd",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.ohm.voltage,
            onValueChange = { viewModel.onOhmChange(voltage = it) },
            label = { Text("Voltage (V)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.ohm.current,
            onValueChange = { viewModel.onOhmChange(current = it) },
            label = { Text("Current (A)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.ohm.resistance,
            onValueChange = { viewModel.onOhmChange(resistance = it) },
            label = { Text("Resistance (\u03A9)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (state.ohm.result.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.ohm.result,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Power Calculator
    CalculatorCard(title = "Power Calculator (P = V \u00D7 I)") {
        Text(
            text = "Enter any 2 values to calculate the 3rd",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.powerCalc.voltage,
            onValueChange = { viewModel.onPowerChange(voltage = it) },
            label = { Text("Voltage (V)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.powerCalc.current,
            onValueChange = { viewModel.onPowerChange(current = it) },
            label = { Text("Current (A)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.powerCalc.power,
            onValueChange = { viewModel.onPowerChange(power = it) },
            label = { Text("Power (W)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (state.powerCalc.result.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.powerCalc.result,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Wire Gauge Reference
    CalculatorCard(title = "AWG Wire Gauge Reference") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("AWG", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Diameter (mm)", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
            Text("Max Amps", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        viewModel.wireGaugeTable.forEach { entry ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(entry.awg, modifier = Modifier.weight(1f))
                Text(entry.diameterMm, modifier = Modifier.weight(1.5f))
                Text("${entry.maxAmps} A", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CookingTab(state: ProfessionUiState, viewModel: ProfessionViewModel) {
    CalculatorCard(title = "Recipe Scaler") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.recipe.originalServings,
                onValueChange = { viewModel.onRecipeServingsChange(original = it) },
                label = { Text("Original servings") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = state.recipe.desiredServings,
                onValueChange = { viewModel.onRecipeServingsChange(desired = it) },
                label = { Text("Desired servings") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        state.recipe.ingredients.forEachIndexed { index, ingredient ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedTextField(
                    value = ingredient.name,
                    onValueChange = { viewModel.onIngredientChange(index, name = it) },
                    label = { Text("Name") },
                    modifier = Modifier.weight(2f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ingredient.amount,
                    onValueChange = { viewModel.onIngredientChange(index, amount = it) },
                    label = { Text("Amt") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ingredient.unit,
                    onValueChange = { viewModel.onIngredientChange(index, unit = it) },
                    label = { Text("Unit") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(
                    onClick = { viewModel.removeIngredient(index) },
                    enabled = state.recipe.ingredients.size > 1
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FilledTonalButton(
            onClick = { viewModel.addIngredient() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Ingredient")
        }

        if (state.recipe.scaledIngredients.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Scaled for ${state.recipe.desiredServings} servings:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            state.recipe.scaledIngredients.forEach { ing ->
                if (ing.name.isNotEmpty() || ing.amount.isNotEmpty()) {
                    Text(
                        text = "${ing.amount} ${ing.unit} ${ing.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalculatorCard(
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
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
