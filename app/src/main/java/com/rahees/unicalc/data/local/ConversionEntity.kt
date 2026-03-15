package com.rahees.unicalc.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversions")
data class ConversionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fromValue: Double,
    val fromUnit: String,
    val toValue: Double,
    val toUnit: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
