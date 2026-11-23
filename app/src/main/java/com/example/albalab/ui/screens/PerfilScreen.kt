package com.example.albalab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.albalab.data.local.CompraEntity
import com.example.albalab.model.ColaboradorResponse
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    email: String,
    passwordRaw: String,
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onCerrarSesion: () -> Unit,
    misColaboraciones: List<ColaboradorResponse> = emptyList(),
    totalColaborado: Int = 0,
    cantidadColaboraciones: Int = 0,
    cargandoColaboraciones: Boolean = false,
    errorColaboraciones: String = "",
    compras: List<CompraEntity> = emptyList(),
    onReintentarColaboraciones: () -> Unit = {}
) {
    val passwordOculta = "*".repeat(passwordRaw.length)
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Mi perfil",
                tint = colorScheme.secondary,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Administra tu cuenta y las preferencias de la app",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Datos de la cuenta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )

                PerfilInfoRow(
                    icono = Icons.Default.Email,
                    etiqueta = "Correo electrónico",
                    valor = email
                )

                PerfilInfoRow(
                    icono = Icons.Default.Lock,
                    etiqueta = "Contraseña",
                    valor = passwordOculta
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Mis colaboraciones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Total aportado",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.secondary
                        )

                        Text(
                            text = formatearMontoPerfil(totalColaborado),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                    }

                    Text(
                        text = if (cantidadColaboraciones == 1) {
                            "1 colaboración"
                        } else {
                            "$cantidadColaboraciones colaboraciones"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.secondary,
                        textAlign = TextAlign.End
                    )
                }

                HorizontalDivider()

                when {
                    cargandoColaboraciones -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    errorColaboraciones.isNotEmpty() -> {
                        Text(
                            text = errorColaboraciones,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.error
                        )

                        Button(
                            onClick = onReintentarColaboraciones,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("REINTENTAR")
                        }
                    }

                    misColaboraciones.isEmpty() -> {
                        Text(
                            text = "Aún no tienes colaboraciones registradas.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.secondary
                        )
                    }

                    else -> {
                        misColaboraciones.forEachIndexed { index, colaboracion ->
                            if (index > 0) {
                                HorizontalDivider()
                            }

                            ColaboracionPerfilRow(
                                colaboracion = colaboracion
                            )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Historial de compras",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (compras.isEmpty()) {
                    Text(
                        text = "Aun no tienes compras registradas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.secondary
                    )
                } else {
                    compras.forEachIndexed { index, compra ->
                        if (index > 0) {
                            HorizontalDivider()
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = java.text.SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    java.util.Locale.getDefault()
                                ).format(java.util.Date(compra.fecha)),
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.secondary
                            )

                            Text(
                                text = compra.productos,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onSurface
                            )

                            Text(
                                text = formatearMontoPerfil(compra.total),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = "Modo oscuro",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Modo oscuro",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )

                    Text(
                        text = if (modoOscuro) {
                            "Activado en toda la aplicación"
                        } else {
                            "Desactivado"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.secondary
                    )
                }

                Switch(
                    checked = modoOscuro,
                    onCheckedChange = onModoOscuroChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorScheme.onPrimary,
                        checkedTrackColor = colorScheme.primary,
                        uncheckedThumbColor = colorScheme.onSurfaceVariant,
                        uncheckedTrackColor = colorScheme.surfaceVariant,
                        uncheckedBorderColor = colorScheme.outline
                    )
                )
            }
        }

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.error,
                contentColor = colorScheme.onError
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp)
            )

            Text(
                text = "CERRAR SESIÓN",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun PerfilInfoRow(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = colorScheme.secondary,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.secondary
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ColaboracionPerfilRow(
    colaboracion: ColaboradorResponse
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = colorScheme.secondary,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (colaboracion.esAnonimo) {
                    "Anonimo"
                } else {
                    colaboracion.nombre
                },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )

            Text(
                text = formatearFechaPerfil(colaboracion.fecha),
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.secondary
            )

            colaboracion.estado?.takeIf { it.isNotBlank() }?.let { estado ->
                Text(
                    text = "Estado: $estado",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.primary
                )
            }
        }

        Text(
            text = formatearMontoPerfil(colaboracion.montoDonacion),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )
    }
}

private fun formatearMontoPerfil(monto: Int): String {
    val formato = NumberFormat.getIntegerInstance(Locale("es", "CL"))
    return "$" + formato.format(monto)
}

private fun formatearFechaPerfil(fecha: String?): String {
    if (fecha.isNullOrBlank()) {
        return "Sin fecha"
    }

    val soloFecha = fecha.substringBefore("T")
    val partes = soloFecha.split("-")

    return if (partes.size == 3) {
        "${partes[2]}/${partes[1]}/${partes[0]}"
    } else {
        soloFecha
    }
}
