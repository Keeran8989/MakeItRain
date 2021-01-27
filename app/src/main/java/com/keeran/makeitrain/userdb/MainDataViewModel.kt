package com.keeran.makeitrain.userdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keeran.makeitrain.userdb.MainDataRoomDB.Companion.getDatabase
import kotlinx.coroutines.launch

class MainDataViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MainDataRepo

    // LiveData gives us updated words when they change.
    val allMainData: LiveData<List<MainData>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val mainDataDao = getDatabase(application, viewModelScope).mainDataDAO()
        repository = MainDataRepo(mainDataDao)
        allMainData = repository.allMainInfo

    }

    fun insert(data: MainData) = viewModelScope.launch {
        repository.insert(data)
    }


    fun updateLocation(lon: String, lat: String) = viewModelScope.launch {
        repository.updateLocation(lon, lat)
    }


}