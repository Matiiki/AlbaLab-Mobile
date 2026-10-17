package com.example.albalab.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.albalab.R
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Spacer


@Composable
fun AppDrawer(
    pantallaActual: String,
    onPantallaSeleccionada: (String) -> Unit,
    contenido: @Composable (abrirMenu: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier.size(120.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo_albalab),
                        contentDescription = "Logo",
                        modifier = Modifier.fillMaxSize()
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
                    label = { Text("Galeria") },
                    selected = pantallaActual == "Galeria",
                    onClick = {
                        onPantallaSeleccionada("Galeria")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Presentacion") },
                    selected = pantallaActual == "Presentacion",
                    onClick = {
                        onPantallaSeleccionada("Presentacion")
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
                Spacer(modifier = Modifier.weight(1f)) //lo tira para bajo
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
