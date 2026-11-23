package com.example.albalab.network

import com.example.albalab.model.EmailValidationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AbstractEmailApi {
    @GET("v1/")
    suspend fun validarEmail(
        @Query("api_key") apiKey: String,
        @Query("email") email: String
    ): Response<EmailValidationResponse>
}
