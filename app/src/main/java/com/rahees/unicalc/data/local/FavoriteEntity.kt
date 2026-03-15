package com.rahees.unicalc.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val fromUnit: String,
    val toUnit: String
)
