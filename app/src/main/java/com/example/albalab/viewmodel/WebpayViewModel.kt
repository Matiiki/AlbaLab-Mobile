package com.example.albalab.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.model.ConfirmarPagoRequest
import com.example.albalab.model.WebpayRequest
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.launch

/**
 * Conserva la transacción de Webpay mientras la pantalla se recrea.
 * La creación y la confirmación se ejecutan en viewModelScope, por lo que
 * girar el dispositivo no cancela la operación ni crea un segundo pago.
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

    private var pagoIdActual: Int? = null

    private var pagoAprobado = false

    fun iniciarPago(
        pagoId: Int,
        montoTotal: Double,
        tokenUsuario: String,
        concepto: String
    ) {
        // Si es el mismo pago, ya se inició antes (por ejemplo, antes de rotar).
        if (pagoIdActual == pagoId) {
            return
        }

        pagoIdActual = pagoId
        pagoAprobado = false
        webpayUrl = null
        webpayToken = null
        mensajeError = null
        confirmando = false
        cargando = true

        if (tokenUsuario.isBlank()) {
            mensajeError = "Debes iniciar sesión para pagar con Webpay."
            cargando = false
            return
        }

        val monto = montoTotal
        val token = tokenUsuario
        val tipo = concepto
        val idSolicitud = pagoId

        viewModelScope.launch {
            try {
                val tiempoActual = System.currentTimeMillis().toString()

                val request = WebpayRequest(
                    ordenCompra = "$tipo-$tiempoActual",
                    idSesion = "SESION-$tiempoActual",
                    monto = monto,
                    urlRetorno = RetrofitClient.WEBPAY_RETURN_URL
                )

                val response = RetrofitClient.apiService.crearTransaccionWebpay(
                    authorization = "Bearer $token",
                    request = request
                )

                if (pagoIdActual != idSolicitud) {
                    return@launch
                }

                if (response.url.isBlank() || response.token.isBlank()) {
                    mensajeError = "El backend respondió sin URL o token de Webpay."
                } else {
                    webpayUrl = response.url
                    webpayToken = response.token
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (pagoIdActual == idSolicitud) {
                    mensajeError = e.message ?: "Error desconocido al conectar con el backend."
                }
            } finally {
                if (pagoIdActual == idSolicitud) {
                    cargando = false
                }
            }
        }
    }

    fun reintentar(
        montoTotal: Double,
        tokenUsuario: String,
        concepto: String
    ) {
        val pagoId = pagoIdActual ?: return
        pagoIdActual = null
        iniciarPago(
            pagoId = pagoId,
            montoTotal = montoTotal,
            tokenUsuario = tokenUsuario,
            concepto = concepto
        )
    }

    fun confirmarPago(
        tokenUsuario: String,
        onPagoAprobado: () -> Unit
    ) {
        if (confirmando || pagoAprobado) {
            return
        }

        val tokenTransaccion = webpayToken ?: return
        val token = tokenUsuario
        val idPagoConfirmado = pagoIdActual

        confirmando = true

        viewModelScope.launch {
            try {
                val resultado = RetrofitClient.apiService.confirmarTransaccionWebpay(
                    authorization = "Bearer $token",
                    request = ConfirmarPagoRequest(token = tokenTransaccion)
                )

                if (pagoIdActual != idPagoConfirmado) {
                    return@launch
                }

                if (resultado.estado == "APROBADA") {
                    pagoAprobado = true
                    onPagoAprobado()
                } else {
                    mensajeError =
                        "El pago no fue aprobado (${resultado.estado ?: "sin estado"})."
                    webpayUrl = null
                    webpayToken = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (pagoIdActual == idPagoConfirmado) {
                    mensajeError =
                        "No se pudo confirmar el pago: ${e.message ?: "error desconocido"}"
                    webpayUrl = null
                    webpayToken = null
                }
            } finally {
                if (pagoIdActual == idPagoConfirmado) {
                    confirmando = false
                }
            }
        }
    }
}
