package com.example.albalab.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.albalab.model.ColaboradorPublico
import com.example.albalab.viewmodel.FormularioViewModel2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColaboradoresScreen(
    modifier: Modifier = Modifier,
    viewModel: FormularioViewModel2 = viewModel()
) {

    val azulPrimario = MaterialTheme.colorScheme.primary
    val azulMedio = MaterialTheme.colorScheme.secondary
    val celesteFondo = MaterialTheme.colorScheme.background
    val celesteClaro = MaterialTheme.colorScheme.primaryContainer
    val blanco = MaterialTheme.colorScheme.surface

    val colaboradores by viewModel.colaboradoresPublicos.collectAsState()
    val cargando by viewModel.cargandoPublicos.collectAsState()
    val error by viewModel.errorPublicos.collectAsState()

    var searchText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.cargarColaboradoresPublicos()
    }

    val colaboradoresFiltrados = colaboradores.filter { colaborador ->
        val nombre = nombreVisible(colaborador)
        nombre.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(celesteFondo)
            .imePadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = "Colaboradores",
                tint = azulMedio,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "Nuestros Colaboradores",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = azulPrimario,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Gracias a quienes hacen posible Alba Lab.",
                style = MaterialTheme.typography.bodyMedium,
                color = azulMedio,
                textAlign = TextAlign.Center
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Buscar colaborador...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar colaborador",
                        tint = azulMedio
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = azulPrimario,
                    unfocusedBorderColor = azulMedio,
                    focusedLabelColor = azulPrimario,
                    unfocusedContainerColor = celesteClaro.copy(alpha = 0.3f),
                    focusedContainerColor = blanco
                )
            )
        }

        when {
            cargando && colaboradores.isEmpty() -> {
                CircularProgressIndicator()
            }

            error.isNotEmpty() && colaboradores.isEmpty() -> {
                EmptyColaboradoresCard(
                    titulo = "No se pudo cargar la lista",
                    mensaje = error,
                    azulPrimario = azulPrimario,
                    azulMedio = azulMedio,
                    blanco = blanco,
                    onReintentar = {
                        viewModel.cargarColaboradoresPublicos()
                    }
                )
            }

            colaboradores.isEmpty() -> {
                EmptyColaboradoresCard(
                    titulo = "Aún no hay colaboradores",
                    mensaje = "Las donaciones realizadas aparecerán en esta pantalla.",
                    azulPrimario = azulPrimario,
                    azulMedio = azulMedio,
                    blanco = blanco
                )
            }

            colaboradoresFiltrados.isEmpty() -> {
                EmptyColaboradoresCard(
                    titulo = "Sin resultados",
                    mensaje = "No se encontraron colaboradores para '$searchText'.",
                    azulPrimario = azulPrimario,
                    azulMedio = azulMedio,
                    blanco = blanco
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(colaboradoresFiltrados) { colaborador ->
                        ColaboradorCard(
                            colaborador = colaborador,
                            azulPrimario = azulPrimario,
                            azulMedio = azulMedio,
                            celesteClaro = celesteClaro,
                            blanco = blanco
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ColaboradorCard(
    colaborador: ColaboradorPublico,
    azulPrimario: Color,
    azulMedio: Color,
    celesteClaro: Color,
    blanco: Color
) {

    val foto = colaborador.fotoUri
    val nombre = nombreVisible(colaborador)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!colaborador.esAnonimo && foto != null) {
                AsyncImage(
                    model = Uri.parse(foto),
                    contentDescription = "Foto de $nombre",
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .border(3.dp, azulMedio, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(celesteClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = if (colaborador.esAnonimo) {
                            "Donación anónima"
                        } else {
                            "Colaborador"
                        },
                        tint = azulMedio,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = azulPrimario
                )

                Text(
                    text = if (colaborador.esAnonimo) {
                        "Donación anónima"
                    } else {
                        "Colaborador de Alba Lab"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = azulMedio
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Colaboración registrada",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = azulPrimario
                    )
                }

                Text(
                    text = formatearFecha(colaborador.fecha),
                    style = MaterialTheme.typography.bodySmall,
                    color = azulMedio
                )
            }
        }
    }
}

@Composable
private fun EmptyColaboradoresCard(
    titulo: String,
    mensaje: String,
    azulPrimario: Color,
    azulMedio: Color,
    blanco: Color,
    onReintentar: (() -> Unit)? = null
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = azulMedio,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = azulPrimario,
                textAlign = TextAlign.Center
            )

            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = azulMedio,
                textAlign = TextAlign.Center
            )

            if (onReintentar != null) {
                Button(onClick = onReintentar) {
                    Text("REINTENTAR")
                }
            }
        }
    }
}

private fun nombreVisible(colaborador: ColaboradorPublico): String {
    return if (colaborador.esAnonimo) {
        "Anonimo"
    } else {
        colaborador.nombre
    }
}

private fun formatearFecha(fecha: String?): String {
    if (fecha.isNullOrBlank()) {
        return "Colaboración registrada"
    }

    val soloFecha = fecha.substringBefore("T")
    val partes = soloFecha.split("-")

    return if (partes.size == 3) {
        "${partes[2]}/${partes[1]}/${partes[0]}"
    } else {
        soloFecha
    }
}
