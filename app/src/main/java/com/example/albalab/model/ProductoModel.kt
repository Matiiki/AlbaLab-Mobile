package com.example.albalab.model

data class ProductoModel(
    val id: Long = 0L,
    val nombre: String,
    val precio: Int,
    val descripcion: String? = null,
    val imagenUrl: String? = null,
    val disponible: Boolean = true,
    val imagenLocal: Int? = null
)

data class ProductoRequest(
    val nombre: String,
    val precio: Int,
    val descripcion: String? = null,
    val imagenUrl: String? = null,
    val disponible: Boolean = true
)
