package com.example.albalab.viewmodel

import androidx.lifecycle.ViewModel
import com.example.albalab.model.FormularioModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FormularioErrores(
    val nombre: String? = null,
    val motivo: String? = null,
    val aceptaTerminos: String? = null,
    val fecha: String? = null,
    val lugar: String? = null
)

class FormularioViewModel : ViewModel() {
    private val _formulario = MutableStateFlow(FormularioModel())
    val formulario: StateFlow<FormularioModel> = _formulario.asStateFlow()
    private val _errores = MutableStateFlow(FormularioErrores())
    val errores: StateFlow<FormularioErrores> = _errores.asStateFlow()

    fun actualizarNombre(valor: String) {
        _formulario.value = _formulario.value.copy(nombre = valor)
    }
    fun actualizarMotivo(valor: String) {
        _formulario.value = _formulario.value.copy(motivo = valor)
    }
    fun actualizarAceptaTerminos(valor: Boolean) {
        _formulario.value = _formulario.value.copy(aceptaTerminos = valor)
    }
    fun actualizarActivo(valor: Boolean) {
        _formulario.value = _formulario.value.copy(activo = valor)
    }
    fun actualizarFecha(valor: Long?) {
        _formulario.value = _formulario.value.copy(fecha = valor)
    }
    fun actualizarLugar(valor: String) {
        _formulario.value = _formulario.value.copy(lugar = valor)
    }
    fun validarFormulario(): Boolean {
        val datos = _formulario.value
        var nombreError: String? = null
        var motivoError: String? = null
        var terminosError: String? = null
        var fechaError: String? = null
        var lugarError: String? = null

        if (datos.nombre.isBlank()) {
            nombreError = "El nombre es obligatorio"
        } else if (datos.nombre.length < 3) {
            nombreError = "El nombre debe tener al menos 3 caracteres"
        }
        if (datos.motivo.isBlank()) {
            motivoError = "La descripción es obligatoria"
        } else if (datos.motivo.length < 5) {
            motivoError = "La descripción debe tener al menos 5 caracteres"
        }
        if (!datos.aceptaTerminos) {
            terminosError ="Debe aceptar los términos y condiciones"
        }
        if (datos.fecha == null) {
            fechaError ="Debe seleccionar una fecha"
        }
        if (datos.lugar.isBlank()) {
            lugarError ="Debe seleccionar una categoría"
        }

        val nuevosErrores =
            FormularioErrores(
                nombre = nombreError,
                motivo = motivoError,
                aceptaTerminos = terminosError,
                fecha = fechaError,
                lugar = lugarError
            )
        _errores.value = nuevosErrores
        return nuevosErrores == FormularioErrores()
    }
    fun guardar(): Boolean {
        if (!validarFormulario()) {
            return false
        }
        println("Formulario guardado: ${_formulario.value}")
        return true
    }
}
