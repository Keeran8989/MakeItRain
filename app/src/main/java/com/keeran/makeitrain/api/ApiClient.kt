package com.keeran.makeitrain.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("data/2.5/forecast?")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("APPID") app_id: String,
        @Query("cnt") cnt: Int,
        @Query("units") units: String
    ): Response<WeatherDataModel>

}