package com.rahees.unicalc.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.rahees.unicalc.domain.UnitConverter
import com.rahees.unicalc.domain.UnitData
import com.rahees.unicalc.domain.UnitCategory

class ConverterWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val units = UnitData.units[UnitCategory.LENGTH] ?: emptyList()
        val fromUnit = units.firstOrNull()
        val toUnit = units.getOrNull(4) // Mile

        val inputValue = 1.0
        val result = if (fromUnit != null && toUnit != null) {
            UnitConverter.convert(inputValue, fromUnit, toUnit)
        } else {
            0.0
        }

        val resultStr = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.4f", result)
        }

        provideContent {
            GlanceTheme {
                WidgetContent(
                    fromLabel = fromUnit?.symbol ?: "m",
                    toLabel = toUnit?.symbol ?: "mi",
                    inputValue = inputValue.toLong().toString(),
                    resultValue = resultStr
                )
            }
        }
    }
}

@Composable
private fun WidgetContent(
    fromLabel: String,
    toLabel: String,
    inputValue: String,
    resultValue: String
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "UniCalc",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.primary
            )
        )

        Spacer(modifier = GlanceModifier.height(8.dp))

        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = inputValue,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.onSurface
                    )
                )
                Text(
                    text = fromLabel,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = GlanceModifier.width(16.dp))

            Text(
                text = "=",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = GlanceTheme.colors.onSurfaceVariant
                )
            )

            Spacer(modifier = GlanceModifier.width(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = resultValue,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.primary
                    )
                )
                Text(
                    text = toLabel,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.onSurfaceVariant
                    )
                )
            }
        }
    }
}
