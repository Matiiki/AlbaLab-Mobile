package com.example.albalab.ui.theme.screens

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.model.Contenido
import com.example.albalab.model.ContenidoDefaults
import com.example.albalab.viewmodel.ContenidoViewModel

@Composable
fun NosotrosScreen(
    modifier: Modifier = Modifier,
    contenidoViewModel: ContenidoViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {

    val contenidos by contenidoViewModel.contenidos.collectAsState()
    val contenidosPorClave = contenidos.associateBy { it.clave }

    fun contenido(clave: String): Contenido {
        return contenidosPorClave[clave]
            ?: ContenidoDefaults.items.first { it.clave == clave }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
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
                contentDescription = "Nosotros",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = contenido("nosotros_titulo").titulo,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = contenido("nosotros_titulo").cuerpo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

        NosotrosInfoCard(
            icono = Icons.Default.Favorite,
            titulo = contenido("nosotros_que_es").titulo,
            cuerpo = contenido("nosotros_que_es").cuerpo
        )

        NosotrosInfoCard(
            icono = Icons.Default.Flag,
            titulo = contenido("nosotros_mision").titulo,
            cuerpo = contenido("nosotros_mision").cuerpo
        )

        NosotrosInfoCard(
            icono = Icons.Default.Visibility,
            titulo = contenido("nosotros_vision").titulo,
            cuerpo = contenido("nosotros_vision").cuerpo
        )

        Text(
            text = contenido("nosotros_que_hacemos").titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )

        NosotrosInfoCard(
            icono = Icons.Default.Star,
            titulo = contenido("nosotros_inclusion").titulo,
            cuerpo = contenido("nosotros_inclusion").cuerpo
        )

        NosotrosInfoCard(
            icono = Icons.Default.ShoppingCart,
            titulo = contenido("nosotros_productos").titulo,
            cuerpo = contenido("nosotros_productos").cuerpo,
            textoBoton = "VER PRODUCTOS",
            onClickBoton = {
                onNavigate("Productos")
            }
        )

        NosotrosInfoCard(
            icono = Icons.Default.Public,
            titulo = contenido("nosotros_vinculacion").titulo,
            cuerpo = contenido("nosotros_vinculacion").cuerpo
        )
    }
}

@Composable
private fun NosotrosInfoCard(
    icono: ImageVector,
    titulo: String,
    cuerpo: String,
    modifier: Modifier = Modifier,
    textoBoton: String? = null,
    onClickBoton: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = cuerpo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            if (textoBoton != null && onClickBoton != null) {
                Button(
                    onClick = onClickBoton,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                ) {
                    Text(
                        text = textoBoton,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
