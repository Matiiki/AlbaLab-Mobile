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
    val id: Long? = null,
    val ordenCompra: String? = null,
    val token: String = "",
    val url: String = "",
    val estado: String? = null
)

data class ConfirmarPagoRequest(
    val token: String
)

data class PagoWebpayResponse(
    val id: Long? = null,
    val ordenCompra: String? = null,
    val token: String? = null,
    val estado: String? = null,
    val monto: Double? = null,
    val montoFinal: Double? = null,
    val codigoRespuesta: Int? = null
)
