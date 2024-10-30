package com.example.p72_weather.model

data class Forecastday(
    val date: String,
    val day: Day,
    val hour: List<Hour>,
)
