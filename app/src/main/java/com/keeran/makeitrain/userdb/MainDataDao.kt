package com.keeran.makeitrain.userdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MainDataDao {

    @Query("SELECT * from MainDB ")
    fun getMainInfo(): LiveData<List<MainData>>

    @Insert
    suspend fun insert(user: MainData)

    @Query("DELETE FROM MainDB")
    suspend fun deleteAll()

    @Query("UPDATE MainDB SET lon = :lon, lat = :lat")
    suspend fun updateLocation(lon: String, lat: String)


}