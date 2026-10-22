package com.example.albalab.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.model.Usuario
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // Error general del backend
    var mensajeError by mutableStateOf("")
        private set

    // Errores de validación
    var errorEmail by mutableStateOf<String?>(null)
        private set

    var errorPassword by mutableStateOf<String?>(null)
        private set

    var errorConfirmarPassword by mutableStateOf<String?>(null)
        private set

    // Para evitar presionar varias veces el botón
    var cargando by mutableStateOf(false)
        private set


    // ------------------------------------------------
    // LIMPIAR ERRORES
    // ------------------------------------------------

    fun limpiarErrores() {

        mensajeError = ""

        errorEmail = null

        errorPassword = null

        errorConfirmarPassword = null
    }


    // ------------------------------------------------
    // VALIDAR LOGIN
    // ------------------------------------------------

    private fun validarLogin(
        email: String,
        password: String
    ): Boolean {

        errorEmail = null
        errorPassword = null
        mensajeError = ""

        var valido = true

        val correoLimpio =
            email.trim()

        if (correoLimpio.isEmpty()) {

            errorEmail =
                "El correo es obligatorio"

            valido = false

        } else if (
            !Patterns.EMAIL_ADDRESS
                .matcher(correoLimpio)
                .matches()
        ) {

            errorEmail =
                "Ingrese un correo válido"

            valido = false
        }

        if (password.isEmpty()) {

            errorPassword =
                "La contraseña es obligatoria"

            valido = false

        } else if (
            password.length < 6
        ) {

            errorPassword =
                "La contraseña debe tener al menos 6 caracteres"

            valido = false
        }

        return valido
    }


    // ------------------------------------------------
    // VALIDAR REGISTRO
    // ------------------------------------------------

    private fun validarRegistro(
        email: String,
        password: String,
        confirmarPassword: String
    ): Boolean {

        errorEmail = null
        errorPassword = null
        errorConfirmarPassword = null
        mensajeError = ""

        var valido = true

        val correoLimpio =
            email.trim()

        if (correoLimpio.isEmpty()) {

            errorEmail =
                "El correo es obligatorio"

            valido = false

        } else if (
            !Patterns.EMAIL_ADDRESS
                .matcher(correoLimpio)
                .matches()
        ) {

            errorEmail =
                "Ingrese un correo válido"

            valido = false
        }

        if (password.isEmpty()) {

            errorPassword =
                "La contraseña es obligatoria"

            valido = false

        } else if (
            password.length < 6
        ) {

            errorPassword =
                "La contraseña debe tener al menos 6 caracteres"

            valido = false
        }

        if (
            confirmarPassword.isEmpty()
        ) {

            errorConfirmarPassword =
                "Debe confirmar la contraseña"

            valido = false

        } else if (
            password != confirmarPassword
        ) {

            errorConfirmarPassword =
                "Las contraseñas no coinciden"

            valido = false
        }

        return valido
    }


    // ------------------------------------------------
    // LOGIN
    // ------------------------------------------------

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {

        if (
            !validarLogin(
                email,
                password
            )
        ) {

            return
        }

        viewModelScope.launch {

            try {

                cargando = true

                mensajeError = ""

                val usuario =
                    Usuario(
                        email =
                            email.trim(),

                        password =
                            password
                    )

                val response =
                    RetrofitClient
                        .apiService
                        .loginUsuario(
                            usuario
                        )

                if (
                    response.isSuccessful
                ) {

                    onSuccess()

                } else {

                    mensajeError =
                        "Correo o contraseña incorrectos"
                }

            } catch (
                e: Exception
            ) {

                mensajeError =
                    "Error de conexión. Verifica el servidor o la IP."

            } finally {

                cargando = false
            }
        }
    }


    // ------------------------------------------------
    // REGISTRO
    // ------------------------------------------------

    fun registrar(
        email: String,
        password: String,
        confirmarPassword: String,
        onSuccess: () -> Unit
    ) {

        if (
            !validarRegistro(
                email,
                password,
                confirmarPassword
            )
        ) {

            return
        }

        viewModelScope.launch {

            try {

                cargando = true

                mensajeError = ""

                val usuario =
                    Usuario(
                        email =
                            email.trim(),

                        password =
                            password
                    )

                val response =
                    RetrofitClient
                        .apiService
                        .registrarUsuario(
                            usuario
                        )

                if (
                    response.isSuccessful
                ) {

                    onSuccess()

                } else {

                    mensajeError =
                        "El correo ya está registrado o hubo un error."
                }

            } catch (
                e: Exception
            ) {

                mensajeError =
                    "Error de conexión. Verifica el servidor o la IP."

            } finally {

                cargando = false
            }
        }
    }
}