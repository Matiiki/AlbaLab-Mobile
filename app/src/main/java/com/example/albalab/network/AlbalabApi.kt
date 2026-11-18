package com.example.albalab.network

import com.example.albalab.model.LoginResponse
import com.example.albalab.model.Usuario
import okhttp3.ResponseBody // Agrega esta importación
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import com.example.albalab.model.ConfirmarPagoRequest
import com.example.albalab.model.PagoWebpayResponse
import com.example.albalab.model.WebpayRequest
import com.example.albalab.model.WebpayResponse

interface AlbalabApi {
    // Cambiamos Response<String> por Response<ResponseBody>
    @POST("api/auth/registro")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<ResponseBody>

    @POST("api/auth/login")
    suspend fun loginUsuario(@Body usuario: Usuario): Response<LoginResponse>

    @POST("api/webpay/crear")
    suspend fun crearTransaccionWebpay(
        @Header("Authorization") authorization: String,
        @Body request: WebpayRequest
    ): WebpayResponse

    @POST("api/webpay/confirmar")
    suspend fun confirmarTransaccionWebpay(
        @Header("Authorization") authorization: String,
        @Body request: ConfirmarPagoRequest
    ): PagoWebpayResponse
}