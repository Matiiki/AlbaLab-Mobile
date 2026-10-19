package com.example.albalab.model

// Esta clase representa el JSON que le enviaremos a Spring Boot
data class Usuario(
    val email: String,
    val password: String
)