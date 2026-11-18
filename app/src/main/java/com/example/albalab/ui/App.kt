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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.components.AppDrawer
import com.example.albalab.components.AppTopBar
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
import com.example.albalab.viewmodel.AppViewModel
import com.example.albalab.viewmodel.ContenidoViewModel
import com.example.albalab.viewmodel.FormularioViewModel2
import com.example.albalab.viewmodel.WebpayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    modoOscuro: Boolean = false,
    onModoOscuroChange: (Boolean) -> Unit = {}
) {

    val appViewModel: AppViewModel = viewModel()
    val webpayViewModel: WebpayViewModel = viewModel()

    val adminViewModel: AdminViewModel = viewModel()
    val formularioViewModel: FormularioViewModel2 = viewModel()
    val contenidoViewModel: ContenidoViewModel = viewModel()

    val resumenDonaciones by
        formularioViewModel.resumenDonaciones.collectAsState()

    val pantallaActual = appViewModel.pantallaActual
    val carrito = appViewModel.carrito
    val webpayOrigen = appViewModel.webpayOrigen
    val montoDonacionPendiente = appViewModel.montoDonacionPendiente
    val ultimaDonacionMonto = appViewModel.ultimaDonacionMonto
    val emailLogueado = appViewModel.emailLogueado
    val tokenLogueado = appViewModel.tokenLogueado

    val isLoggedIn = emailLogueado.isNotEmpty() || adminViewModel.estaLogueado

    AppDrawer(
        pantallaActual = pantallaActual,
        onPantallaSeleccionada = { destino ->
            appViewModel.pantallaActual = destino
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
                        appViewModel.pantallaPostLogin = "Inicio"
                        appViewModel.pantallaActual = "Login"
                    },
                    onPerfilClick = {
                        appViewModel.pantallaActual =
                            if (appViewModel.emailLogueado.isNotEmpty()) {
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
                                appViewModel.pantallaActual = destino
                            }
                        )
                    }

                    "Productos" -> {
                        ProductosScreen(
                            carrito = carrito,
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                appViewModel.pantallaActual = destino
                            }
                        )
                    }

                    "Carrito" -> {
                        CarritoScreen(
                            carrito = carrito,
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                if (destino == "Webpay") {
                                    appViewModel.navegarAWebpay("Carrito")
                                } else {
                                    appViewModel.pantallaActual = destino
                                }
                            }
                        )
                    }

                    "ResumenCompra" -> {
                        ResumenCompraScreen(
                            carrito = carrito,
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                appViewModel.pantallaActual = destino
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
                            pagoId = appViewModel.pagoWebpayId,
                            montoTotal = total,
                            token = tokenLogueado,
                            concepto = if (webpayOrigen == "Donacion") "DON" else "ORDEN",
                            modifier = Modifier.padding(innerPadding),
                            viewModel = webpayViewModel,
                            onPagoTerminado = {
                                if (appViewModel.webpayOrigen == "Donacion") {
                                    formularioViewModel.confirmarDonacion {
                                        appViewModel.ultimaDonacionMonto =
                                            appViewModel.montoDonacionPendiente
                                        appViewModel.montoDonacionPendiente = 0
                                        appViewModel.webpayOrigen = "Carrito"
                                        appViewModel.pantallaActual = "DonacionConfirmada"
                                    }
                                } else {
                                    // El carrito se conserva hasta mostrar la confirmación.
                                    appViewModel.pantallaActual = "ConfirmacionCompra"
                                }
                            }
                        )
                    }

                    "DonacionConfirmada" -> {
                        DonacionConfirmacionScreen(
                            monto = ultimaDonacionMonto,
                            modifier = Modifier.padding(innerPadding),
                            onVolverInicio = {
                                appViewModel.pantallaActual = "Inicio"
                            },
                            onVerColaboradores = {
                                appViewModel.pantallaActual = "Colaboradores"
                            }
                        )
                    }

                    "ConfirmacionCompra" -> {
                        ConfirmacionCompraScreen(
                            carrito = carrito,
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                if (destino == "Inicio") {
                                    appViewModel.carrito.clear()
                                }
                                appViewModel.pantallaActual = destino
                            }
                        )
                    }

                    "Nosotros" -> {
                        NosotrosScreen(
                            modifier = Modifier.padding(innerPadding),
                            contenidoViewModel = contenidoViewModel,
                            onNavigate = { destino ->
                                appViewModel.pantallaActual = destino
                            }
                        )
                    }

                    "Donaciones" -> {
                        DonacionesScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = formularioViewModel,
                            token = tokenLogueado,
                            onIrALogin = {
                                appViewModel.pantallaPostLogin = "Donaciones"
                                appViewModel.pantallaActual = "Login"
                            },
                            onIrAWebpay = { monto ->
                                appViewModel.navegarAWebpay("Donacion", monto)
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
                                appViewModel.pantallaActual = "Registro"
                            },
                            onIrAAdmin = {
                                appViewModel.pantallaPostLogin = "Inicio"
                                appViewModel.pantallaActual = "AdminLogin"
                            },
                            onLoginExitoso = { email, password, token ->
                                adminViewModel.cerrarSesion()
                                appViewModel.iniciarSesion(email, password, token)
                                appViewModel.pantallaActual =
                                    appViewModel.pantallaPostLogin
                                appViewModel.pantallaPostLogin = "Inicio"
                            }
                        )
                    }

                    "Registro" -> {
                        RegistroScreen(
                            modifier = Modifier.padding(innerPadding),
                            onRegistroExitoso = {
                                appViewModel.pantallaActual = "Login"
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
                                        appViewModel.pantallaActual = "AdminPanel"
                                    }
                                )
                            } else {
                                AdminPanelScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    viewModel = adminViewModel,
                                    onEditarContenido = {
                                        appViewModel.pantallaActual = "AdminContenido"
                                    },
                                    onCerrarSesion = {
                                        appViewModel.pantallaActual = "Inicio"
                                    }
                                )
                            }
                        } else {
                            AdminLoginScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = adminViewModel,
                                onLoginExitoso = {
                                    appViewModel.cerrarSesion()
                                    appViewModel.pantallaActual = "AdminPanel"
                                },
                                onVolver = {
                                    appViewModel.pantallaActual = "Inicio"
                                }
                            )
                        }
                    }

                    "Perfil" -> {
                        PerfilScreen(
                            modifier = Modifier.padding(innerPadding),
                            email = emailLogueado,
                            passwordRaw = appViewModel.passwordLogueada,
                            modoOscuro = modoOscuro,
                            onModoOscuroChange = onModoOscuroChange,
                            onCerrarSesion = {
                                appViewModel.cerrarSesion()
                                appViewModel.pantallaActual = "Inicio"
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
                                appViewModel.pantallaActual = destino
                            }
                        )
                    }
                }
            }
        }
    }
}
