package com.example.albalab.network

import com.example.albalab.model.GoogleDnsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDnsApi {
    @GET("resolve")
    suspend fun resolverMx(
        @Query("name") name: String,
        @Query("type") type: String = "MX"
    ): Response<GoogleDnsResponse>
}
