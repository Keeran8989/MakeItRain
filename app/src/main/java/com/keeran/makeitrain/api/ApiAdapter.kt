package com.keeran.makeitrain.api

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiAdapter {
    var httpBuilder = OkHttpClient.Builder()
    var certificatePinner = CertificatePinner.Builder()
        .add("api.openweathermap.org", "sha256/axmGTWYycVN5oCjh3GJrxWVndLSZjypDO6evrHMwbXg=")
        .build()
    var client = httpBuilder
        .certificatePinner(certificatePinner)
        .build()
    val apiClient: ApiClient = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.openweathermap.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)
}