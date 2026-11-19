package com.example.albalab.model

data class DonacionPendiente(
    val nombre: String,
    val fotoUri: String?,
    val esAnonimo: Boolean,
    val monto: Int,
    val fecha: Long,
    val webpayOrden: String
)
