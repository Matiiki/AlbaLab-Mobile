package com.example.albalab.network

import com.example.albalab.model.ColaboradorPublico
import com.example.albalab.model.ColaboradorRequest
import com.example.albalab.model.ColaboradorResponse
import com.example.albalab.model.ConfirmarPagoRequest
import com.example.albalab.model.LoginResponse
import com.example.albalab.model.PagoWebpayResponse
import com.example.albalab.model.Usuario
import com.example.albalab.model.WebpayRequest
import com.example.albalab.model.WebpayResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AlbalabApi {
    // Cambiamos Response<String> por Response<ResponseBody>
    @POST("api/auth/registro")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<ResponseBody>

    @POST("api/auth/login")
    suspend fun loginUsuario(@Body usuario: Usuario): Response<LoginResponse>

    @GET("api/colaboradores/mios")
    suspend fun obtenerMisColaboraciones(
        @Header("Authorization") authorization: String
    ): Response<List<ColaboradorResponse>>

    @GET("api/colaboradores/publicos")
    suspend fun obtenerColaboradoresPublicos(): Response<List<ColaboradorPublico>>

    @POST("api/colaboradores")
    suspend fun crearColaboracion(
        @Header("Authorization") authorization: String,
        @Body request: ColaboradorRequest
    ): Response<ColaboradorResponse>

    @POST("api/webpay/crear")
    suspend fun crearTransaccionWebpay(
        @Header("Authorization") authorization: String,
        @Body request: WebpayRequest
    ): Response<WebpayResponse>

    @POST("api/webpay/confirmar")
    suspend fun confirmarTransaccionWebpay(
        @Header("Authorization") authorization: String,
        @Body request: ConfirmarPagoRequest
    ): Response<PagoWebpayResponse>
}
