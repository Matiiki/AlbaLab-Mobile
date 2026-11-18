package com.example.albalab.model

data class FormularioModelDonaciones (
    val monto: String = "",
    val nombre: String = "",
    val fecha: Long? = System.currentTimeMillis(),
    val aceptaCamara: Boolean = false,
    val esAnonimo: Boolean? = null,
    val fotoUri: String? = null
)