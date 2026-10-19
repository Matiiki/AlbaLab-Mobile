package com.example.albalab.ui.screens

import android.widget.Toast // Importante
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext // Importante
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.viewmodel.AuthViewModel

@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    onRegistroExitoso: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Obtenemos el contexto para poder mostrar el mensaje en pantalla
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Crear Cuenta", modifier = Modifier.padding(bottom = 16.dp))

        // El error del backend se mostrará aquí si el correo ya existe
        if (viewModel.mensajeError.isNotEmpty()) {
            Text(text = viewModel.mensajeError, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.registrar(email, password, onSuccess = {
                    // 1. Muestra el mensaje flotante de éxito
                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // 2. Ejecuta la navegación hacia la pantalla de Login
                    onRegistroExitoso()
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}