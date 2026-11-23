package com.example.albalab.data.repository

import com.example.albalab.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ImagenRepository {

    suspend fun subirImagen(archivo: File): Result<String> {
        val cloudName = RetrofitClient.CLOUDINARY_CLOUD_NAME
        val preset = RetrofitClient.CLOUDINARY_UPLOAD_PRESET

        // Si Cloudinary está configurado, se usa; si no, se usa un host sin key (uguu.se).
        if (cloudName.isNotBlank() && preset.isNotBlank()) {
            return subirCloudinary(archivo, cloudName, preset)
        }
        return subirUguu(archivo)
    }

    private suspend fun subirCloudinary(
        archivo: File,
        cloudName: String,
        preset: String
    ): Result<String> {
        return try {
            val requestBody = archivo.asRequestBody("image/*".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData(
                "file",
                archivo.name,
                requestBody
            )
            val presetBody = preset.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = RetrofitClient.cloudinaryService.subirImagen(
                cloudName,
                filePart,
                presetBody
            )

            val url = response.body()?.secure_url ?: response.body()?.url
            if (response.isSuccessful && !url.isNullOrBlank()) {
                Result.success(url)
            } else {
                Result.failure(Exception("No se pudo subir la imagen (código ${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun subirUguu(archivo: File): Result<String> {
        return try {
            val requestBody = archivo.asRequestBody("image/*".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData(
                "files[]",
                archivo.name,
                requestBody
            )

            val response = RetrofitClient.uguuService.subirArchivo(filePart)
            val body = response.body()
            val url = body?.files?.firstOrNull()?.url

            if (response.isSuccessful && body?.success == true && !url.isNullOrBlank()) {
                Result.success(url)
            } else {
                Result.failure(Exception("No se pudo subir la imagen (código ${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
