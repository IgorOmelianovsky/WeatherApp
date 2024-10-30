package com.example.p72_weather.model

import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("maxtemp_c")
    val maxtempC: Double,
    @SerializedName("mintemp_c")
    val mintempC: Double,
    @SerializedName("maxwind_kph")
    val maxwindKph: Double,
    val condition: Condition,
)
