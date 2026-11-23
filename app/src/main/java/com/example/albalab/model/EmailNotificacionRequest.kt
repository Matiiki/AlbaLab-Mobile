package com.example.albalab.model

data class EmailNotificacionRequest(
    val to: String,
    val subject: String,
    val body: String
)
