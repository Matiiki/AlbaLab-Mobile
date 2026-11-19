package com.example.albalab.model

/**
 * Colaboración privada devuelta por GET /api/colaboradores/mios.
 * Solo el dueño de la colaboración (o un ADMIN) puede ver estos datos.
 */
data class ColaboradorResponse(
    val id: Long = 0,
    val usuarioId: Long? = null,
    val usuarioEmail: String? = null,
    val nombre: String = "",
    val fotoUri: String? = null,
    val montoDonacion: Int = 0,
    val esAnonimo: Boolean = false,
    val fecha: String? = null,
    val estado: String? = null,
    val webpayOrden: String? = null
)

/**
 * Colaboración pública devuelta por GET /api/colaboradores/publicos.
 * El backend no entrega el monto ni los datos personales de donaciones anónimas.
 */
data class ColaboradorPublico(
    val id: Long = 0,
    val nombre: String = "",
    val fotoUri: String? = null,
    val esAnonimo: Boolean = false,
    val fecha: String? = null
)

/**
 * Cuerpo de POST /api/colaboradores. El dueño y el estado APROBADA
 * son asignados por el backend a partir del JWT.
 */
data class ColaboradorRequest(
    val nombre: String,
    val fotoUri: String? = null,
    val montoDonacion: Int,
    val esAnonimo: Boolean,
    val webpayOrden: String
)

/**
 * Formato ProblemDetail que usa Spring Boot para los errores 400/409.
 */
data class ApiProblemDetail(
    val title: String? = null,
    val detail: String? = null,
    val message: String? = null,
    val errores: List<String>? = null
)
