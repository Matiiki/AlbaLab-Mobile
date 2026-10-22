package com.example.albalab.ui.theme.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.albalab.R
import com.example.albalab.model.ProductoModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductosScreen(
    carrito: MutableList<ProductoModel>,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {

    val productos = listOf(
        ProductoModel(
            nombre = "Difusor Aroma.",
            precio = 12000,
            imagen = R.drawable.difusor_aroma
        ),
        ProductoModel(
            nombre = "Bolsa AlbaLab",
            precio = 4000,
            imagen = R.drawable.bolsa_albalab
        )
    )

    // --- LÓGICA DE ANIMACIÓN DEL CARRITO ---
    // Estado para saber cuándo "iluminar" el carrito
    var carritoResaltado by remember { mutableStateOf(false) }

    // Efecto que se dispara automáticamente cada vez que cambia la cantidad de productos
    LaunchedEffect(carrito.size) {
        if (carrito.isNotEmpty()) {
            carritoResaltado = true
            delay(300) // Mantiene la iluminación por 300 milisegundos
            carritoResaltado = false
        }
    }

    // Animación de color: Cambia a un color destacado y luego vuelve al normal
    val colorFondoCarrito by animateColorAsState(
        targetValue = if (carritoResaltado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300)
    )

    // Animación de escala: Hace que la tarjeta crezca un 5% y vuelva a la normalidad
    val escalaCarrito by animateFloatAsState(
        targetValue = if (carritoResaltado) 1.05f else 1f,
        animationSpec = tween(durationMillis = 300)
    )
    // ----------------------------------------

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Productos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Conoce algunos de los productos disponibles en Alba Lab.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Carrito: ${carrito.size} productos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        val totalCarrito = carrito.sumOf { it.precio }

        // TARJETA DEL CARRITO (Ahora con animaciones aplicadas)
        Card(
            onClick = {
                onNavigate("Carrito")
            },
            modifier = Modifier
                .fillMaxWidth()
                .scale(escalaCarrito), // Aplica el rebote
            colors = CardDefaults.cardColors(
                containerColor = colorFondoCarrito // Aplica la iluminación de color
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "🛍️ ${carrito.size} productos",
                    color = if (carritoResaltado) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "$$totalCarrito",
                    fontWeight = FontWeight.Bold,
                    color = if (carritoResaltado) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Producto 1
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(productos[0].imagen),
                    contentDescription = productos[0].nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = productos[0].nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "$${productos[0].precio}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Aromatizador de ambiente en spray. Envase pet. 250 ml. Formato ideal para el hogar. Aromas disponibles: Vainilla.",
                    style = MaterialTheme.typography.bodyMedium
                )

                BotonAgregarAnimado(
                    onClick = {
                        carrito.add(productos[0])
                    }
                )
            }
        }

        // Producto 2
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(productos[1].imagen),
                    contentDescription = productos[1].nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = productos[1].nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "$${productos[1].precio}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Bolsa ecológica de algodón medidas 40x60cm. Lavable y reutilizable. Personalizada con frase alba lab «todos tenemos una luz que hacer brillar» Tamaño ideal para el supermercado.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                BotonAgregarAnimado(
                    onClick = {
                        carrito.add(productos[1])
                    }
                )
            }
        }
    }
}

// Componente reutilizable para el botón con animación de rebote (¡se mantiene!)
@Composable
fun BotonAgregarAnimado(onClick: () -> Unit) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            onClick()
            coroutineScope.launch {
                scale.animateTo(
                    targetValue = 1.15f,
                    animationSpec = tween(durationMillis = 100)
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        },
        modifier = Modifier.scale(scale.value)
    ) {
        Text("Agregar al carrito")
    }
}