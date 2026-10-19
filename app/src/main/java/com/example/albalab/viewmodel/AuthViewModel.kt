package com.example.albalab.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.model.Usuario
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // Variable para mostrar errores en pantalla (si la contraseña está mal, si no hay internet, etc.)
    var mensajeError by mutableStateOf("")

    fun registrar(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                mensajeError = "" // Limpiamos errores previos
                val usuario = Usuario(email, password)
                val response = RetrofitClient.apiService.registrarUsuario(usuario)

                if (response.isSuccessful) {
                    onSuccess() // Si todo sale bien, ejecutamos la acción de éxito (cambiar de pantalla)
                } else {
                    mensajeError = "El correo ya está registrado o hubo un error."
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: Verifica tu internet o IP."
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                mensajeError = ""
                val usuario = Usuario(email, password)
                val response = RetrofitClient.apiService.loginUsuario(usuario)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    mensajeError = "Credenciales incorrectas."
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión: Verifica tu internet o IP."
            }
        }
    }
}