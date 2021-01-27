package com.keeran.makeitrain.weatherdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WeatherDB")
data class WeatherData(
    @ColumnInfo(name = "dt")
    var dt: Long = 0,
    @ColumnInfo(name = "pop")
    var pop: Float = 0.toFloat(),
    @ColumnInfo(name = "dt_txt")
    var dt_txt: String? = null,

    @ColumnInfo(name = "main")
    var main: String? = null,
    @ColumnInfo(name = "description")
    var description: String? = null,
    @ColumnInfo(name = "icon")
    var icon: String? = null,
    @ColumnInfo(name = "all")
    var all: Int = 0,

    @ColumnInfo(name = "temp")
    var temp: Float = 0.toFloat(),
    @ColumnInfo(name = "humidity")
    var humidity: Float = 0.toFloat(),
    @ColumnInfo(name = "pressure")
    var temp_min: Float = 0.toFloat(),
    @ColumnInfo(name = "temp_max")
    var temp_max: Float = 0.toFloat(),
    @ColumnInfo(name = "speed")
    var speed: Float = 0.toFloat(),

    ) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
    //...
}