package com.keeran.makeitrain.weatherdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDataDao {

    @Query("SELECT * from WeatherDB ")
    fun getWeatherInfo(): LiveData<List<WeatherData>>

    @Insert
    suspend fun insert(user: WeatherData)

    @Query("DELETE FROM WeatherDB")
    suspend fun deleteAll()


}