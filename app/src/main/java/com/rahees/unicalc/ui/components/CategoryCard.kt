package com.rahees.unicalc.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rahees.unicalc.domain.UnitCategory

fun getCategoryIcon(category: UnitCategory): ImageVector {
    return when (category) {
        UnitCategory.LENGTH -> Icons.Default.Straighten
        UnitCategory.WEIGHT -> Icons.Default.FitnessCenter
        UnitCategory.TEMPERATURE -> Icons.Default.Thermostat
        UnitCategory.AREA -> Icons.Default.SquareFoot
        UnitCategory.VOLUME -> Icons.Default.WaterDrop
        UnitCategory.SPEED -> Icons.Default.Speed
        UnitCategory.TIME -> Icons.Default.Schedule
        UnitCategory.DATA -> Icons.Default.Storage
        UnitCategory.PRESSURE -> Icons.Default.Compress
        UnitCategory.ENERGY -> Icons.Default.Bolt
        UnitCategory.POWER -> Icons.Default.Power
        UnitCategory.FUEL -> Icons.Default.LocalGasStation
        UnitCategory.ANGLE -> Icons.Default.Architecture
        UnitCategory.FREQUENCY -> Icons.Default.GraphicEq
        UnitCategory.COOKING -> Icons.Default.Restaurant
    }
}

@Composable
fun CategoryCard(
    category: UnitCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = getCategoryIcon(category),
                contentDescription = category.displayName,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 2
            )
        }
    }
}
