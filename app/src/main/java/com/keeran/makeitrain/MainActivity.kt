package com.keeran.makeitrain

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.MasterKeys
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.keeran.makeitrain.api.ApiAdapter
import com.keeran.makeitrain.userdb.MainDataRoomDB
import com.keeran.makeitrain.userdb.MainDataViewModel
import com.keeran.makeitrain.utilities.InternetCheck
import com.keeran.makeitrain.weatherdb.WeatherData
import com.keeran.makeitrain.weatherdb.WeatherDataRoomDB
import com.keeran.makeitrain.weatherdb.WeatherDataViewModel
import com.keeran.makeitrain.weathermodel.WeatherForecastAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    private lateinit var mainViewModel: MainDataViewModel
    private lateinit var weatherViewModel: WeatherDataViewModel
    private lateinit var maindb: MainDataRoomDB
    private lateinit var weatherdb: WeatherDataRoomDB
    private lateinit var textView_main_mainWeatherTemp: TextView
    private lateinit var textView_main_mainLocation: TextView
    private lateinit var textView_main_mainWeatherTitle: TextView
    private lateinit var recyclerView_main_forecastWeatherInfo: RecyclerView
    private lateinit var weatherForecastAdapter: WeatherForecastAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    val currentTime = System.currentTimeMillis() / 1000L

    companion object {
        var BaseUrl = "http://api.openweathermap.org/"
        var appId = "993c3c4329a8cb656a939d06beec3a49"
        var lat = "35"
        var lon = "139"

        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        maindb = MainDataRoomDB.getInstance(applicationContext)
        weatherdb = WeatherDataRoomDB.getInstance(applicationContext)
        textView_main_mainWeatherTemp = findViewById(R.id.textView_main_mainWeatherTemp)
        textView_main_mainLocation = findViewById(R.id.textView_main_mainLocation)
        textView_main_mainWeatherTitle = findViewById(R.id.textView_main_mainWeatherTitle)
        recyclerView_main_forecastWeatherInfo =
            findViewById(R.id.recyclerView_main_forecastWeatherInfo)
        weatherForecastAdapter = WeatherForecastAdapter(this, this)
        gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView_main_forecastWeatherInfo.layoutManager = gridLayoutManager
        weatherForecastAdapter.notifyDataSetChanged()

        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if (internet == true) {
                    getCurrentLocation()
                } else {
                    getStoredLocation()
                }
            }
        })


    }


    fun callWeatherAPI(lat: String, long: String, context: MainActivity) {
        scope.launch(Dispatchers.Main) {
            // Try catch block to handle exceptions when calling the API.

            try {
                val response = ApiAdapter.apiClient.getWeatherData(lat, long, appId, 40, "metric")
                // Check if response was successful

                if (response.isSuccessful && response.body() != null) {
                    var dt: Long = 0
                    var pop: Float = 0.toFloat()
                    var dt_txt: String? = null
                    var main: String? = null
                    var description: String? = null
                    var icon: String? = null
                    var all: Int = 0
                    var r3h: Float = 0.toFloat()
                    var temp: Float = 0.toFloat()
                    var humidity: Float = 0.toFloat()
                    var temp_min: Float = 0.toFloat()
                    var temp_max: Float = 0.toFloat()
                    var speed: Float = 0.toFloat()
                    val data = response.body()!!

                    data.list.forEach {
                        pop = it.pop
                        dt_txt = it.dt_txt
                        dt = it.dt
                        all = it.clouds!!.all
                        speed = it.wind!!.speed
                        temp = it.main!!.temp
                        humidity = it.main!!.humidity
                        temp_min = it.main!!.temp_min
                        temp_max = it.main!!.temp_max

                        Log.i("WeatherCheck2", dt_txt.toString())
                        for (weather in it.weather) {
                            description = weather.description
                            main = weather.main
                            icon = weather.icon
                            val weatherData = WeatherData(
                                dt,
                                pop,
                                dt_txt,
                                main,
                                description,
                                icon,
                                all,
                                temp,
                                humidity,
                                temp_min,
                                temp_max,
                                speed
                            )

                            storeWeatherData(weatherData, context)


                            //WeatherDataViewModel(Application()).insert(weatherData)

                        }
                    }


                    callStoredWeatherInfo()

                } else {
                    Log.i("OMErrorCheck1", response.message())
                    Toast.makeText(
                        this@MainActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Log.i("OMErrorCheck2", e.message.toString())

            }

        }
    }

    fun storeWeatherData(data: WeatherData, context: MainActivity) {
        val callable = Callable {
            weatherViewModel = ViewModelProvider(context).get(WeatherDataViewModel::class.java)

            weatherViewModel.insert(data)
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        future.get()
    }

    fun storeLocationData(lon: String, lat: String, context: MainActivity) {
        val callable = Callable {
            mainViewModel = ViewModelProvider(this).get(MainDataViewModel::class.java)
            mainViewModel.updateLocation(lon, lat)
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        future.get()
    }

    fun permissionRequest(){
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), 99
        )

    }
//
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        }else{
            permissionRequest()
        }
    }


    fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionRequest()

        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->

                    storeLocationData(
                        location!!.latitude.toString(),
                        location.latitude.toString(),
                        this
                    )
                    callWeatherAPI(
                        location!!.latitude.toString(),
                        location.latitude.toString(),
                        this
                    )

                }
        }
    }

    fun getStoredLocation() {
        mainViewModel = ViewModelProvider(this).get(MainDataViewModel::class.java)
        mainViewModel.allMainData.observe(this, { info ->
            info?.let {
                for (i in it) {
                    callStoredWeatherInfo()
                }
            }
        })
    }

    fun callStoredWeatherInfo() {

        weatherViewModel = ViewModelProvider(this).get(WeatherDataViewModel::class.java)
        weatherViewModel.allWeatherData.observe(this, { info ->
            info?.let {
                val dailyList = arrayListOf<WeatherData>()
                for (i in it) {
                    val sdfDay = SimpleDateFormat("dd")
                    val sdfHour = SimpleDateFormat("hh")
                    val currentDay: String? = sdfDay.format(Date())
                    val currentHour: String? = sdfHour.format(Date())
                    val itemHour = i.dt_txt!![11].toString() + i.dt_txt!![12].toString()
                    val itemDay = i.dt_txt!![8].toString() + i.dt_txt!![9].toString()

                    if (currentDay == itemDay && itemHour == currentHour || itemHour.toInt() + 1 == currentHour!!.toInt() || itemHour.toInt() + 2 == currentHour!!.toInt()) {

                        textView_main_mainWeatherTitle.text = i.main
                        textView_main_mainLocation.text =
                            i.description.toString().capitalize(Locale.ROOT)
                        val temp = i.temp.toInt().toString()
                        textView_main_mainWeatherTemp.text = temp + getString(R.string.degree)


                        val tempMin = i.temp_min.toInt().toString()
                        val tempMax = i.temp_max.toInt().toString()
                        textView_main_otherDetailsTempData.text =
                            tempMin + " - " + tempMax + getString(R.string.degree)

                        textView_main_otherDetailsHumidityData.text = i.humidity.toString()
                        textView_main_otherDetailsPrecipitationData.text = i.speed.toString()

                        imageView_main_otherDetailsTemp.setImageDrawable(getDrawable(R.mipmap.temp_icon))
                        imageView_main_otherDetailsHumidity.setImageDrawable(getDrawable(R.mipmap.humidity_icon))
                        imageView_main_otherDetailsPrecipitation.setImageDrawable(getDrawable(R.mipmap.windspeed_icon))

                    }

                    if (!dailyList.contains(i) && itemHour == "12") {
                        dailyList.add(i)
                    }


                }

                weatherForecastAdapter.setBlocks(dailyList)
                recyclerView_main_forecastWeatherInfo.adapter = weatherForecastAdapter
                weatherForecastAdapter.notifyDataSetChanged()
            }

        })
    }


}