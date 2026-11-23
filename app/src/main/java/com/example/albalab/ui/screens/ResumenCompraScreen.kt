package com.example.albalab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.albalab.model.ProductoModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.albalab.viewmodel.ResumenCompraViewModel

@Composable
fun ResumenCompraScreen(
    carrito: List<ProductoModel>,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit,
    viewModel: ResumenCompraViewModel = viewModel()
) {

    val formulario by viewModel.formulario.collectAsState()
    val nombreError by viewModel.nombreError.collectAsState()
    val correoError by viewModel.correoError.collectAsState()
    val direccionError by viewModel.direccionError.collectAsState()
    val tasas by viewModel.tasas.collectAsState()

    val totalCarrito = carrito.sumOf { it.precio }

    LaunchedEffect(Unit) {
        viewModel.cargarTasas()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Resumen de compra",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Productos: ${carrito.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Total: $$totalCarrito",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (tasas == null) {
            Text(
                text = "Cargando conversión de moneda...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        tasas?.let { rates ->
            rates["USD"]?.let { usd ->
                Text(
                    text = "${"%.2f".format(totalCarrito * usd)} USD (aprox.)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            rates["EUR"]?.let { eur ->
                Text(
                    text = "${"%.2f".format(totalCarrito * eur)} EUR (aprox.)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        OutlinedTextField(
            value = formulario.nombre,
            onValueChange = {
                viewModel.actualizarNombre(it)
            },
            label = {
                Text("Nombre")
            },
            isError = nombreError != null,
            supportingText = {
                nombreError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formulario.correo,
            onValueChange = {
                viewModel.actualizarCorreo(it)
            },
            label = {
                Text("Correo")
            },
            isError = correoError != null,
            supportingText = {
                correoError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formulario.direccion,
            onValueChange = {
                viewModel.actualizarDireccion(it)
            },
            label = {
                Text("DirecciÃ³n")
            },
            isError = direccionError != null,
            supportingText = {
                direccionError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    onNavigate("ConfirmacionCompra")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "SIGUIENTE",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
