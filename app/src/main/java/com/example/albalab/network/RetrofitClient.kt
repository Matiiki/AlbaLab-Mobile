package com.example.albalab.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // IMPORTANTE:
    // - Teléfono físico: reemplaza 192.168.1.100 por la IPv4 de tu PC.
    // - Emulador Android Studio: usa http://10.0.2.2:9091/
    const val BASE_URL = "http://192.168.1.100:9091/"

    const val WEBPAY_RETURN_URL = BASE_URL + "api/webpay/retorno"

    val apiService: AlbalabApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlbalabApi::class.java)
    }
}
