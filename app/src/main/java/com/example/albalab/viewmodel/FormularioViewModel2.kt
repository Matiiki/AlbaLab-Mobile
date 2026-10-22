package com.example.albalab.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.data.local.AlbaLabDatabase
import com.example.albalab.data.repository.DonacionRepository
import com.example.albalab.model.Colaborador
import com.example.albalab.model.FormularioModelDonaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FormularioErrores2(
    val monto: String? = null,
    val nombre: String? = null,
    val fecha: String? = null,
    val aceptaCamara: Boolean = false
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
                            "$${registro.monto}"
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
    // GUARDAR EN ROOM
    // -----------------------------

    fun guardarDonacionRoom(
        esAnonimo: Boolean,
        fotoUri: Uri?,
        onGuardado: () -> Unit
    ) {

        if (
            !validarFormulario(
                esAnonimo
            )
        ) {
            return
        }

        val datos =
            _formulario2.value

        val monto =
            datos.monto.toIntOrNull()
                ?: return

        val fecha =
            datos.fecha
                ?: System.currentTimeMillis()

        viewModelScope.launch {

            repository.guardarDonacion(
                nombre = datos.nombre,
                fotoUri =
                    fotoUri?.toString(),
                esAnonimo = esAnonimo,
                monto = monto,
                fecha = fecha
            )

            limpiarFormulario()

            onGuardado()
        }
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