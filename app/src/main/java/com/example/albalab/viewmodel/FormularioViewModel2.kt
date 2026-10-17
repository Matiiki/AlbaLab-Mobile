package com.example.albalab.viewmodel

import androidx.lifecycle.ViewModel
import com.example.albalab.model.FormularioModelDonaciones
import com.example.albalab.model.Colaborador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Los errores siempre son textos (String?) que inician en null (sin error)
data class FormularioErrores2(
    val monto: String? = null,
    val nombre: String? = null,
    val fecha: String? = null,
    val aceptaCamara: Boolean = false
)

class FormularioViewModel2 : ViewModel() {

    private val _formulario2 = MutableStateFlow(FormularioModelDonaciones())
    val formulario2: StateFlow<FormularioModelDonaciones> = _formulario2.asStateFlow()

    private val _errores2 = MutableStateFlow(FormularioErrores2())
    val errores2: StateFlow<FormularioErrores2> = _errores2.asStateFlow()

    // --- VARIABLES Y FUNCIÓN DE COLABORADORES (Ahora están adentro de la clase) ---

    // Lista local de colaboradores
    private val _listaColaboradores = MutableStateFlow<List<Colaborador>>(emptyList())
    val listaColaboradores: StateFlow<List<Colaborador>> = _listaColaboradores.asStateFlow()

    // Función para agregar un nuevo colaborador a la lista
    fun guardarColaboradorLocal(nombre: String, fotoUri: android.net.Uri?) {
        val nuevoColaborador = Colaborador(
            nombre = nombre,
            fotoUri = fotoUri?.toString()
        )
        // Agregamos el nuevo a la lista existente
        _listaColaboradores.value = _listaColaboradores.value + nuevoColaborador
    }

    // ------------------------------------------------------------------------------

    fun actualizarMonto(valor: String) {
        // Solo permite ingresar números
        if (valor.all { it.isDigit() }) {
            _formulario2.value = _formulario2.value.copy(monto = valor)
        }
    }

    fun actualizarAceptaCamara(valor: Boolean) {
        _formulario2.value = _formulario2.value.copy(aceptaCamara = valor)
    }

    fun actualizarNombre(valor: String) {
        _formulario2.value = _formulario2.value.copy(nombre = valor)
    }

    fun actualizarFecha(valor: Long?) {
        _formulario2.value = _formulario2.value.copy(fecha = valor)
    }

    fun validarFormulario(): Boolean {
        val datos = _formulario2.value
        var montoError: String? = null
        var nombreError: String? = null
        var fechaError: String? = null

        // Validación del monto
        val montoInt = datos.monto.toIntOrNull()
        if (montoInt == null) {
            montoError = "Debe ingresar un número"
        } else if (montoInt < 1000 ) {
            montoError = "El valor debe ser superior a 1000"
        }

        // Validación del nombre
        if (datos.nombre.isBlank()) {
            nombreError = "El nombre es obligatorio"
        } else if (datos.nombre.length < 3) {
            nombreError = "El nombre debe tener al menos 3 caracteres"
        }

        // Validación de la fecha
        if (datos.fecha == null) {
            fechaError = "Debe seleccionar una fecha"
        }

        val nuevosErrores = FormularioErrores2(
            monto = montoError,
            nombre = nombreError,
            fecha = fechaError,
        )

        _errores2.value = nuevosErrores
        return nuevosErrores == FormularioErrores2()
    }

    fun guardar(): Boolean {
        if (!validarFormulario()) {
            return false
        }
        println("Donación guardada: ${_formulario2.value}")
        return true
    }
}