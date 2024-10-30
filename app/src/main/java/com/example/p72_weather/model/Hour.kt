package com.example.p72_weather.model

import com.google.gson.annotations.SerializedName

data class Hour(
    val time: String,
    @SerializedName("temp_c")
    val tempC: Double,
    val condition: Condition,
    @SerializedName("wind_kph")
    val windKph: Double,
)
