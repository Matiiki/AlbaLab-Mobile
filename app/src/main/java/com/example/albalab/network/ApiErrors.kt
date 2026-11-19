package com.example.albalab.network

import com.example.albalab.model.ApiProblemDetail
import com.google.gson.Gson
import retrofit2.Response

/**
 * Extrae el mensaje legible que envía el backend en los errores 400/409.
 * Spring Boot responde con ProblemDetail; algunos servicios pueden usar
 * simplemente "message".
 */
fun Response<*>.mensajeDeError(predeterminado: String): String {
    val cuerpo = errorBody()?.string()

    if (cuerpo.isNullOrBlank()) {
        return predeterminado
    }

    return runCatching {
        val problema = Gson().fromJson(cuerpo, ApiProblemDetail::class.java)

        problema?.detail?.takeIf { it.isNotBlank() }
            ?: problema?.message?.takeIf { it.isNotBlank() }
            ?: problema?.errores?.joinToString("\n")?.takeIf { it.isNotBlank() }
            ?: predeterminado
    }.getOrDefault(predeterminado)
}
