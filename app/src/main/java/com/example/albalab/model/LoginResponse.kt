package com.example.albalab.model

data class LoginResponse(
    val status: String? = null,
    val token: String? = null,
    val role: String? = null,
    val email: String? = null,
    val expiresInSeconds: Long = 0
)
