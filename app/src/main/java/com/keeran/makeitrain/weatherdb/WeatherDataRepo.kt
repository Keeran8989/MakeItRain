package com.keeran.makeitrain.weatherdb

import androidx.lifecycle.LiveData


class WeatherDataRepo(private val weatherDataDao: WeatherDataDao) {
    val allWeatherInfo: LiveData<List<WeatherData>> = weatherDataDao.getWeatherInfo()


    suspend fun insert(data: WeatherData) {
        weatherDataDao.insert(data)
    }


}