package com.example.albalab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.data.local.AlbaLabDatabase
import com.example.albalab.data.local.ContactoEntity
import com.example.albalab.data.repository.ContactoRepository
import com.example.albalab.model.FormularioModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FormularioErrores(

    val nombre: String? = null,

    val motivo: String? = null,

    val cantidadParticipantes: String? = null,

    val aceptaTerminos: String? = null,

    val fecha: String? = null,

    val lugar: String? = null
)

class FormularioViewModel(
    application: Application
) : AndroidViewModel(application) {

    // -----------------------------
    // ROOM
    // -----------------------------

    private val database =
        AlbaLabDatabase.obtenerDatabase(application)

    private val repository =
        ContactoRepository(
            database.albaLabDao()
        )

    val listaContactos:
            StateFlow<List<ContactoEntity>> =
        repository.contactos
            .stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // -----------------------------
    // FORMULARIO
    // -----------------------------

    private val _formulario =
        MutableStateFlow(
            FormularioModel()
        )

    val formulario:
            StateFlow<FormularioModel> =
        _formulario.asStateFlow()

    private val _errores =
        MutableStateFlow(
            FormularioErrores()
        )

    val errores:
            StateFlow<FormularioErrores> =
        _errores.asStateFlow()

    // -----------------------------
    // ACTUALIZAR CAMPOS
    // -----------------------------

    fun actualizarNombre(valor: String) {

        _formulario.value =
            _formulario.value.copy(
                nombre = valor
            )
    }

    fun actualizarMotivo(valor: String) {

        _formulario.value =
            _formulario.value.copy(
                motivo = valor
            )
    }

    fun actualizarCantidadParticipantes(
        valor: String
    ) {

        if (valor.isEmpty()) {

            _formulario.value =
                _formulario.value.copy(
                    cantidadParticipantes = null
                )

            return
        }

        if (
            valor.all {
                it.isDigit()
            }
        ) {

            _formulario.value =
                _formulario.value.copy(
                    cantidadParticipantes =
                        valor.toIntOrNull()
                )
        }
    }

    fun actualizarAceptaTerminos(
        valor: Boolean
    ) {

        _formulario.value =
            _formulario.value.copy(
                aceptaTerminos = valor
            )
    }

    fun actualizarSolicitaSeguimiento(
        valor: Boolean
    ) {

        _formulario.value =
            _formulario.value.copy(
                solicitaSeguimiento = valor
            )
    }

    fun actualizarFecha(
        valor: Long?
    ) {

        _formulario.value =
            _formulario.value.copy(
                fecha = valor
            )
    }

    fun actualizarLugar(
        valor: String
    ) {

        _formulario.value =
            _formulario.value.copy(
                lugar = valor
            )
    }

    // -----------------------------
    // VALIDACIONES
    // -----------------------------

    fun validarFormulario(): Boolean {

        val datos =
            _formulario.value

        var nombreError: String? = null
        var motivoError: String? = null
        var cantidadError: String? = null
        var terminosError: String? = null
        var fechaError: String? = null
        var lugarError: String? = null

        if (datos.nombre.isBlank()) {

            nombreError =
                "El nombre es obligatorio"

        } else if (
            datos.nombre.length < 3
        ) {

            nombreError =
                "El nombre debe tener al menos 3 caracteres"
        }

        if (datos.motivo.isBlank()) {

            motivoError =
                "El motivo es obligatorio"

        } else if (
            datos.motivo.length < 5
        ) {

            motivoError =
                "El motivo debe tener al menos 5 caracteres"
        }

        if (
            datos.cantidadParticipantes == null
        ) {

            cantidadError =
                "Debe ingresar la cantidad de participantes"

        } else if (
            datos.cantidadParticipantes <= 0
        ) {

            cantidadError =
                "La cantidad debe ser mayor a 0"
        }

        if (
            !datos.aceptaTerminos
        ) {

            terminosError =
                "Debe aceptar los términos y condiciones"
        }

        if (
            datos.fecha == null
        ) {

            fechaError =
                "Debe seleccionar una fecha"
        }

        if (
            datos.lugar.isBlank()
        ) {

            lugarError =
                "Debe seleccionar un lugar"
        }

        val nuevosErrores =
            FormularioErrores(
                nombre = nombreError,
                motivo = motivoError,
                cantidadParticipantes =
                    cantidadError,
                aceptaTerminos =
                    terminosError,
                fecha = fechaError,
                lugar = lugarError
            )

        _errores.value =
            nuevosErrores

        return nuevosErrores ==
                FormularioErrores()
    }

    // -----------------------------
    // GUARDAR EN ROOM
    // -----------------------------

    fun guardar(
        onGuardado: () -> Unit = {}
    ) {

        if (!validarFormulario()) {
            return
        }

        val datos =
            _formulario.value

        val cantidad =
            datos.cantidadParticipantes
                ?: return

        val fecha =
            datos.fecha
                ?: return

        viewModelScope.launch {

            repository.guardarContacto(
                nombre =
                    datos.nombre,

                motivo =
                    datos.motivo,

                cantidadParticipantes =
                    cantidad,

                aceptaTerminos =
                    datos.aceptaTerminos,

                solicitaSeguimiento =
                    datos.solicitaSeguimiento,

                fecha =
                    fecha,

                lugar =
                    datos.lugar
            )

            limpiarFormulario()

            onGuardado()
        }
    }

    private fun limpiarFormulario() {

        _formulario.value =
            FormularioModel()

        _errores.value =
            FormularioErrores()
    }
}