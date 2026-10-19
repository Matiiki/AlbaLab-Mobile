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
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import com.example.albalab.model.ProductoModel
import com.example.albalab.ui.screens.CarritoScreen
import com.example.albalab.ui.screens.ResumenCompraScreen
import com.example.albalab.ui.screens.ConfirmacionCompraScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var pantallaActual by remember {
        mutableStateOf("Inicio")
    }
    val carrito = remember {
        mutableStateListOf<ProductoModel>()
    }
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
                    }
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
                "ConfirmacionCompra" -> {
                    ConfirmacionCompraScreen(
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
                            pantallaActual = "Colaboradores" // Cambia "Inicio" por el nombre de la pantalla a la que quieras ir
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
            }
        }
    }
}