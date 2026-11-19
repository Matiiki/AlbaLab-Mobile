package com.example.albalab.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.model.ColaboradorRequest
import com.example.albalab.model.ConfirmarPagoRequest
import com.example.albalab.model.WebpayRequest
import com.example.albalab.network.RetrofitClient
import com.example.albalab.network.mensajeDeError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Conserva la transacción de Webpay y el registro de la colaboración
 * mientras la pantalla se recrea. La creación, la confirmación y el POST
 * de la colaboración se ejecutan en viewModelScope.
 */
class WebpayViewModel : ViewModel() {

    var webpayUrl by mutableStateOf<String?>(null)
        private set

    var webpayToken by mutableStateOf<String?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    var confirmando by mutableStateOf(false)
        private set

    var pagoAprobado by mutableStateOf(false)
        private set

    var guardandoColaboracion by mutableStateOf(false)
        private set

    var errorColaboracion by mutableStateOf<String?>(null)
        private set

    /**
     * Solo se pone en true cuando el pago está aprobado y, si corresponde,
     * la colaboración ya fue guardada correctamente en el backend.
     */
    var pagoTerminado by mutableStateOf(false)
        private set

    var sesionExpirada by mutableStateOf(false)
        private set

    private var pagoIdActual: Int? = null
    private var colaboracionGuardada = false
    private var confirmacionRechazada = false
    private var ordenCompraActual: String? = null
    private var idSesionActual: String? = null
    private var tokenUsuarioActual: String? = null
    private var montoActual: Double = 0.0
    private var colaboracionPendiente: ColaboradorRequest? = null

    private var trabajoCreacion: Job? = null
    private var trabajoConfirmacion: Job? = null
    private var trabajoGuardado: Job? = null

    /**
     * Inicia el pago. Para donaciones, [ordenCompra] debe ser exactamente
     * la misma orden que luego se envía a POST /api/colaboradores.
     */
    fun iniciarPago(
        pagoId: Int,
        montoTotal: Double,
        tokenUsuario: String,
        ordenCompra: String? = null,
        colaboracion: ColaboradorRequest? = null
    ) {
        // Si es el mismo pago, ya se inició antes (por ejemplo, antes de rotar).
        if (pagoIdActual == pagoId) {
            return
        }

        cancelarTrabajos()

        pagoIdActual = pagoId
        montoActual = montoTotal
        tokenUsuarioActual = tokenUsuario
        colaboracionPendiente = colaboracion
        colaboracionGuardada = false
        confirmacionRechazada = false
        pagoAprobado = false
        guardandoColaboracion = false
        errorColaboracion = null
        pagoTerminado = false
        sesionExpirada = false
        webpayUrl = null
        webpayToken = null
        mensajeError = null
        confirmando = false
        cargando = false

        ordenCompraActual =
            ordenCompra
                ?: "ORDEN-${System.currentTimeMillis()}"

        idSesionActual =
            "SESION-${System.currentTimeMillis()}"

        if (tokenUsuario.isBlank()) {
            marcarSesionExpirada(
                "Debes iniciar sesión para pagar con Webpay."
            )
            return
        }

        iniciarCreacion()
    }

    private fun iniciarCreacion() {

        val orden = ordenCompraActual ?: return
        val sesion = idSesionActual ?: return
        val token = tokenUsuarioActual ?: return
        val idSolicitud = pagoIdActual ?: return

        if (token.isBlank()) {
            marcarSesionExpirada(
                "Debes iniciar sesión para pagar con Webpay."
            )
            return
        }

        trabajoCreacion?.cancel()
        cargando = true
        mensajeError = null
        webpayUrl = null
        webpayToken = null

        trabajoCreacion = viewModelScope.launch {
            try {

                val request = WebpayRequest(
                    ordenCompra = orden,
                    idSesion = sesion,
                    monto = montoActual,
                    urlRetorno = RetrofitClient.WEBPAY_RETURN_URL
                )

                val response = RetrofitClient.apiService.crearTransaccionWebpay(
                    authorization = "Bearer $token",
                    request = request
                )

                if (pagoIdActual != idSolicitud) {
                    return@launch
                }

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body == null || body.url.isBlank() || body.token.isBlank()) {
                        mensajeError =
                            "El backend respondió sin URL o token de Webpay."
                    } else {
                        webpayUrl = body.url
                        webpayToken = body.token

                        // El backend devuelve la misma orden; conservarla
                        // evita diferencias al registrar la colaboración.
                        body.ordenCompra?.takeIf { it.isNotBlank() }?.let {
                            ordenCompraActual = it
                        }
                    }

                } else if (response.code() == 401) {

                    marcarSesionExpirada(
                        "Tu sesión expiró. Inicia sesión nuevamente."
                    )

                } else {

                    mensajeError = response.mensajeDeError(
                        "No se pudo crear la transacción de Webpay."
                    )
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (pagoIdActual == idSolicitud) {
                    mensajeError =
                        "No se pudo conectar con Webpay. Revisa la conexión e intenta nuevamente."
                }

            } finally {

                if (pagoIdActual == idSolicitud) {
                    cargando = false
                }
            }
        }
    }

    /**
     * Confirma el pago con el token devuelto por Webpay.
     * El POST de la colaboración solo se ejecuta si el estado es APROBADA.
     */
    fun confirmarPago(
        tokenUsuario: String
    ) {
        if (confirmando || pagoAprobado) {
            return
        }

        val tokenTransaccion = webpayToken

        if (tokenTransaccion.isNullOrBlank()) {
            mensajeError =
                "No se recibió el token de la transacción de Webpay."
            return
        }

        if (tokenUsuario.isBlank()) {
            marcarSesionExpirada(
                "Tu sesión expiró. Inicia sesión nuevamente."
            )
            return
        }

        tokenUsuarioActual = tokenUsuario
        val idPagoConfirmado = pagoIdActual
        confirmando = true
        mensajeError = null
        confirmacionRechazada = false

        trabajoConfirmacion?.cancel()

        trabajoConfirmacion = viewModelScope.launch {
            try {

                val response =
                    RetrofitClient.apiService.confirmarTransaccionWebpay(
                        authorization = "Bearer $tokenUsuario",
                        request = ConfirmarPagoRequest(token = tokenTransaccion)
                    )

                if (pagoIdActual != idPagoConfirmado) {
                    return@launch
                }

                if (response.isSuccessful) {

                    val estado = response.body()?.estado

                    if (estado == "APROBADA") {

                        pagoAprobado = true
                        webpayUrl = null
                        mensajeError = null

                        if (colaboracionPendiente != null) {
                            guardarColaboracion()
                        } else {
                            // Pago del carrito: no se registra colaboración.
                            pagoTerminado = true
                        }

                    } else {

                        confirmacionRechazada = true
                        webpayUrl = null
                        mensajeError =
                            "El pago no fue aprobado (${estado ?: "sin estado"}). No se creó ninguna colaboración."
                    }

                } else if (response.code() == 401) {

                    marcarSesionExpirada(
                        "Tu sesión expiró. Inicia sesión nuevamente."
                    )

                } else {

                    webpayUrl = null
                    mensajeError =
                        "No se pudo confirmar el pago: " +
                            response.mensajeDeError("error desconocido")
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (pagoIdActual == idPagoConfirmado) {
                    webpayUrl = null
                    mensajeError =
                        "No se pudo confirmar el pago. Revisa la conexión e intenta nuevamente."
                }

            } finally {

                if (pagoIdActual == idPagoConfirmado) {
                    confirmando = false
                }
            }
        }
    }

    /**
     * Guarda la colaboración después de un pago APROBADA.
     * Si falla, se puede reintentar con la misma webpayOrden sin volver a cobrar.
     */
    private fun guardarColaboracion() {

        if (guardandoColaboracion || colaboracionGuardada || !pagoAprobado) {
            return
        }

        val request = colaboracionPendiente ?: run {
            pagoTerminado = true
            return
        }

        val token = tokenUsuarioActual

        if (token.isNullOrBlank()) {
            marcarSesionExpirada(
                "Tu sesión expiró. Inicia sesión nuevamente."
            )
            return
        }

        guardandoColaboracion = true
        errorColaboracion = null
        mensajeError = null

        trabajoGuardado?.cancel()

        trabajoGuardado = viewModelScope.launch {
            try {

                val response =
                    RetrofitClient.apiService.crearColaboracion(
                        authorization = "Bearer $token",
                        request = request
                    )

                if (response.isSuccessful) {

                    colaboracionGuardada = true
                    pagoTerminado = true
                    errorColaboracion = null

                } else if (response.code() == 401) {

                    marcarSesionExpirada(
                        "Tu sesión expiró. Inicia sesión nuevamente."
                    )

                } else {

                    errorColaboracion = response.mensajeDeError(
                        "El pago fue aprobado, pero no se pudo registrar la colaboración."
                    )
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                errorColaboracion =
                    "El pago fue aprobado, pero no se pudo registrar la colaboración. " +
                        "Revisa la conexión y reintenta; no se volverá a cobrar."

            } finally {

                guardandoColaboracion = false
            }
        }
    }

    /**
     * Reintenta la etapa que falló:
     * - Si el pago está aprobado, reintenta solo el POST de la colaboración.
     * - Si la confirmación falló por red, reintenta la confirmación.
     * - Si la creación o el pago fue rechazado, inicia un nuevo pago.
     */
    fun reintentarProceso() {

        if (cargando || confirmando || guardandoColaboracion) {
            return
        }

        if (pagoAprobado) {
            if (colaboracionPendiente != null && !colaboracionGuardada) {
                guardarColaboracion()
            }
            return
        }

        if (!webpayToken.isNullOrBlank() && !confirmacionRechazada) {
            confirmarPago(tokenUsuarioActual.orEmpty())
            return
        }

        // La orden anterior no terminó en un pago aprobado. Se genera una
        // nueva orden para el siguiente intento.
        val nuevaOrden = if (colaboracionPendiente != null) {
            "DON-${System.currentTimeMillis()}"
        } else {
            "ORDEN-${System.currentTimeMillis()}"
        }

        ordenCompraActual = nuevaOrden
        idSesionActual = "SESION-${System.currentTimeMillis()}"
        colaboracionPendiente =
            colaboracionPendiente?.copy(
                webpayOrden = nuevaOrden
            )

        confirmacionRechazada = false
        mensajeError = null
        errorColaboracion = null
        webpayUrl = null
        webpayToken = null

        iniciarCreacion()
    }

    fun limpiarSesion() {

        cancelarTrabajos()

        pagoIdActual = null
        webpayUrl = null
        webpayToken = null
        cargando = false
        mensajeError = null
        confirmando = false
        pagoAprobado = false
        guardandoColaboracion = false
        errorColaboracion = null
        pagoTerminado = false
        sesionExpirada = false
        colaboracionGuardada = false
        confirmacionRechazada = false
        ordenCompraActual = null
        idSesionActual = null
        tokenUsuarioActual = null
        montoActual = 0.0
        colaboracionPendiente = null
    }

    private fun marcarSesionExpirada(mensaje: String) {
        mensajeError = mensaje
        sesionExpirada = true
        cargando = false
        confirmando = false
        guardandoColaboracion = false
        webpayUrl = null
        webpayToken = null
    }

    private fun cancelarTrabajos() {
        trabajoCreacion?.cancel()
        trabajoConfirmacion?.cancel()
        trabajoGuardado?.cancel()
        trabajoCreacion = null
        trabajoConfirmacion = null
        trabajoGuardado = null
    }
}
