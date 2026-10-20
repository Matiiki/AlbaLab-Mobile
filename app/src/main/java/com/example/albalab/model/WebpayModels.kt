package com.example.albalab.model

// Lo que le enviamos al backend (el total a pagar)
data class WebpayRequest(
    val monto: Double
)

// Lo que nos responde el backend (la URL de Transbank y el token de la compra)
data class WebpayResponse(
    val url: String,
    val token: String
)