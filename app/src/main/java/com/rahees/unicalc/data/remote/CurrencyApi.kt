package com.rahees.unicalc.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("v1/latest")
    suspend fun getLatestRates(@Query("base") base: String = "USD"): CurrencyResponse
}
