package com.example.albalab.network

import com.example.albalab.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    // open.er-api.com: https://open.er-api.com/v6/latest/CLP
    @GET("latest/{base}")
    suspend fun obtenerTasas(
        @Path("base") base: String = "CLP"
    ): Response<ExchangeRateResponse>
}
