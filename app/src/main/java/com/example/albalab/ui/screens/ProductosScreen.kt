package com.example.albalab.ui.theme.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.albalab.R
import com.example.albalab.model.ProductoModel

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.remember


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

        Card(
            onClick = {
                onNavigate("Carrito")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "🛍️ ${carrito.size} productos"
                )

                Text(
                    text = "$$totalCarrito",
                    fontWeight = FontWeight.Bold
                )
            }
        }
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

                Button(
                    onClick = {
                        carrito.add(productos[0])
                    }
                ) {
                    Text("Agregar al carrito")
                }
            }
        }
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

                Button(
                    onClick = {
                        carrito.add(productos[1])
                    }
                ) {
                    Text("Agregar al carrito")
                }
            }
        }
    }

}