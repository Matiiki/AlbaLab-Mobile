package com.example.albalab.model

data class HistorialCompraResponse(
    val id: Long = 0L,
    val usuarioEmail: String = "",
    val fecha: Long = 0L,
    val total: Int = 0,
    val productos: String = ""
)
