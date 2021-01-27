package com.keeran.makeitrain.api

import com.google.gson.annotations.SerializedName

class WeatherDataModel {

    @SerializedName("cnt")
    var cnt: Int = 0

    @SerializedName("list")
    var list = ArrayList<List>()
}

class List {
    @SerializedName("dt")
    var dt: Long = 0

    @SerializedName("weather")
    var weather = ArrayList<Weather>()

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("clouds")
    var clouds: Clouds? = null

    @SerializedName("pop")
    var pop: Float = 0.toFloat()

    @SerializedName("dt_txt")
    var dt_txt: String? = null
}

class Weather {

    @SerializedName("main")
    var main: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("icon")
    var icon: String? = null
}

class Clouds {
    @SerializedName("all")
    var all: Int = 0
}

class Wind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()

    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()

    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()

    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()

}

