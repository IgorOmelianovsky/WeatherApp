package com.example.p72_weather.repository

import com.example.p72_weather.api.RetrofitInstance
import com.example.p72_weather.model.MainResponse

class Repository {

    suspend fun getData(location: String): MainResponse {
        val api = RetrofitInstance.getApi()
        val response = api.getData(location)
        return response
    }

}