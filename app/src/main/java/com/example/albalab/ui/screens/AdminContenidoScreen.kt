package com.example.albalab.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.albalab.model.ContenidoDefaults
import com.example.albalab.viewmodel.ContenidoViewModel

@Composable
fun AdminContenidoScreen(
    viewModel: ContenidoViewModel,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit
) {

    val azulPrimario = MaterialTheme.colorScheme.primary
    val azulMedio = MaterialTheme.colorScheme.secondary
    val celesteFondo = MaterialTheme.colorScheme.background
    val blanco = MaterialTheme.colorScheme.surface

    val context = LocalContext.current
    val contenidos by viewModel.contenidos.collectAsState()

    var editados by remember { mutableStateOf(contenidos) }
    var mostrarRestaurar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        editados = contenidos
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(celesteFondo)
            .imePadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver al panel",
                    tint = azulPrimario
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Contenido de Nosotros",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = azulPrimario
                )
                Text(
                    text = "Personaliza los textos que ven los usuarios.",
                    style = MaterialTheme.typography.bodySmall,
                    color = azulMedio
                )
            }

            Icon(
                imageVector = Icons.Default.Article,
                contentDescription = null,
                tint = azulMedio
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = editados,
                key = { _, item -> item.clave }
            ) { index, item ->

                val referencia = ContenidoDefaults.items.firstOrNull {
                    it.clave == item.clave
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = blanco),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Sección: ${referencia?.titulo ?: item.clave}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = azulMedio
                        )

                        OutlinedTextField(
                            value = item.titulo,
                            onValueChange = { nuevo ->
                                editados = editados.toMutableList().also {
                                    it[index] = item.copy(titulo = nuevo)
                                }
                            },
                            label = { Text("Título") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = item.cuerpo,
                            onValueChange = { nuevo ->
                                editados = editados.toMutableList().also {
                                    it[index] = item.copy(cuerpo = nuevo)
                                }
                            },
                            label = { Text("Texto") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Button(
            onClick = {
                viewModel.guardar(editados)
                Toast.makeText(
                    context,
                    "Textos guardados correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "GUARDAR CAMBIOS",
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            onClick = { mostrarRestaurar = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Restaurar textos originales",
                color = azulMedio
            )
        }
    }

    if (mostrarRestaurar) {
        AlertDialog(
            onDismissRequest = { mostrarRestaurar = false },
            title = { Text("Restaurar textos") },
            text = {
                Text(
                    "Se perderán los cambios realizados y volverán los textos originales. ¿Continuar?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.restaurar()
                        editados = ContenidoDefaults.items
                        mostrarRestaurar = false

                        Toast.makeText(
                            context,
                            "Textos restaurados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text(
                        text = "RESTAURAR",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarRestaurar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
