package com.example.albalab.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.albalab.model.ProductoModel

/**
 * Estado que debe sobrevivir a los cambios de configuración (por ejemplo,
 * girar la pantalla). Al vivir en un ViewModel, Compose puede reconstruir
 * toda la interfaz sin perder la navegación, la sesión ni el carrito.
 */
class AppViewModel : ViewModel() {

    var pantallaActual by mutableStateOf("Inicio")

    var pantallaPostLogin by mutableStateOf("Inicio")

    var emailLogueado by mutableStateOf("")

    var passwordLogueada by mutableStateOf("")

    var tokenLogueado by mutableStateOf("")

    var webpayOrigen by mutableStateOf("Carrito")

    var montoDonacionPendiente by mutableStateOf(0)

    var ultimaDonacionMonto by mutableStateOf(0)

    /**
     * Identifica cada intento de pago. Permite que Webpay sepa si debe crear
     * una transacción nueva o reutilizar la que ya estaba en curso.
     */
    var pagoWebpayId by mutableStateOf(0)
        private set

    val carrito = mutableStateListOf<ProductoModel>()

    fun iniciarSesion(email: String, password: String, token: String) {
        emailLogueado = email
        passwordLogueada = password
        tokenLogueado = token
    }

    fun cerrarSesion() {
        emailLogueado = ""
        passwordLogueada = ""
        tokenLogueado = ""
    }

    fun navegarAWebpay(origen: String, montoDonacion: Int = 0) {
        webpayOrigen = origen
        montoDonacionPendiente = montoDonacion
        pagoWebpayId++
        pantallaActual = "Webpay"
    }
}
