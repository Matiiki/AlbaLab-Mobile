package com.example.albalab.data.repository

import com.example.albalab.network.RetrofitClient

class ValidacionRepository {

    suspend fun validarEmail(email: String): Result<Boolean> {
        val apiKey = RetrofitClient.ABSTRACT_EMAIL_API_KEY
        if (apiKey.isNotBlank()) {
            return validarEmailAbstract(email, apiKey)
        }
        return validarDominioEmail(email)
    }

    private suspend fun validarEmailAbstract(email: String, apiKey: String): Result<Boolean> {
        return try {
            val response = RetrofitClient.abstractEmailService.validarEmail(apiKey, email)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val valido = body.is_valid_format != false &&
                        body.is_disposable_email != true
                Result.success(valido)
            } else {
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.success(true)
        }
    }

    private suspend fun validarDominioEmail(email: String): Result<Boolean> {
        val dominio = email.substringAfter("@", "").trim()
        if (dominio.isBlank() || !dominio.contains(".")) {
            return Result.success(false)
        }
        return try {
            val response = RetrofitClient.googleDnsService.resolverMx(dominio, "MX")
            val body = response.body()
            if (response.isSuccessful && body != null) {
                // Status 0 = NOERROR. Aceptamos si el dominio resuelve (con o sin MX).
                Result.success(body.Status == 0)
            } else {
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.success(true)
        }
    }

    suspend fun validarTelefono(telefono: String): Result<Boolean> {
        val apiKey = RetrofitClient.NUMVERIFY_API_KEY
        if (apiKey.isBlank()) {
            return Result.success(true)
        }
        return try {
            val response = RetrofitClient.numVerifyService.validarTelefono(apiKey, telefono)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body.valid == true)
            } else {
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.success(true)
        }
    }
}
