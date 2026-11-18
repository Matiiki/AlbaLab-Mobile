package com.example.albalab.model

data class AdminLoginRequest(
    val email: String,
    val password: String
)

data class AdminLoginResponse(
    val status: String? = null,
    val token: String? = null,
    val role: String? = null,
    val email: String? = null,
    val expiresInSeconds: Long = 0
)

data class Administrador(
    val id: Long,
    val email: String,
    val role: String,
    val fechaCreacion: String? = null
)

data class AdminRequest(
    val email: String,
    val password: String
)

data class AdminUpdateRequest(
    val email: String,
    val password: String? = null
)

data class MensajeResponse(
    val mensaje: String? = null
)
