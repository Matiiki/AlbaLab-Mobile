package com.example.albalab.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.albalab.viewmodel.FormularioViewModel2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColaboradoresScreen(
    modifier: Modifier = Modifier,
    viewModel: FormularioViewModel2 = viewModel()
) {

    val colaboradores by viewModel.listaColaboradores.collectAsState()

    // 1. Estado para guardar el texto del buscador
    var searchText by remember { mutableStateOf("") }

    // 2. Filtramos la lista en tiempo real basada en la búsqueda
    val colaboradoresFiltrados = colaboradores.filter { colaborador ->
        val nombre = if (colaborador.fotoUri == null || colaborador.nombre.isBlank()) {
            "Anónimo"
        } else {
            colaborador.nombre
        }
        // Ignora mayúsculas/minúsculas al buscar
        nombre.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Nuestros Colaboradores",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 3. El componente del buscador
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Buscar colaborador...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Icono de búsqueda"
                )
            },
            singleLine = true
        )

        // 4. Manejo de estados (vacío, sin resultados y lista con datos)
        if (colaboradores.isEmpty()) {
            Text(
                text = "Aún no hay colaboradores. ¡Sé el primero!"
            )
        } else if (colaboradoresFiltrados.isEmpty()) {
            Text(
                text = "No se encontraron resultados para '$searchText'"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Iteramos sobre la lista filtrada, no sobre la original
                items(colaboradoresFiltrados) { colaborador ->

                    val esAnonimo = colaborador.fotoUri == null
                    val nombreMostrar = if (esAnonimo) {
                        "Anónimo"
                    } else {
                        colaborador.nombre.ifBlank { "Anónimo" }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!esAnonimo) {
                                AsyncImage(
                                    model = Uri.parse(colaborador.fotoUri),
                                    contentDescription = "Foto de $nombreMostrar",
                                    modifier = Modifier.size(60.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Donación anónima",
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(16.dp)
                            )

                            Column {
                                Text(
                                    text = nombreMostrar,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Donación: ${colaborador.montoOculto}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}