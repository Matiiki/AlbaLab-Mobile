package com.example.albalab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    val totalCarrito = carrito.sumOf { it.precio }

    Column(
        modifier = modifier
            .fillMaxSize()
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
                Text("Dirección")
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