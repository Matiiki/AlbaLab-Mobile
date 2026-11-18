package com.example.albalab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.data.local.AlbaLabDatabase
import com.example.albalab.data.repository.DonacionRepository
import com.example.albalab.model.Colaborador
import com.example.albalab.model.DonacionPendiente
import com.example.albalab.model.FormularioModelDonaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

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

class FormularioViewModel2(
    application: Application
) : AndroidViewModel(application) {

    // -----------------------------
    // ROOM
    // -----------------------------

    private val database =
        AlbaLabDatabase.obtenerDatabase(application)

    private val repository =
        DonacionRepository(
            database.albaLabDao()
        )

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
    // COLABORADORES DESDE ROOM
    // -----------------------------

    val listaColaboradores:
            StateFlow<List<Colaborador>> =
        repository
            .colaboradoresConDonaciones
            .map { lista ->

                lista.map { registro ->

                    Colaborador(
                        nombre =
                            if (
                                registro.colaborador.esAnonimo
                            ) {
                                "Anónimo"
                            } else {
                                registro.colaborador.nombre
                            },

                        fotoUri =
                            registro.colaborador.fotoUri,

                        montoOculto =
                            formatearMonto(registro.monto),

                        esAnonimo =
                            registro.colaborador.esAnonimo
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted.WhileSubscribed(
                        5000
                    ),
                initialValue = emptyList()
            )

    val resumenDonaciones:
            StateFlow<ResumenDonaciones> =
        repository
            .donaciones
            .map { lista ->
                ResumenDonaciones(
                    total = lista.sumOf { it.monto },
                    cantidad = lista.size
                )
            }
            .stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted.WhileSubscribed(
                        5000
                    ),
                initialValue = ResumenDonaciones()
            )

    // -----------------------------
    // ACTUALIZAR FORMULARIO
    // -----------------------------

    fun actualizarMonto(
        valor: String
    ) {

        if (
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
                    "Anónimo"
                } else {
                    datos.nombre
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
                    ?: System.currentTimeMillis()
        )

        _donacionPendiente.value =
            pendiente

        return pendiente
    }

    fun confirmarDonacion(
        onGuardado: () -> Unit
    ) {

        val pendiente =
            _donacionPendiente.value
                ?: return

        viewModelScope.launch {

            repository.guardarDonacion(
                nombre = pendiente.nombre,
                fotoUri = pendiente.fotoUri,
                esAnonimo = pendiente.esAnonimo,
                monto = pendiente.monto,
                fecha = pendiente.fecha
            )

            limpiarFormulario()
            _donacionPendiente.value = null

            onGuardado()
        }
    }

    fun cancelarDonacionPendiente() {
        _donacionPendiente.value = null
    }

    private fun formatearMonto(
        monto: Int
    ): String {

        val formato =
            NumberFormat.getIntegerInstance(
                Locale("es", "CL")
            )

        return "$" + formato.format(monto)
    }

    // -----------------------------
    // LIMPIAR FORMULARIO
    // -----------------------------

    private fun limpiarFormulario() {

        _formulario2.value =
            FormularioModelDonaciones()

        _errores2.value =
            FormularioErrores2()
    }
}