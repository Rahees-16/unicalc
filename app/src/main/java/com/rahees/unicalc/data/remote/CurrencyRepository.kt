package com.rahees.unicalc.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val api: CurrencyApi,
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) {
    companion object {
        private val CACHED_RATES_KEY = stringPreferencesKey("cached_rates")
        private val CACHED_BASE_KEY = stringPreferencesKey("cached_base")
        private val CACHED_DATE_KEY = stringPreferencesKey("cached_date")
        private val CACHED_TIMESTAMP_KEY = longPreferencesKey("cached_timestamp")
        private const val CACHE_DURATION_MS = 3600_000L // 1 hour
    }

    data class CurrencyResult(
        val rates: Map<String, Double>,
        val base: String,
        val date: String,
        val isFromCache: Boolean
    )

    suspend fun getRates(base: String = "USD"): CurrencyResult {
        val prefs = dataStore.data.first()
        val cachedTimestamp = prefs[CACHED_TIMESTAMP_KEY] ?: 0L
        val cachedBase = prefs[CACHED_BASE_KEY] ?: ""
        val now = System.currentTimeMillis()

        if (now - cachedTimestamp < CACHE_DURATION_MS && cachedBase == base) {
            val cachedRatesJson = prefs[CACHED_RATES_KEY] ?: ""
            val cachedDate = prefs[CACHED_DATE_KEY] ?: ""
            if (cachedRatesJson.isNotEmpty()) {
                val type = object : TypeToken<Map<String, Double>>() {}.type
                val rates: Map<String, Double> = gson.fromJson(cachedRatesJson, type)
                return CurrencyResult(rates, base, cachedDate, isFromCache = true)
            }
        }

        return try {
            val response = api.getLatestRates(base)
            dataStore.edit { settings ->
                settings[CACHED_RATES_KEY] = gson.toJson(response.rates)
                settings[CACHED_BASE_KEY] = base
                settings[CACHED_DATE_KEY] = response.date
                settings[CACHED_TIMESTAMP_KEY] = now
            }
            CurrencyResult(response.rates, response.base, response.date, isFromCache = false)
        } catch (e: Exception) {
            val cachedRatesJson = prefs[CACHED_RATES_KEY] ?: ""
            val cachedDate = prefs[CACHED_DATE_KEY] ?: ""
            if (cachedRatesJson.isNotEmpty()) {
                val type = object : TypeToken<Map<String, Double>>() {}.type
                val rates: Map<String, Double> = gson.fromJson(cachedRatesJson, type)
                CurrencyResult(rates, cachedBase, cachedDate, isFromCache = true)
            } else {
                throw e
            }
        }
    }
}
