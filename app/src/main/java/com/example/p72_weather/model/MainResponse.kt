package com.example.p72_weather.model

data class MainResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast,
)
