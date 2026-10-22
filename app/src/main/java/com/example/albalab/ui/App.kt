package com.example.albalab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.albalab.components.AppDrawer
import com.example.albalab.components.AppTopBar
import com.example.albalab.ui.screens.ColaboradoresScreen
import com.example.albalab.ui.screens.ContactoScreen
import com.example.albalab.ui.screens.DonacionesScreen
import com.example.albalab.ui.theme.screens.ProductosScreen
import com.example.albalab.ui.theme.screens.InicioScreen
import com.example.albalab.ui.theme.screens.NosotrosScreen
import com.example.albalab.ui.screens.LoginScreen
import com.example.albalab.ui.screens.RegistroScreen
import com.example.albalab.ui.screens.PerfilScreen
import com.example.albalab.ui.screens.CarritoScreen
import com.example.albalab.ui.screens.ResumenCompraScreen
import com.example.albalab.ui.screens.ConfirmacionCompraScreen
import com.example.albalab.ui.screens.WebpayScreen
import androidx.compose.runtime.mutableStateListOf
import com.example.albalab.model.ProductoModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var pantallaActual by remember { mutableStateOf("Inicio") }

    val carrito = remember {
        mutableStateListOf<ProductoModel>()
    }

    // Variables para guardar los datos del usuario cuando inicie sesión
    var emailLogueado by remember { mutableStateOf("") }
    var passwordLogueada by remember { mutableStateOf("") }

    // Comprobamos si hay un usuario logueado (si el correo no está vacío)
    val isLoggedIn = emailLogueado.isNotEmpty()

    AppDrawer(
        pantallaActual = pantallaActual,
        onPantallaSeleccionada = {
            pantallaActual = it
        }
    ) { abrirMenu ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppTopBar(
                    onMenuClick = {
                        abrirMenu()
                    },
                    onLoginClick = {
                        pantallaActual = "Login"
                    },
                    // Agregamos la acción para el ícono de perfil y el estado de sesión
                    onPerfilClick = {
                        pantallaActual = "Perfil"
                    },
                    isLoggedIn = isLoggedIn
                )
            }
        ) { innerPadding ->
            when (pantallaActual) {
                "Inicio" -> {
                    InicioScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigate = { destino ->
                            pantallaActual = destino
                        }
                    )
                }

// NUEVA RUTA DE PAGO
                "Webpay" -> {
                    val total = carrito.sumOf { it.precio }
                    WebpayScreen(
                        montoTotal = total.toDouble(),
                        modifier = Modifier.padding(innerPadding),
                        onPagoTerminado = {
                            // 1. Vaciamos el carrito
                            carrito.clear()
                            // 2. Navegamos a la pantalla de animación en lugar de "Inicio"
                            pantallaActual = "ConfirmacionCompra"
                        }
                    )
                }

                "Carrito" -> {
                    CarritoScreen(
                        carrito = carrito,
                        modifier = Modifier.padding(innerPadding),
                        onNavigate = { destino -> pantallaActual = destino }
                    )
                }

                "ResumenCompra" -> {
                    ResumenCompraScreen(
                        carrito = carrito,
                        modifier = Modifier.padding(innerPadding),
                        onNavigate = { destino -> pantallaActual = destino }
                    )
                }

                // NUEVA RUTA DE CONFIRMACIÓN ANIMADA
                "ConfirmacionCompra" -> {
                    ConfirmacionCompraScreen(
                        modifier = Modifier.padding(innerPadding),
                        onVolverInicio = {
                            pantallaActual = "Inicio"
                        }
                    )
                }

                "Productos" -> {
                    ProductosScreen(
                        carrito = carrito,
                        modifier = Modifier.padding(innerPadding),
                        onNavigate = { destino ->
                            pantallaActual = destino
                        }
                    )
                }
                "Nosotros" -> {
                    NosotrosScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigate = { destino ->
                            pantallaActual = destino
                        }
                    )
                }
                "Donaciones" -> {
                    DonacionesScreen(
                        modifier = Modifier.padding(innerPadding),
                        onGuardarExitoso = {
                            pantallaActual = "Colaboradores"
                        }
                    )
                }
                "Colaboradores" -> {
                    ColaboradoresScreen(modifier = Modifier.padding(innerPadding))
                }
                "Contacto" -> {
                    ContactoScreen(modifier = Modifier.padding(innerPadding))
                }
                "Login" -> {
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        onIrARegistro = { pantallaActual = "Registro" },
                        // Recibimos los datos del LoginScreen y los guardamos
                        onLoginExitoso = { email, password ->
                            emailLogueado = email
                            passwordLogueada = password
                            pantallaActual = "Inicio"
                        }
                    )
                }
                "Registro" -> {
                    RegistroScreen(
                        modifier = Modifier.padding(innerPadding),
                        onRegistroExitoso = { pantallaActual = "Login" }
                    )
                }
                // Pantalla donde se muestran los datos del usuario
                "Perfil" -> {
                    PerfilScreen(
                        modifier = Modifier.padding(innerPadding),
                        email = emailLogueado,
                        passwordRaw = passwordLogueada,
                        onCerrarSesion = {
                            // 1. Borramos los datos del usuario de la memoria
                            emailLogueado = ""
                            passwordLogueada = ""
                            // 2. Lo mandamos a la pantalla de Inicio
                            pantallaActual = "Inicio"
                        }
                    )
                }
            }
        }
    }
}