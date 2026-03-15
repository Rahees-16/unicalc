package com.rahees.unicalc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ConversionEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ConverterDatabase : RoomDatabase() {
    abstract fun conversionDao(): ConversionDao
}
