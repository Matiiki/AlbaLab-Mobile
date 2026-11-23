package com.example.albalab.network

import com.example.albalab.model.UguuUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UguuApi {
    @Multipart
    @POST("upload.php")
    suspend fun subirArchivo(
        @Part files: MultipartBody.Part
    ): Response<UguuUploadResponse>
}
