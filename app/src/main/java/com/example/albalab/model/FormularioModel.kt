package com.example.albalab.model

data class FormularioModel(

    val nombre: String = "",

    val motivo: String = "",

    val cantidadParticipantes: Int? = null,

    val aceptaTerminos: Boolean = false,

    val solicitaSeguimiento: Boolean = false,

    val fecha: Long? = null,

    val lugar: String = ""
)