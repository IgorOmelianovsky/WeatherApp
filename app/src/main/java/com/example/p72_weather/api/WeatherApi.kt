package com.example.p72_weather.api

import com.example.p72_weather.model.MainResponse
import com.example.p72_weather.util.AppConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast.json")
    suspend fun getData(
        @Query("q") location: String,
        @Query("key") key: String = AppConstants.API_KEY,
        @Query("days") days: String = AppConstants.DAYS,
        @Query("aqi") aqi: String = AppConstants.NO,
        @Query("alerts") alerts: String = AppConstants.NO,
    ): MainResponse

}