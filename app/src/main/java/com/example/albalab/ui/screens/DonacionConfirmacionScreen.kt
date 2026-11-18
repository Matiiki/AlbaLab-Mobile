package com.example.albalab.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DonacionConfirmacionScreen(
    monto: Int,
    modifier: Modifier = Modifier,
    onVolverInicio: () -> Unit,
    onVerColaboradores: () -> Unit
) {

    val azulPrimario = MaterialTheme.colorScheme.primary
    val azulMedio = MaterialTheme.colorScheme.secondary
    val celesteFondo = MaterialTheme.colorScheme.background
    val verde = MaterialTheme.colorScheme.primary

    val montoFormateado = "$" + NumberFormat
        .getIntegerInstance(Locale("es", "CL"))
        .format(monto)

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(celesteFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(
                animationSpec = tween(durationMillis = 700)
            ) + fadeIn(
                animationSpec = tween(durationMillis = 700)
            )
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Donación exitosa",
                tint = verde,
                modifier = Modifier.size(140.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { 50 },
                animationSpec = tween(
                    durationMillis = 700,
                    delayMillis = 300
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 700,
                    delayMillis = 300
                )
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¡Gracias por tu donación!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = azulPrimario,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Tu aporte fue procesado correctamente con Webpay.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = azulMedio,
                    textAlign = TextAlign.Center
                )

                Card(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Monto donado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = verde
                        )
                        Text(
                            text = montoFormateado,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = verde
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 700,
                    delayMillis = 600
                )
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onVolverInicio,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "VOLVER AL INICIO",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onVerColaboradores,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "VER COLABORADORES",
                        fontWeight = FontWeight.Bold,
                        color = azulPrimario
                    )
                }
            }
        }
    }
}
