package com.example.p72_weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p72_weather.model.AdapterModel
import com.example.p72_weather.model.MainResponse
import com.example.p72_weather.repository.Repository
import com.example.p72_weather.util.Resource
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _currentResponse = MutableLiveData<Resource>()
    val currentResponse: LiveData<Resource> get() = _currentResponse

    private val _currentDay = MutableLiveData(0)
    val currentDay: LiveData<Int> get() = _currentDay

    private val _tabLayout = MutableLiveData<TabLayout>()

    fun setTabLayout(tabLayout: TabLayout) {
        _tabLayout.value = tabLayout
    }

    fun getTabLayout() = _tabLayout.value!!

    fun setCurrentDay(day: Int) {
        _currentDay.value = day
    }

    fun getData(location: String) = viewModelScope.launch(Dispatchers.IO) {
        _currentResponse.postValue(Resource.Loading)
        try {
            val response = repository.getData(location)
            _currentResponse.postValue(Resource.Success(response))
        } catch (exception: Exception) {
            exception.message?.let {
                _currentResponse.postValue(Resource.Error(it))
            }
        }
    }

    fun getAdapterListForHours(response: MainResponse, currentDay: Int): List<AdapterModel> {
        val list = mutableListOf<AdapterModel>()

        for (item in response.forecast.forecastday[currentDay].hour) {
            val temp = "${item.tempC} °C"
            val icon = "https:${item.condition.icon}"
            val wind = "Wind: ${item.windKph} km/h"

            val model = AdapterModel(
                item.time,
                temp,
                wind,
                item.condition.text,
                icon
            )

            list += model
        }

        return list
    }

    fun getAdapterListForDays(response: MainResponse): List<AdapterModel> {
        val list = mutableListOf<AdapterModel>()

        for (item in response.forecast.forecastday) {
            val maxTemp = item.day.maxtempC
            val minTemp = item.day.mintempC
            val temp = "max: $maxTemp °C / min: $minTemp °C"

            val icon = "https:${item.day.condition.icon}"
            val wind = "Wind: ${item.day.maxwindKph} km/h"

            val model = AdapterModel(
                item.date,
                temp,
                wind,
                item.day.condition.text,
                icon
            )

            list += model
        }

        return list
    }

}