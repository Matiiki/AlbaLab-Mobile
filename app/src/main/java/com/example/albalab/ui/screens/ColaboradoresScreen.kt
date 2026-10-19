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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    viewModel: FormularioViewModel2 = viewModel()
) {

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

            Text(
                text = "Aún no hay colaboradores. ¡Sé el primero!"
            )

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(colaboradores) { colaborador ->

                    // Si no existe foto, la donación se muestra como anónima
                    val esAnonimo = colaborador.fotoUri == null

                    val nombreMostrar = if (esAnonimo) {
                        "Anónimo"
                    } else {
                        colaborador.nombre.ifBlank {
                            "Anónimo"
                        }
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