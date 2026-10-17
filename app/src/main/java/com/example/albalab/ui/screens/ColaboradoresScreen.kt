package com.example.albalab.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    // Importante: Debes asegurarte de pasar el MISMO ViewModel que usas en DonacionesScreen
    viewModel: FormularioViewModel2 = viewModel()
) {
    // Obtenemos la lista de colaboradores del ViewModel
    val colaboradores by viewModel.listaColaboradores.collectAsState()

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

        if (colaboradores.isEmpty()) {
            Text(text = "Aún no hay colaboradores. ¡Sé el primero!")
        } else {
            // LazyColumn es ideal para listas largas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(colaboradores) { colaborador ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Mostrar imagen o un ícono por defecto si no quiso foto
                            if (colaborador.fotoUri != null) {
                                AsyncImage(
                                    model = Uri.parse(colaborador.fotoUri),
                                    contentDescription = "Foto de ${colaborador.nombre}",
                                    modifier = Modifier.size(60.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Sin foto",
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Nombre y monto oculto
                            Column {
                                Text(
                                    text = colaborador.nombre.ifBlank { "Anónimo" },
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