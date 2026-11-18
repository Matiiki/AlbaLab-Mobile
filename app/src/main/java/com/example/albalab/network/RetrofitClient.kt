package com.example.albalab.network

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // IMPORTANTE:
    // - Teléfono físico: reemplaza 192.168.1.100 por la IPv4 de tu PC.
    // - Emulador Android Studio: usa 10.0.2.2.
    private const val HOST = "192.168.1.100"

    // Todas las rutas de usuario y Webpay pasan por el API Gateway.
    const val BASE_URL = "http://$HOST:9090/"
    // El panel de administración llama directo a admin-service.
    const val ADMIN_BASE_URL = "http://$HOST:9092/"

    const val WEBPAY_RETURN_URL = BASE_URL + "api/webpay/retorno"

    // Webpay puede tardar en crear la transacción; los timeouts por defecto
    // de OkHttp (10 s) son demasiado cortos para la primera llamada.
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val apiService: AlbalabApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlbalabApi::class.java)
    }

    val adminApiService: AdminApi by lazy {
        Retrofit.Builder()
            .baseUrl(ADMIN_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdminApi::class.java)
    }
}
