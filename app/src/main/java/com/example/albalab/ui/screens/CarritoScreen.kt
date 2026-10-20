package com.example.albalab.ui.screens

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.albalab.model.ProductoModel

@Composable
fun CarritoScreen(
    carrito: MutableList<ProductoModel>,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {

    val context = LocalContext.current
    val fragmentActivity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(fragmentActivity)

    val biometricPrompt = remember {
        BiometricPrompt(
            fragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(context, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // AQUÍ ESTÁ EL CAMBIO: Al reconocer la huella, avanzamos a Webpay
                    onNavigate("Webpay")
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(context, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirmar Identidad")
            .setSubtitle("Usa tu huella para proceder con la compra")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    val totalCarrito = carrito.sumOf { it.precio }
    val productosAgrupados = carrito.groupBy { it.nombre }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Carrito",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Productos agregados: ${carrito.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Total: $$totalCarrito",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        productosAgrupados.forEach { (_, productos) ->

            val producto = productos.first()
            val cantidad = productos.size
            // Se corrigió el error de sintaxis agregando el multiplicador (*)
            val subtotal = producto.precio * cantidad

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(producto.imagen),
                        contentDescription = producto.nombre,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Precio: $${producto.precio}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            IconButton(
                                onClick = {
                                    carrito.remove(producto)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Disminuir cantidad"
                                )
                            }

                            Text(
                                text = cantidad.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            IconButton(
                                onClick = {
                                    carrito.add(producto)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Aumentar cantidad"
                                )
                            }
                        }

                        Text(
                            text = "Subtotal: $$subtotal",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        TextButton(
                            onClick = {
                                carrito.removeAll {
                                    it.nombre == producto.nombre
                                }
                            }
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }

        if (carrito.isNotEmpty()) {
            Button(
                onClick = {
                    // Llamamos a la biometría en lugar de navegar directamente
                    biometricPrompt.authenticate(promptInfo)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "CONTINUAR COMPRA",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}