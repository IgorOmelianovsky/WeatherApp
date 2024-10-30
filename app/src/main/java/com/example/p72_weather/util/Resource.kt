package com.example.p72_weather.util

import com.example.p72_weather.model.MainResponse

sealed class Resource {
    data object Loading : Resource()
    data class Success(val response: MainResponse) : Resource()
    data class Error(val message: String) : Resource()
}