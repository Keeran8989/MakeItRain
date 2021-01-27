package com.keeran.makeitrain.weatherdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keeran.makeitrain.weatherdb.WeatherDataRoomDB.Companion.getDatabase
import kotlinx.coroutines.launch

class WeatherDataViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WeatherDataRepo

    // LiveData gives us updated words when they change.
    val allWeatherData: LiveData<List<WeatherData>>
    //val allGoalsNL: LiveData<List<GoalsData>>


    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val weatherDataDao = getDatabase(application, viewModelScope).weatherDataDAO()
        //val goalsDaoNL = GoalsRoomDB.getDatabase(application, viewModelScope).goalDao()
        repository = WeatherDataRepo(weatherDataDao)
        allWeatherData = repository.allWeatherInfo

    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the weather thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(data: WeatherData) = viewModelScope.launch {
        repository.insert(data)
    }


}