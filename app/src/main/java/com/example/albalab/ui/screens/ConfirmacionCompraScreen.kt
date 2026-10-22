package com.example.albalab.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ConfirmacionCompraScreen(
    modifier: Modifier = Modifier,
    onVolverInicio: () -> Unit // Navegación para salir de esta pantalla
) {
    // Estado para controlar cuándo iniciar la animación
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200) // Pequeño retraso para que la animación comience al abrir la pantalla
        visible = true
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animación 1: Icono verde de éxito apareciendo con zoom
        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(animationSpec = tween(700)) + fadeIn(animationSpec = tween(700))
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Éxito",
                tint = Color(0xFF4CAF50), // Verde vibrante
                modifier = Modifier.size(140.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Animación 2: Texto deslizando desde abajo
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { 50 },
                animationSpec = tween(700, delayMillis = 300)
            ) + fadeIn(animationSpec = tween(700, delayMillis = 300))
        ) {
            Text(
                text = "Compra exitosa",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Animación 3: Botón apareciendo suavemente al final
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(700, delayMillis = 600))
        ) {
            Button(
                onClick = onVolverInicio,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Volver al inicio",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}