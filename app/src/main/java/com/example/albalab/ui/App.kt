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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.example.albalab.model.EmailNotificacionRequest
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.components.AppDrawer
import com.example.albalab.components.AppTopBar
import com.example.albalab.ui.screens.AdminContenidoScreen
import com.example.albalab.ui.screens.AdminLoginScreen
import com.example.albalab.ui.screens.AdminPanelScreen
import com.example.albalab.ui.screens.AdminProductosScreen
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
import com.example.albalab.viewmodel.AdminProductoViewModel
import com.example.albalab.viewmodel.AdminViewModel
import com.example.albalab.viewmodel.AppViewModel
import com.example.albalab.viewmodel.CompraViewModel
import com.example.albalab.viewmodel.ContenidoViewModel
import com.example.albalab.viewmodel.FormularioViewModel2
import com.example.albalab.viewmodel.ProductoViewModel
import com.example.albalab.viewmodel.WebpayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    modoOscuro: Boolean = false,
    onModoOscuroChange: (Boolean) -> Unit = {}
) {

    val appViewModel: AppViewModel = viewModel()
    val compraViewModel: CompraViewModel = viewModel()
    val webpayViewModel: WebpayViewModel = viewModel()

    val scope = rememberCoroutineScope()

    val adminViewModel: AdminViewModel = viewModel()
    val adminProductoViewModel: AdminProductoViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val formularioViewModel: FormularioViewModel2 = viewModel()
    val contenidoViewModel: ContenidoViewModel = viewModel()

    val resumenDonaciones by
        formularioViewModel.resumenDonaciones.collectAsState()

    val misColaboraciones by
        formularioViewModel.misColaboraciones.collectAsState()

    val cargandoMisColaboraciones by
        formularioViewModel.cargandoMis.collectAsState()

    val errorMisColaboraciones by
        formularioViewModel.errorMis.collectAsState()

    val donacionPendiente by
        formularioViewModel.donacionPendiente.collectAsState()

    val pantallaActual = appViewModel.pantallaActual
    val carrito = appViewModel.carrito
    val compras by compraViewModel.compras.collectAsState()
    val webpayOrigen = appViewModel.webpayOrigen
    val montoDonacionPendiente = appViewModel.montoDonacionPendiente
    val ultimaDonacionMonto = appViewModel.ultimaDonacionMonto
    val emailLogueado = appViewModel.emailLogueado
    val tokenLogueado = appViewModel.tokenLogueado

    val isLoggedIn = emailLogueado.isNotEmpty() || adminViewModel.estaLogueado

    // La lista pública se carga siempre, con o sin sesión.
    LaunchedEffect(Unit) {
        formularioViewModel.cargarColaboradoresPublicos()
    }

    // El resumen del inicio y "Mis colaboraciones" salen de /mios.
    LaunchedEffect(tokenLogueado) {
        if (tokenLogueado.isBlank()) {
            formularioViewModel.limpiarSesion()
        } else {
            formularioViewModel.cargarMisColaboraciones(tokenLogueado)
        }
    }

    LaunchedEffect(emailLogueado) {
        if (emailLogueado.isBlank()) {
            compraViewModel.cargarCompras("")
        } else {
            compraViewModel.cargarCompras(emailLogueado, tokenLogueado)
        }
    }

    // Un 401 desde /mios, Webpay o el POST de colaboración cierra la sesión.
    LaunchedEffect(
        formularioViewModel.sesionExpirada,
        webpayViewModel.sesionExpirada
    ) {
        if (
            formularioViewModel.sesionExpirada ||
            webpayViewModel.sesionExpirada
        ) {
            appViewModel.cerrarSesion()
            formularioViewModel.limpiarSesion()
            webpayViewModel.limpiarSesion()
            appViewModel.pantallaPostLogin = "Inicio"
            appViewModel.pantallaActual = "Login"
        }
    }

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
                            usuarioLogueado = emailLogueado.isNotEmpty(),
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
                            viewModel = productoViewModel,
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
                        val esDonacion = webpayOrigen == "Donacion"
                        val pendiente = donacionPendiente

                        if (esDonacion && pendiente == null) {
                            // No hay datos de donación (por ejemplo, tras perder
                            // el proceso). Volver al formulario evita cobrar sin
                            // poder registrar la colaboración.
                            LaunchedEffect(Unit) {
                                appViewModel.pantallaActual = "Donaciones"
                            }
                        } else {
                            val total = if (esDonacion) {
                                pendiente?.monto?.toDouble()
                                    ?: montoDonacionPendiente.toDouble()
                            } else {
                                carrito.sumOf { it.precio }.toDouble()
                            }

                            val colaboracion = if (esDonacion && pendiente != null) {
                                formularioViewModel.crearRequestColaboracion(pendiente)
                            } else {
                                null
                            }

                            WebpayScreen(
                                pagoId = appViewModel.pagoWebpayId,
                                montoTotal = total,
                                token = tokenLogueado,
                                concepto = if (esDonacion) "DON" else "ORDEN",
                                ordenCompra = if (esDonacion) {
                                    pendiente?.webpayOrden
                                } else {
                                    null
                                },
                                colaboracion = colaboracion,
                                modifier = Modifier.padding(innerPadding),
                                viewModel = webpayViewModel,
                                onPagoTerminado = {
                                    if (esDonacion) {
                                        appViewModel.ultimaDonacionMonto =
                                            pendiente?.monto
                                                ?: appViewModel.montoDonacionPendiente

                                        // Enviar correo de confirmación de donación
                                        val montoDonacion =
                                            pendiente?.monto
                                                ?: appViewModel.montoDonacionPendiente
                                        if (emailLogueado.isNotBlank()) {
                                            scope.launch {
                                                try {
                                                    RetrofitClient.apiService.enviarEmailNotificacion(
                                                        "Bearer $tokenLogueado",
                                                        EmailNotificacionRequest(
                                                            to = emailLogueado,
                                                            subject = "Donación recibida - AlbaLab",
                                                            body = "Hola,\n\nTu donación por $$montoDonacion fue procesada exitosamente.\n\nGracias por colaborar con AlbaLab."
                                                        )
                                                    )
                                                } catch (_: Exception) {
                                                }
                                            }
                                        }

                                        // Navegar primero evita que una
                                        // recomposición vuelva a entrar a Webpay.
                                        appViewModel.pantallaActual = "DonacionConfirmada"

                                        appViewModel.montoDonacionPendiente = 0
                                        appViewModel.webpayOrigen = "Carrito"

                                        formularioViewModel.finalizarDonacionExitosa(
                                            tokenLogueado
                                        )
                                    } else {
                                        // El carrito se conserva hasta mostrar la confirmación.
                                        val totalCompra = carrito.sumOf { it.precio }
                                        compraViewModel.guardarCompra(
                                            emailLogueado,
                                            tokenLogueado,
                                            carrito.toList()
                                        )
                                        if (emailLogueado.isNotBlank()) {
                                            scope.launch {
                                                try {
                                                    RetrofitClient.apiService.enviarEmailNotificacion(
                                                        "Bearer $tokenLogueado",
                                                        EmailNotificacionRequest(
                                                            to = emailLogueado,
                                                            subject = "Compra confirmada - AlbaLab",
                                                            body = "Hola,\n\nTu compra por un total de $$totalCompra fue procesada exitosamente.\n\nGracias por tu compra."
                                                        )
                                                    )
                                                } catch (_: Exception) {
                                                }
                                            }
                                        }
                                        appViewModel.pantallaActual = "ConfirmacionCompra"
                                    }
                                }
                            )
                        }
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

                    "AdminLogin", "AdminPanel", "AdminContenido", "AdminProductos" -> {
                        if (adminViewModel.estaLogueado) {
                            when (pantalla) {
                                "AdminContenido" -> {
                                    AdminContenidoScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        viewModel = contenidoViewModel,
                                        onVolver = {
                                            appViewModel.pantallaActual = "AdminPanel"
                                        }
                                    )
                                }

                                "AdminProductos" -> {
                                    AdminProductosScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        viewModel = adminProductoViewModel,
                                        token = adminViewModel.token,
                                        onVolver = {
                                            appViewModel.pantallaActual = "AdminPanel"
                                        }
                                    )
                                }

                                else -> {
                                    AdminPanelScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        viewModel = adminViewModel,
                                        onEditarContenido = {
                                            appViewModel.pantallaActual = "AdminContenido"
                                        },
                                        onGestionarProductos = {
                                            appViewModel.pantallaActual = "AdminProductos"
                                        },
                                        onCerrarSesion = {
                                            adminProductoViewModel.limpiarProductos()
                                            appViewModel.pantallaActual = "Inicio"
                                        }
                                    )
                                }
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
                            misColaboraciones = misColaboraciones,
                            totalColaborado = resumenDonaciones.total,
                            cantidadColaboraciones = resumenDonaciones.cantidad,
                            cargandoColaboraciones = cargandoMisColaboraciones,
                            errorColaboraciones = errorMisColaboraciones,
                            compras = compras,
                            onReintentarColaboraciones = {
                                if (tokenLogueado.isNotBlank()) {
                                    formularioViewModel.cargarMisColaboraciones(
                                        tokenLogueado
                                    )
                                }
                            },
                            onCerrarSesion = {
                                appViewModel.cerrarSesion()
                                formularioViewModel.limpiarSesion()
                                webpayViewModel.limpiarSesion()
                                appViewModel.pantallaActual = "Inicio"
                            }
                        )
                    }

                    else -> {
                        InicioScreen(
                            modifier = Modifier.padding(innerPadding),
                            adminLogueado = adminViewModel.estaLogueado,
                            usuarioLogueado = emailLogueado.isNotEmpty(),
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
