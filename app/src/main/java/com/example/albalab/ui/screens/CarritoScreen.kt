package com.example.albalab.ui.screens

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.albalab.components.ProductoImagen
import com.example.albalab.model.ProductoModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CarritoScreen(
    carrito: MutableList<ProductoModel>,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {

    val context = LocalContext.current
    val fragmentActivity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(fragmentActivity)
    val biometricManager = BiometricManager.from(context)

    val biometricPrompt = remember {
        BiometricPrompt(
            fragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    Toast.makeText(
                        context,
                        "Autenticación cancelada: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    Toast.makeText(
                        context,
                        "Identidad confirmada",
                        Toast.LENGTH_SHORT
                    ).show()

                    onNavigate("Webpay")
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(
                        context,
                        "Huella no reconocida",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirmar identidad")
            .setSubtitle("Usa tu huella para proceder con la compra")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    val totalCarrito = carrito.sumOf { it.precio }
    val productosAgrupados = carrito.groupBy { it.nombre }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextButton(
            onClick = {
                onNavigate("Productos")
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver a productos",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Volver a productos",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrito",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "Carrito",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Revisa tu selección y confirma tu compra.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

        if (carrito.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Agrega productos desde la sección Productos para continuar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = {
                            onNavigate("Productos")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "VER PRODUCTOS",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Resumen de compra",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    AnimatedContent(
                        targetState = carrito.size,
                        transitionSpec = {
                            (
                                slideInVertically(
                                    animationSpec = tween(durationMillis = 250),
                                    initialOffsetY = { it }
                                ) + fadeIn(
                                    animationSpec = tween(durationMillis = 250)
                                )
                            ).togetherWith(
                                slideOutVertically(
                                    animationSpec = tween(durationMillis = 250),
                                    targetOffsetY = { -it }
                                ) + fadeOut(
                                    animationSpec = tween(durationMillis = 200)
                                )
                            )
                        },
                        label = "CantidadTotalCarrito"
                    ) { cantidadTotal ->
                        Text(
                            text = "Productos agregados: $cantidadTotal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    AnimatedContent(
                        targetState = totalCarrito,
                        transitionSpec = {
                            fadeIn(
                                animationSpec = tween(durationMillis = 250)
                            ).togetherWith(
                                fadeOut(
                                    animationSpec = tween(durationMillis = 200)
                                )
                            )
                        },
                        label = "TotalCarrito"
                    ) { totalAnimado ->
                        Text(
                            text = "Total: ${formatearPrecio(totalAnimado)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            productosAgrupados.forEach { (_, productos) ->

                val producto = productos.first()
                val cantidad = productos.size
                val subtotal = producto.precio * cantidad

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = tween(durationMillis = 300)
                        ),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProductoImagen(
                            producto = producto,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = producto.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "Precio: ${formatearPrecio(producto.precio)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        carrito.remove(producto)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Disminuir cantidad",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                AnimatedContent(
                                    targetState = cantidad,
                                    transitionSpec = {
                                        (
                                            slideInVertically(
                                                animationSpec = tween(durationMillis = 220),
                                                initialOffsetY = { it }
                                            ) + fadeIn(
                                                animationSpec = tween(durationMillis = 220)
                                            )
                                        ).togetherWith(
                                            slideOutVertically(
                                                animationSpec = tween(durationMillis = 220),
                                                targetOffsetY = { -it }
                                            ) + fadeOut(
                                                animationSpec = tween(durationMillis = 180)
                                            )
                                        )
                                    },
                                    label = "CantidadProducto"
                                ) { cantidadAnimada ->
                                    Text(
                                        text = cantidadAnimada.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        carrito.add(producto)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Aumentar cantidad",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            AnimatedContent(
                                targetState = subtotal,
                                transitionSpec = {
                                    fadeIn(
                                        animationSpec = tween(durationMillis = 220)
                                    ).togetherWith(
                                        fadeOut(
                                            animationSpec = tween(durationMillis = 180)
                                        )
                                    )
                                },
                                label = "SubtotalProducto"
                            ) { subtotalAnimado ->
                                Text(
                                    text = "Subtotal: ${formatearPrecio(subtotalAnimado)}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            TextButton(
                                onClick = {
                                    carrito.removeAll {
                                        it.nombre == producto.nombre
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "Eliminar",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val biometricStatus = biometricManager.canAuthenticate(
                        BiometricManager.Authenticators.BIOMETRIC_WEAK
                    )

                    if (biometricStatus == BiometricManager.BIOMETRIC_SUCCESS) {
                        biometricPrompt.authenticate(promptInfo)
                    } else {
                        Toast.makeText(
                            context,
                            "Biometría no disponible. Continuando con la compra.",
                            Toast.LENGTH_SHORT
                        ).show()

                        onNavigate("Webpay")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp)
                )

                Text(
                    text = "CONTINUAR COMPRA",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

private fun formatearPrecio(precio: Int): String {
    val formato = NumberFormat.getIntegerInstance(Locale("es", "CL"))
    return "$" + formato.format(precio)
}
