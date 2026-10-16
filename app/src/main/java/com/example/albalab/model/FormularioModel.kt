package com.example.albalab.model

data class FormularioModel (
    val nombre: String = "",
    val motivo: String = "",
    val aceptaTerminos: Boolean = false,
    val activo: Boolean = false,
    val fecha: Long? = null,
    val lugar: String = ""
)
