package com.example.albalab.model

data class Colaborador(
    val nombre: String,
    val montoOculto: String = "$***",
    val fotoUri: String? = null
)