package com.rahees.unicalc.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversion(conversion: ConversionEntity)

    @Query("SELECT * FROM conversions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentConversions(limit: Int = 20): Flow<List<ConversionEntity>>

    @Query("DELETE FROM conversions")
    suspend fun clearHistory()

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavorite(id: Long)

    @Query("DELETE FROM favorites WHERE category = :category AND fromUnit = :fromUnit AND toUnit = :toUnit")
    suspend fun deleteFavoriteByUnits(category: String, fromUnit: String, toUnit: String)

    @Query("SELECT COUNT(*) FROM favorites WHERE category = :category AND fromUnit = :fromUnit AND toUnit = :toUnit")
    suspend fun isFavorite(category: String, fromUnit: String, toUnit: String): Int
}
