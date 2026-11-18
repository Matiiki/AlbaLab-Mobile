package com.example.albalab.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.model.AdminLoginRequest
import com.example.albalab.model.AdminRequest
import com.example.albalab.model.AdminUpdateRequest
import com.example.albalab.model.Administrador
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    var admins by mutableStateOf<List<Administrador>>(emptyList())
        private set

    var emailAdmin by mutableStateOf("")
        private set

    var token by mutableStateOf("")
        private set

    var cargando by mutableStateOf(false)
        private set

    var cargandoLista by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    var mensajeExito by mutableStateOf("")
        private set

    val estaLogueado: Boolean
        get() = token.isNotBlank()

    fun limpiarMensajes() {
        mensajeError = ""
        mensajeExito = ""
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        val correo = email.trim()

        limpiarMensajes()

        if (correo.isEmpty() || password.isEmpty()) {
            mensajeError = "Ingresa el correo y la contraseña"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            mensajeError = "Ingresa un correo válido"
            return
        }

        viewModelScope.launch {
            try {
                cargando = true

                val response = RetrofitClient.adminApiService.loginAdmin(
                    AdminLoginRequest(correo, password)
                )

                val body = response.body()
                val tokenRecibido = body?.token

                if (response.isSuccessful && !tokenRecibido.isNullOrBlank()) {
                    token = tokenRecibido
                    emailAdmin = body?.email ?: correo
                    onSuccess()
                } else if (response.code() == 401) {
                    mensajeError = "Correo o contraseña incorrectos"
                } else {
                    mensajeError = "No se pudo iniciar sesión (código ${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión con el servicio de administración"
            } finally {
                cargando = false
            }
        }
    }

    fun cargarAdmins() {
        if (token.isBlank()) {
            return
        }

        viewModelScope.launch {
            try {
                cargandoLista = true

                val response = RetrofitClient.adminApiService.listarAdmins(
                    "Bearer $token"
                )

                if (response.isSuccessful) {
                    admins = response.body().orEmpty()
                } else if (response.code() == 401 || response.code() == 403) {
                    mensajeError = "La sesión expiró. Vuelve a iniciar sesión."
                } else {
                    mensajeError = "No se pudo cargar la lista (código ${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al cargar administradores"
            } finally {
                cargandoLista = false
            }
        }
    }

    fun crearAdmin(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        val correo = email.trim()

        limpiarMensajes()

        if (!correoValido(correo)) {
            mensajeError = "Ingresa un correo válido"
            return
        }

        if (password.length < 8) {
            mensajeError = "La contraseña debe tener al menos 8 caracteres"
            return
        }

        viewModelScope.launch {
            try {
                cargando = true

                val response = RetrofitClient.adminApiService.crearAdmin(
                    "Bearer $token",
                    AdminRequest(correo, password)
                )

                if (response.isSuccessful) {
                    mensajeExito = response.body()?.mensaje
                        ?: "Administrador agregado correctamente"
                    cargarAdmins()
                    onSuccess()
                } else if (response.code() == 409) {
                    mensajeError = "Ese correo ya está registrado"
                } else if (response.code() == 401 || response.code() == 403) {
                    mensajeError = "No tienes permisos para agregar administradores"
                } else {
                    mensajeError = "No se pudo agregar el administrador (código ${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al agregar el administrador"
            } finally {
                cargando = false
            }
        }
    }

    fun actualizarAdmin(
        id: Long,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        val correo = email.trim()
        val nuevaPassword = password.trim()

        limpiarMensajes()

        if (!correoValido(correo)) {
            mensajeError = "Ingresa un correo válido"
            return
        }

        if (nuevaPassword.isNotEmpty() && nuevaPassword.length < 8) {
            mensajeError = "La nueva contraseña debe tener al menos 8 caracteres"
            return
        }

        viewModelScope.launch {
            try {
                cargando = true

                val response = RetrofitClient.adminApiService.actualizarAdmin(
                    "Bearer $token",
                    id,
                    AdminUpdateRequest(
                        email = correo,
                        password = nuevaPassword.ifBlank { null }
                    )
                )

                if (response.isSuccessful) {
                    mensajeExito = "Administrador actualizado correctamente"
                    cargarAdmins()
                    onSuccess()
                } else if (response.code() == 409) {
                    mensajeError = "Ese correo ya está registrado"
                } else if (response.code() == 404) {
                    mensajeError = "El administrador ya no existe"
                    cargarAdmins()
                } else {
                    mensajeError = "No se pudo actualizar el administrador (código ${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al actualizar el administrador"
            } finally {
                cargando = false
            }
        }
    }

    fun eliminarAdmin(
        id: Long,
        onSuccess: () -> Unit
    ) {
        limpiarMensajes()

        viewModelScope.launch {
            try {
                cargando = true

                val response = RetrofitClient.adminApiService.eliminarAdmin(
                    "Bearer $token",
                    id
                )

                if (response.isSuccessful) {
                    mensajeExito = "Administrador eliminado correctamente"
                    cargarAdmins()
                    onSuccess()
                } else if (response.code() == 404) {
                    mensajeError = "El administrador ya no existe"
                    cargarAdmins()
                } else if (response.code() == 401 || response.code() == 403) {
                    mensajeError = "No tienes permisos para eliminar administradores"
                } else {
                    mensajeError = "No se pudo eliminar el administrador (código ${response.code()})"
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al eliminar el administrador"
            } finally {
                cargando = false
            }
        }
    }

    fun cerrarSesion() {
        token = ""
        emailAdmin = ""
        admins = emptyList()
        limpiarMensajes()
    }

    private fun correoValido(correo: String): Boolean {
        return correo.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    }
}
