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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.components.AppDrawer
import com.example.albalab.components.AppTopBar
import com.example.albalab.model.ProductoModel
import com.example.albalab.ui.screens.AdminContenidoScreen
import com.example.albalab.ui.screens.AdminLoginScreen
import com.example.albalab.ui.screens.AdminPanelScreen
import com.example.albalab.ui.screens.CarritoScreen
import com.example.albalab.ui.screens.ColaboradoresScreen
import com.example.albalab.ui.screens.ConfirmacionCompraScreen
import com.example.albalab.ui.screens.ContactoScreen
import com.example.albalab.ui.screens.DonacionConfirmacionScreen
import com.example.albalab.ui.screens.DonacionesScreen
import com.example.albalab.ui.screens.LoginScreen
import com.example.albalab.ui.screens.PerfilScreen
import com.example.albalab.ui.screens.RegistroScreen
import com.example.albalab.ui.screens.ResumenCompraScreen
import com.example.albalab.ui.screens.WebpayScreen
import com.example.albalab.ui.theme.screens.InicioScreen
import com.example.albalab.ui.theme.screens.NosotrosScreen
import com.example.albalab.ui.theme.screens.ProductosScreen
import com.example.albalab.viewmodel.AdminViewModel
import com.example.albalab.viewmodel.ContenidoViewModel
import com.example.albalab.viewmodel.FormularioViewModel2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    modoOscuro: Boolean = false,
    onModoOscuroChange: (Boolean) -> Unit = {}
) {

    var pantallaActual by remember {
        mutableStateOf("Inicio")
    }

    val carrito = remember {
        mutableStateListOf<ProductoModel>()
    }

    val adminViewModel: AdminViewModel = viewModel()
    val formularioViewModel: FormularioViewModel2 = viewModel()
    val contenidoViewModel: ContenidoViewModel = viewModel()

    val resumenDonaciones by
        formularioViewModel.resumenDonaciones.collectAsState()

    var webpayOrigen by remember {
        mutableStateOf("Carrito")
    }

    var montoDonacionPendiente by remember {
        mutableStateOf(0)
    }

    var ultimaDonacionMonto by remember {
        mutableStateOf(0)
    }

    var pantallaPostLogin by remember {
        mutableStateOf("Inicio")
    }

    var emailLogueado by remember {
        mutableStateOf("")
    }

    var passwordLogueada by remember {
        mutableStateOf("")
    }

    var tokenLogueado by remember {
        mutableStateOf("")
    }

    val isLoggedIn = emailLogueado.isNotEmpty() || adminViewModel.estaLogueado

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
                        pantallaPostLogin = "Inicio"
                        pantallaActual = "Login"
                    },
                    onPerfilClick = {
                        pantallaActual = if (emailLogueado.isNotEmpty()) {
                            "Perfil"
                        } else {
                            "AdminPanel"
                        }
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
                            adminLogueado = adminViewModel.estaLogueado,
                            totalDonaciones = resumenDonaciones.total,
                            cantidadDonaciones = resumenDonaciones.cantidad,
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
                                if (destino == "Webpay") {
                                    webpayOrigen = "Carrito"
                                    montoDonacionPendiente = 0
                                }
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
                        val total = if (webpayOrigen == "Donacion") {
                            montoDonacionPendiente.toDouble()
                        } else {
                            carrito.sumOf { it.precio }.toDouble()
                        }

                        WebpayScreen(
                            montoTotal = total,
                            token = tokenLogueado,
                            concepto = if (webpayOrigen == "Donacion") "DON" else "ORDEN",
                            modifier = Modifier.padding(innerPadding),
                            onPagoTerminado = {
                                if (webpayOrigen == "Donacion") {
                                    formularioViewModel.confirmarDonacion {
                                        ultimaDonacionMonto = montoDonacionPendiente
                                        montoDonacionPendiente = 0
                                        webpayOrigen = "Carrito"
                                        pantallaActual = "DonacionConfirmada"
                                    }
                                } else {
                                    // El carrito se conserva hasta mostrar la confirmación.
                                    pantallaActual = "ConfirmacionCompra"
                                }
                            }
                        )
                    }

                    "DonacionConfirmada" -> {
                        DonacionConfirmacionScreen(
                            monto = ultimaDonacionMonto,
                            modifier = Modifier.padding(innerPadding),
                            onVolverInicio = {
                                pantallaActual = "Inicio"
                            },
                            onVerColaboradores = {
                                pantallaActual = "Colaboradores"
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
                            contenidoViewModel = contenidoViewModel,
                            onNavigate = { destino ->
                                pantallaActual = destino
                            }
                        )
                    }

                    "Donaciones" -> {
                        DonacionesScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = formularioViewModel,
                            token = tokenLogueado,
                            onIrALogin = {
                                pantallaPostLogin = "Donaciones"
                                pantallaActual = "Login"
                            },
                            onIrAWebpay = { monto ->
                                montoDonacionPendiente = monto
                                webpayOrigen = "Donacion"
                                pantallaActual = "Webpay"
                            }
                        )
                    }

                    "Colaboradores" -> {
                        ColaboradoresScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = formularioViewModel
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
                            onIrAAdmin = {
                                pantallaPostLogin = "Inicio"
                                pantallaActual = "AdminLogin"
                            },
                            onLoginExitoso = { email, password, token ->
                                adminViewModel.cerrarSesion()
                                emailLogueado = email
                                passwordLogueada = password
                                tokenLogueado = token
                                pantallaActual = pantallaPostLogin
                                pantallaPostLogin = "Inicio"
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

                    "AdminLogin", "AdminPanel", "AdminContenido" -> {
                        if (adminViewModel.estaLogueado) {
                            if (pantalla == "AdminContenido") {
                                AdminContenidoScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    viewModel = contenidoViewModel,
                                    onVolver = {
                                        pantallaActual = "AdminPanel"
                                    }
                                )
                            } else {
                                AdminPanelScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    viewModel = adminViewModel,
                                    onEditarContenido = {
                                        pantallaActual = "AdminContenido"
                                    },
                                    onCerrarSesion = {
                                        pantallaActual = "Inicio"
                                    }
                                )
                            }
                        } else {
                            AdminLoginScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = adminViewModel,
                                onLoginExitoso = {
                                    emailLogueado = ""
                                    passwordLogueada = ""
                                    tokenLogueado = ""
                                    pantallaActual = "AdminPanel"
                                },
                                onVolver = {
                                    pantallaActual = "Inicio"
                                }
                            )
                        }
                    }

                    "Perfil" -> {
                        PerfilScreen(
                            modifier = Modifier.padding(innerPadding),
                            email = emailLogueado,
                            passwordRaw = passwordLogueada,
                            modoOscuro = modoOscuro,
                            onModoOscuroChange = onModoOscuroChange,
                            onCerrarSesion = {
                                emailLogueado = ""
                                passwordLogueada = ""
                                tokenLogueado = ""
                                pantallaActual = "Inicio"
                            }
                        )
                    }

                    else -> {
                        InicioScreen(
                            modifier = Modifier.padding(innerPadding),
                            adminLogueado = adminViewModel.estaLogueado,
                            totalDonaciones = resumenDonaciones.total,
                            cantidadDonaciones = resumenDonaciones.cantidad,
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
