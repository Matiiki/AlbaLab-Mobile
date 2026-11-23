package com.example.albalab.network

import com.example.albalab.model.CloudinaryUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CloudinaryApi {
    @Multipart
    @POST("{cloudName}/image/upload")
    suspend fun subirImagen(
        @Path("cloudName") cloudName: String,
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody
    ): Response<CloudinaryUploadResponse>
}
