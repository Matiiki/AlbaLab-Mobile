package com.example.albalab.model

// Lo que le enviamos al backend
data class WebpayRequest(
    val ordenCompra: String,
    val idSesion: String,
    val monto: Double,
    val urlRetorno: String
)

// Lo que nos responde el backend
data class WebpayResponse(
    val url: String,
    val token: String
)

data class ConfirmarPagoRequest(
    val token: String
)

data class PagoWebpayResponse(
    val estado: String? = null
)