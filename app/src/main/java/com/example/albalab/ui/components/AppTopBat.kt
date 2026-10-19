package com.example.albalab.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onMenuClick: () -> Unit,
    onLoginClick: () -> Unit,
    onPerfilClick: () -> Unit, // Nueva acción para cuando toquen el ícono
    isLoggedIn: Boolean // Variable para saber si el usuario ya entró
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue
        ),
        navigationIcon = {
            TextButton(
                onClick = onMenuClick
            ) {
                Text("MENU", color = Color.White)
            }
        },
        title = {
            Text("")
        },
        actions = {
            // Si está logueado, mostramos el ícono de perfil
            if (isLoggedIn) {
                IconButton(onClick = onPerfilClick) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Perfil de Usuario",
                        tint = Color.White
                    )
                }
            } else {
                // Si NO está logueado, mostramos el botón de siempre
                TextButton(
                    onClick = onLoginClick
                ) {
                    Text("Inicio de sesion", color = Color.White)
                }
            }
        }
    )
}