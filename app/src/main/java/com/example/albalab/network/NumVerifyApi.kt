package com.example.albalab.network

import com.example.albalab.model.PhoneValidationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NumVerifyApi {
    @GET("api/validate")
    suspend fun validarTelefono(
        @Query("access_key") accessKey: String,
        @Query("number") number: String
    ): Response<PhoneValidationResponse>
}
