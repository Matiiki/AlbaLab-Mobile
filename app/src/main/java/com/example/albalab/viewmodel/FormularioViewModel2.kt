package com.example.albalab.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.model.ColaboradorPublico
import com.example.albalab.model.ColaboradorRequest
import com.example.albalab.model.ColaboradorResponse
import com.example.albalab.model.DonacionPendiente
import com.example.albalab.model.FormularioModelDonaciones
import com.example.albalab.network.RetrofitClient
import com.example.albalab.network.mensajeDeError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FormularioErrores2(
    val monto: String? = null,
    val nombre: String? = null,
    val fecha: String? = null,
    val aceptaCamara: Boolean = false
)

data class ResumenDonaciones(
    val total: Int = 0,
    val cantidad: Int = 0
)

class FormularioViewModel2 : ViewModel() {

    // -----------------------------
    // FORMULARIO
    // -----------------------------

    private val _formulario2 =
        MutableStateFlow(
            FormularioModelDonaciones()
        )

    val formulario2:
            StateFlow<FormularioModelDonaciones> =
        _formulario2.asStateFlow()

    private val _errores2 =
        MutableStateFlow(
            FormularioErrores2()
        )

    val errores2:
            StateFlow<FormularioErrores2> =
        _errores2.asStateFlow()

    private val _donacionPendiente =
        MutableStateFlow<DonacionPendiente?>(null)

    val donacionPendiente:
            StateFlow<DonacionPendiente?> =
        _donacionPendiente.asStateFlow()

    // -----------------------------
    // COLABORACIONES REMOTAS
    // -----------------------------

    private val _misColaboraciones =
        MutableStateFlow<List<ColaboradorResponse>>(emptyList())

    val misColaboraciones:
            StateFlow<List<ColaboradorResponse>> =
        _misColaboraciones.asStateFlow()

    private val _colaboradoresPublicos =
        MutableStateFlow<List<ColaboradorPublico>>(emptyList())

    val colaboradoresPublicos:
            StateFlow<List<ColaboradorPublico>> =
        _colaboradoresPublicos.asStateFlow()

    private val _resumenDonaciones =
        MutableStateFlow(ResumenDonaciones())

    val resumenDonaciones:
            StateFlow<ResumenDonaciones> =
        _resumenDonaciones.asStateFlow()

    private val _cargandoMis =
        MutableStateFlow(false)

    val cargandoMis: StateFlow<Boolean> =
        _cargandoMis.asStateFlow()

    private val _errorMis =
        MutableStateFlow("")

    val errorMis: StateFlow<String> =
        _errorMis.asStateFlow()

    private val _cargandoPublicos =
        MutableStateFlow(false)

    val cargandoPublicos: StateFlow<Boolean> =
        _cargandoPublicos.asStateFlow()

    private val _errorPublicos =
        MutableStateFlow("")

    val errorPublicos: StateFlow<String> =
        _errorPublicos.asStateFlow()

    /**
     * Se activa cuando el backend responde 401. App lo observa para
     * cerrar la sesión y volver al login.
     */
    var sesionExpirada by mutableStateOf(false)
        private set

    private var trabajoMis: Job? = null
    private var trabajoPublicos: Job? = null
    private var generacionMis = 0
    private var generacionPublicos = 0

    // -----------------------------
    // ACTUALIZAR FORMULARIO
    // -----------------------------

    fun actualizarMonto(
        valor: String
    ) {

        if (
            valor.isEmpty() ||
            valor.all {
                it.isDigit()
            }
        ) {

            _formulario2.value =
                _formulario2.value.copy(
                    monto = valor
                )
        }
    }

    fun actualizarNombre(
        valor: String
    ) {

        _formulario2.value =
            _formulario2.value.copy(
                nombre = valor
            )
    }

    fun actualizarFecha(
        valor: Long?
    ) {

        _formulario2.value =
            _formulario2.value.copy(
                fecha = valor
            )
    }

    fun actualizarAceptaCamara(
        valor: Boolean
    ) {

        _formulario2.value =
            _formulario2.value.copy(
                aceptaCamara = valor
            )
    }

    fun actualizarEsAnonimo(
        valor: Boolean?
    ) {

        _formulario2.value =
            _formulario2.value.copy(
                esAnonimo = valor,
                nombre =
                    if (valor == true) {
                        ""
                    } else {
                        _formulario2.value.nombre
                    },
                fotoUri =
                    if (valor == true) {
                        null
                    } else {
                        _formulario2.value.fotoUri
                    },
                aceptaCamara =
                    if (valor == true) {
                        false
                    } else {
                        _formulario2.value.aceptaCamara
                    }
            )
    }

    fun actualizarFotoUri(
        valor: String?
    ) {

        _formulario2.value =
            _formulario2.value.copy(
                fotoUri = valor
            )
    }

    // -----------------------------
    // VALIDACIONES
    // -----------------------------

    fun validarFormulario(
        esAnonimo: Boolean = false
    ): Boolean {

        val datos =
            _formulario2.value

        var montoError: String? = null
        var nombreError: String? = null
        var fechaError: String? = null

        // MONTO

        val montoInt =
            datos.monto.toIntOrNull()

        if (montoInt == null) {

            montoError =
                "Debe ingresar un número"

        } else if (montoInt < 1000) {

            montoError =
                "El valor debe ser superior a 1000"
        }

        // NOMBRE
        // Solo se exige cuando NO es anónimo

        if (!esAnonimo) {

            if (datos.nombre.isBlank()) {

                nombreError =
                    "El nombre es obligatorio"

            } else if (
                datos.nombre.length < 3
            ) {

                nombreError =
                    "El nombre debe tener al menos 3 caracteres"
            }
        }

        // FECHA

        if (datos.fecha == null) {

            fechaError =
                "Debe seleccionar una fecha"
        }

        val nuevosErrores =
            FormularioErrores2(
                monto = montoError,
                nombre = nombreError,
                fecha = fechaError
            )

        _errores2.value =
            nuevosErrores

        return nuevosErrores ==
                FormularioErrores2()
    }

    // -----------------------------
    // DONACIÓN CON WEBPAY
    // -----------------------------

    fun prepararDonacion(): DonacionPendiente? {

        val esAnonimo =
            _formulario2.value.esAnonimo
                ?: return null

        if (!validarFormulario(esAnonimo)) {
            return null
        }

        val datos =
            _formulario2.value

        val monto =
            datos.monto.toIntOrNull()
                ?: return null

        val pendiente = DonacionPendiente(
            nombre =
                if (esAnonimo) {
                    "Anonimo"
                } else {
                    datos.nombre.trim()
                },
            fotoUri =
                if (esAnonimo) {
                    null
                } else {
                    datos.fotoUri
                },
            esAnonimo = esAnonimo,
            monto = monto,
            fecha =
                datos.fecha
                    ?: System.currentTimeMillis(),
            webpayOrden = generarOrdenDonacion()
        )

        _donacionPendiente.value =
            pendiente

        return pendiente
    }

    /**
     * Convierte la donación preparada en el cuerpo exacto que espera
     * POST /api/colaboradores. La misma orden se usa en Webpay y en el POST.
     */
    fun crearRequestColaboracion(
        pendiente: DonacionPendiente
    ): ColaboradorRequest {

        return ColaboradorRequest(
            nombre = pendiente.nombre,
            fotoUri = pendiente.fotoUri,
            montoDonacion = pendiente.monto,
            esAnonimo = pendiente.esAnonimo,
            webpayOrden = pendiente.webpayOrden
        )
    }

    fun finalizarDonacionExitosa(
        token: String
    ) {

        limpiarFormulario()
        _donacionPendiente.value = null
        sesionExpirada = false

        if (token.isNotBlank()) {
            cargarMisColaboraciones(token)
        }

        cargarColaboradoresPublicos()
    }

    fun cancelarDonacionPendiente() {
        _donacionPendiente.value = null
    }

    private fun generarOrdenDonacion(): String {
        return "DON-${System.currentTimeMillis()}"
    }

    private fun limpiarFormulario() {

        _formulario2.value =
            FormularioModelDonaciones()

        _errores2.value =
            FormularioErrores2()
    }

    // -----------------------------
    // MIS COLABORACIONES
    // -----------------------------

    fun cargarMisColaboraciones(
        token: String
    ) {

        if (token.isBlank()) {
            limpiarSesion()
            return
        }

        trabajoMis?.cancel()

        val generacion = ++generacionMis

        _cargandoMis.value = true
        _errorMis.value = ""

        trabajoMis = viewModelScope.launch {

            try {

                val response =
                    RetrofitClient.apiService.obtenerMisColaboraciones(
                        authorization = "Bearer $token"
                    )

                if (generacion != generacionMis) {
                    return@launch
                }

                if (response.isSuccessful) {

                    val lista =
                        response.body() ?: emptyList()

                    _misColaboraciones.value =
                        lista

                    _resumenDonaciones.value =
                        ResumenDonaciones(
                            total = lista.sumOf {
                                it.montoDonacion
                            },
                            cantidad = lista.size
                        )

                    sesionExpirada = false

                } else if (response.code() == 401) {

                    _misColaboraciones.value = emptyList()
                    _resumenDonaciones.value = ResumenDonaciones()
                    _errorMis.value =
                        "Tu sesión expiró. Inicia sesión nuevamente."
                    sesionExpirada = true

                } else {

                    _errorMis.value =
                        response.mensajeDeError(
                            "No se pudieron cargar tus colaboraciones."
                        )
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (generacion == generacionMis) {
                    _errorMis.value =
                        "No se pudieron cargar tus colaboraciones. Revisa la conexión."
                }

            } finally {

                if (generacion == generacionMis) {
                    _cargandoMis.value = false
                }
            }
        }
    }

    // -----------------------------
    // COLABORADORES PÚBLICOS
    // -----------------------------

    fun cargarColaboradoresPublicos() {

        trabajoPublicos?.cancel()

        val generacion = ++generacionPublicos

        _cargandoPublicos.value = true
        _errorPublicos.value = ""

        trabajoPublicos = viewModelScope.launch {

            try {

                val response =
                    RetrofitClient.apiService.obtenerColaboradoresPublicos()

                if (generacion != generacionPublicos) {
                    return@launch
                }

                if (response.isSuccessful) {

                    _colaboradoresPublicos.value =
                        response.body() ?: emptyList()

                } else {

                    _errorPublicos.value =
                        response.mensajeDeError(
                            "No se pudo cargar la lista de colaboradores."
                        )
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (generacion == generacionPublicos) {
                    _errorPublicos.value =
                        "No se pudo cargar la lista de colaboradores. Revisa la conexión."
                }

            } finally {

                if (generacion == generacionPublicos) {
                    _cargandoPublicos.value = false
                }
            }
        }
    }

    // -----------------------------
    // SESIÓN
    // -----------------------------

    fun limpiarSesion() {

        trabajoMis?.cancel()
        trabajoMis = null
        generacionMis++

        _misColaboraciones.value = emptyList()
        _resumenDonaciones.value = ResumenDonaciones()
        _cargandoMis.value = false
        _errorMis.value = ""
        _donacionPendiente.value = null
        sesionExpirada = false
    }
}
