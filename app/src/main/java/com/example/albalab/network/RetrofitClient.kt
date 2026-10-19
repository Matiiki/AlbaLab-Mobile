package com.example.albalab.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Reemplaza esta IP con tu Dirección IPv4 exacta
    private const val BASE_URL = "http://192.168.1.100:9091/" // cambiarrrr

    val apiService: AlbalabApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlbalabApi::class.java)
    }
}