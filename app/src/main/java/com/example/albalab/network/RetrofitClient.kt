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

    // APIs externas (sin backend propio)
    const val FRANKFURTER_BASE_URL = "https://open.er-api.com/v6/"
    const val ABSTRACT_EMAIL_BASE_URL = "https://emailvalidation.abstractapi.com/"
    const val NUMVERIFY_BASE_URL = "http://apilayer.net/"
    const val CLOUDINARY_UPLOAD_URL = "https://api.cloudinary.com/v1_1/"

    // Host de imágenes sin key (fallback funcional)
    const val UGUU_BASE_URL = "https://uguu.se/"

    // DNS-over-HTTPS (Google) para validar dominio del email sin key
    const val GOOGLE_DNS_BASE_URL = "https://dns.google/"

    // Configura tus keys/preset aquí (déjalos vacíos para desactivar la integración)
    const val ABSTRACT_EMAIL_API_KEY = ""
    const val NUMVERIFY_API_KEY = ""
    const val CLOUDINARY_CLOUD_NAME = ""
    const val CLOUDINARY_UPLOAD_PRESET = ""

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

    // El catálogo pasa por el gateway, que valida el token de administrador
    // para las operaciones de escritura.
    val productApiService: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }

    // APIs externas (sin backend propio)
    val exchangeRateService: ExchangeRateApi by lazy {
        Retrofit.Builder()
            .baseUrl(FRANKFURTER_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }

    val abstractEmailService: AbstractEmailApi by lazy {
        Retrofit.Builder()
            .baseUrl(ABSTRACT_EMAIL_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AbstractEmailApi::class.java)
    }

    val numVerifyService: NumVerifyApi by lazy {
        Retrofit.Builder()
            .baseUrl(NUMVERIFY_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NumVerifyApi::class.java)
    }

    val cloudinaryService: CloudinaryApi by lazy {
        Retrofit.Builder()
            .baseUrl(CLOUDINARY_UPLOAD_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryApi::class.java)
    }

    val uguuService: UguuApi by lazy {
        Retrofit.Builder()
            .baseUrl(UGUU_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UguuApi::class.java)
    }

    val googleDnsService: GoogleDnsApi by lazy {
        Retrofit.Builder()
            .baseUrl(GOOGLE_DNS_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleDnsApi::class.java)
    }
}

