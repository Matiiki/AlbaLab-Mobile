package com.example.albalab.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.albalab.R
import kotlinx.coroutines.launch


@Composable
fun AppDrawer(
    pantallaActual: String,
    onPantallaSeleccionada: (String) -> Unit,
    contenido: @Composable (abrirMenu: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val fondoOscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier.size(120.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo_albalab),
                            contentDescription = "Logo",
                            modifier = Modifier.fillMaxSize(),
                            colorFilter = if (fondoOscuro) {
                                ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            } else {
                                null
                            }
                        )
                    }
                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = pantallaActual == "Inicio",
                        onClick = {
                            onPantallaSeleccionada("Inicio")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Productos") },
                        selected = pantallaActual == "Productos",
                        onClick = {
                            onPantallaSeleccionada("Productos")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )



                    NavigationDrawerItem(
                        label = { Text("Donaciones") },
                        selected = pantallaActual == "Donaciones",
                        onClick = {
                            onPantallaSeleccionada("Donaciones")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Colaboradores") },
                        selected = pantallaActual == "Colaboradores",
                        onClick = {
                            onPantallaSeleccionada("Colaboradores")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Nosotros") },
                        selected = pantallaActual == "Nosotros",
                        onClick = {
                            onPantallaSeleccionada("Nosotros")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Administración") },
                        selected = pantallaActual == "AdminLogin" ||
                            pantallaActual == "AdminPanel" ||
                            pantallaActual == "AdminContenido",
                        onClick = {
                            onPantallaSeleccionada("AdminLogin")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    NavigationDrawerItem(
                        label = { Text("Contacto") },
                        selected = pantallaActual == "Contacto",
                        onClick = {
                            onPantallaSeleccionada("Contacto")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    ) {
        contenido {
            scope.launch {
                drawerState.open()
            }
        }
    }
}
