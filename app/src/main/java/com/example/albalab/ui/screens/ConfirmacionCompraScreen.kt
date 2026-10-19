package com.example.albalab.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.albalab.model.ProductoModel

@Composable
fun ConfirmacionCompraScreen(
    carrito: List<ProductoModel>,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val totalCarrito = carrito.sumOf { it.precio }

    var mostrarContenido by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        mostrarContenido = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedVisibility(
            visible = mostrarContenido,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 1000)
            ) + slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 1000)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Compra confirmada",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Solicitud recibida",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Gracias por tu solicitud. Nos pondremos en contacto contigo para continuar con el proceso.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Total: $$totalCarrito",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = {
                        onNavigate("Inicio")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("VOLVER AL INICIO")
                }
            }
        }
    }
}