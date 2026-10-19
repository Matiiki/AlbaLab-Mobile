package com.example.albalab.viewmodel

import androidx.lifecycle.ViewModel
import com.example.albalab.model.ResumenCompraModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResumenCompraViewModel : ViewModel() {

    private val _formulario = MutableStateFlow(ResumenCompraModel())
    val formulario = _formulario.asStateFlow()

    private val _nombreError = MutableStateFlow<String?>(null)
    val nombreError = _nombreError.asStateFlow()

    private val _correoError = MutableStateFlow<String?>(null)
    val correoError = _correoError.asStateFlow()

    private val _direccionError = MutableStateFlow<String?>(null)
    val direccionError = _direccionError.asStateFlow()

    fun actualizarNombre(valor: String) {
        _formulario.value = _formulario.value.copy(
            nombre = valor
        )
    }

    fun actualizarCorreo(valor: String) {
        _formulario.value = _formulario.value.copy(
            correo = valor
        )
    }

    fun actualizarDireccion(valor: String) {
        _formulario.value = _formulario.value.copy(
            direccion = valor
        )
    }

    fun validarFormulario(): Boolean {

        val datos = _formulario.value

        _nombreError.value = null
        _correoError.value = null
        _direccionError.value = null

        if (datos.nombre.isBlank()) {
            _nombreError.value = "El nombre es obligatorio"
        } else if (datos.nombre.length < 3) {
            _nombreError.value = "El nombre debe tener al menos 3 caracteres"
        }

        if (datos.correo.isBlank()) {
            _correoError.value = "El correo es obligatorio"
        } else if (!datos.correo.contains("@")) {
            _correoError.value = "Ingresa un correo válido"
        }

        if (datos.direccion.isBlank()) {
            _direccionError.value = "La dirección es obligatoria"
        }

        return _nombreError.value == null &&
                _correoError.value == null &&
                _direccionError.value == null
    }
}