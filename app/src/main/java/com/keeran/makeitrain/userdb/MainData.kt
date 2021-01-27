package com.keeran.makeitrain.userdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MainDB")
data class MainData(
    @ColumnInfo(name = "uid") var uid: String,
    @ColumnInfo(name = "lon") var lon: String,
    @ColumnInfo(name = "lat") var lat: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    //...
}