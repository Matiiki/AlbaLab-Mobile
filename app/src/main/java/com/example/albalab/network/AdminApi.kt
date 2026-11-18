package com.example.albalab.network

import com.example.albalab.model.AdminLoginRequest
import com.example.albalab.model.AdminLoginResponse
import com.example.albalab.model.AdminRequest
import com.example.albalab.model.AdminUpdateRequest
import com.example.albalab.model.Administrador
import com.example.albalab.model.MensajeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdminApi {

    @POST("api/admin/auth/login")
    suspend fun loginAdmin(
        @Body request: AdminLoginRequest
    ): Response<AdminLoginResponse>

    @GET("api/admin")
    suspend fun listarAdmins(
        @Header("Authorization") authorization: String
    ): Response<List<Administrador>>

    @POST("api/admin")
    suspend fun crearAdmin(
        @Header("Authorization") authorization: String,
        @Body request: AdminRequest
    ): Response<MensajeResponse>

    @PUT("api/admin/{id}")
    suspend fun actualizarAdmin(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long,
        @Body request: AdminUpdateRequest
    ): Response<Administrador>

    @DELETE("api/admin/{id}")
    suspend fun eliminarAdmin(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    ): Response<Unit>
}
