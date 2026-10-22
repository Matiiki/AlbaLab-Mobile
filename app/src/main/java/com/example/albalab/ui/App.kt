package com.example.albalab

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.albalab.components.AppDrawer
import com.example.albalab.components.AppTopBar
import com.example.albalab.model.ProductoModel
import com.example.albalab.ui.screens.CarritoScreen
import com.example.albalab.ui.screens.ColaboradoresScreen
import com.example.albalab.ui.screens.ConfirmacionCompraScreen
import com.example.albalab.ui.screens.ContactoScreen
import com.example.albalab.ui.screens.DonacionesScreen
import com.example.albalab.ui.screens.LoginScreen
import com.example.albalab.ui.screens.PerfilScreen
import com.example.albalab.ui.screens.RegistroScreen
import com.example.albalab.ui.screens.ResumenCompraScreen
import com.example.albalab.ui.screens.WebpayScreen
import com.example.albalab.ui.theme.screens.InicioScreen
import com.example.albalab.ui.theme.screens.NosotrosScreen
import com.example.albalab.ui.theme.screens.ProductosScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {

    var pantallaActual by remember {
        mutableStateOf("Inicio")
    }

    val carrito = remember {
        mutableStateListOf<ProductoModel>()
    }

    var emailLogueado by remember {
        mutableStateOf("")
    }

    var passwordLogueada by remember {
        mutableStateOf("")
    }

    val isLoggedIn = emailLogueado.isNotEmpty()

    AppDrawer(
        pantallaActual = pantallaActual,
        onPantallaSeleccionada = { destino ->
            pantallaActual = destino
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
                    onPerfilClick = {
                        pantallaActual = "Perfil"
                    },
                    isLoggedIn = isLoggedIn
                )
            }
        ) { innerPadding ->

            AnimatedContent(
                targetState = pantallaActual,
                transitionSpec = {
                    (
                        slideInHorizontally(
                            animationSpec = tween(durationMillis = 400),
                            initialOffsetX = { ancho -> ancho / 3 }
                        ) + fadeIn(
                            animationSpec = tween(durationMillis = 400)
                        )
                    ).togetherWith(
                        slideOutHorizontally(
                            animationSpec = tween(durationMillis = 400),
                            targetOffsetX = { ancho -> -ancho / 3 }
                        ) + fadeOut(
                            animationSpec = tween(durationMillis = 300)
                        )
                    )
                },
                label = "CambioPantalla"
            ) { pantalla ->

                when (pantalla) {

                    "Inicio" -> {
                        InicioScreen(
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                pantallaActual = destino
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

                    "Carrito" -> {
                        CarritoScreen(
                            carrito = carrito,
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                pantallaActual = destino
                            }
                        )
                    }

                    "ResumenCompra" -> {
                        ResumenCompraScreen(
                            carrito = carrito,
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                pantallaActual = destino
                            }
                        )
                    }

                    "Webpay" -> {
                        val total = carrito.sumOf { it.precio }

                        WebpayScreen(
                            montoTotal = total.toDouble(),
                            modifier = Modifier.padding(innerPadding),
                            onPagoTerminado = {
                                // El carrito se conserva hasta mostrar la confirmación.
                                pantallaActual = "ConfirmacionCompra"
                            }
                        )
                    }

                    "ConfirmacionCompra" -> {
                        ConfirmacionCompraScreen(
                            carrito = carrito,
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                if (destino == "Inicio") {
                                    carrito.clear()
                                }
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
                        ColaboradoresScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    "Contacto" -> {
                        ContactoScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    "Login" -> {
                        LoginScreen(
                            modifier = Modifier.padding(innerPadding),
                            onIrARegistro = {
                                pantallaActual = "Registro"
                            },
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
                            onRegistroExitoso = {
                                pantallaActual = "Login"
                            }
                        )
                    }

                    "Perfil" -> {
                        PerfilScreen(
                            modifier = Modifier.padding(innerPadding),
                            email = emailLogueado,
                            passwordRaw = passwordLogueada,
                            onCerrarSesion = {
                                emailLogueado = ""
                                passwordLogueada = ""
                                pantallaActual = "Inicio"
                            }
                        )
                    }

                    else -> {
                        InicioScreen(
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                pantallaActual = destino
                            }
                        )
                    }
                }
            }
        }
    }
}
