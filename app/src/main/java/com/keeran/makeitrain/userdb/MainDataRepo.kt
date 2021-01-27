package com.keeran.makeitrain.userdb

import androidx.lifecycle.LiveData


class MainDataRepo(private val mainDataDao: MainDataDao) {
    val allMainInfo: LiveData<List<MainData>> = mainDataDao.getMainInfo()


    suspend fun insert(data: MainData) {
        mainDataDao.insert(data)
    }

    suspend fun updateLocation(lon: String, lat: String) {
        mainDataDao.updateLocation(lon, lat)
    }


}