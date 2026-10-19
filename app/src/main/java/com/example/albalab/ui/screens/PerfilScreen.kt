package com.example.albalab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    email: String,
    passwordRaw: String,
    onCerrarSesion: () -> Unit // Agregamos la acción para cerrar sesión
) {
    // Reemplazamos cada letra de la contraseña por un asterisco
    val passwordOculta = "*".repeat(passwordRaw.length)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Mi Perfil", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 32.dp))

        Text(text = "Correo Electrónico:", fontWeight = FontWeight.Bold)
        Text(text = email, modifier = Modifier.padding(bottom = 16.dp))

        Text(text = "Contraseña:", fontWeight = FontWeight.Bold)
        Text(text = passwordOculta, modifier = Modifier.padding(bottom = 32.dp))

        // Botón rojo para cerrar sesión
        Button(
            onClick = onCerrarSesion,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Sesión", color = Color.White)
        }
    }
}