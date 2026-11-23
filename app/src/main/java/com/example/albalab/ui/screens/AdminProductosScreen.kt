package com.example.albalab.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.albalab.components.ProductoImagen
import com.example.albalab.model.ProductoModel
import com.example.albalab.model.ProductoRequest
import androidx.compose.ui.platform.LocalContext
import android.net.Uri
import android.content.Context
import java.io.File
import com.example.albalab.viewmodel.AdminProductoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdminProductosScreen(
    viewModel: AdminProductoViewModel,
    token: String,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit
) {
    var mostrarFormulario by rememberSaveable { mutableStateOf(false) }
    var productoEditandoId by rememberSaveable { mutableStateOf<Long?>(null) }
    var productoEliminandoId by rememberSaveable { mutableStateOf<Long?>(null) }

    val productoEditando = productoEditandoId?.let { id ->
        viewModel.productos.firstOrNull { it.id == id }
    }
    val productoEliminando = productoEliminandoId?.let { id ->
        viewModel.productos.firstOrNull { it.id == id }
    }

    LaunchedEffect(token) {
        if (token.isNotBlank()) {
            viewModel.cargarProductos(token)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver al panel",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Gestión de productos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Crea, edita o elimina los productos del catálogo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    productoEditandoId = null
                    mostrarFormulario = true
                },
                enabled = !viewModel.guardando,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("NUEVO PRODUCTO", fontWeight = FontWeight.Bold)
            }

            IconButton(
                onClick = { viewModel.cargarProductos(token) },
                enabled = !viewModel.cargandoLista && token.isNotBlank()
            ) {
                if (viewModel.cargandoLista) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar productos"
                    )
                }
            }
        }

        if (viewModel.mensajeError.isNotEmpty()) {
            Text(
                text = viewModel.mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (viewModel.mensajeExito.isNotEmpty()) {
            Text(
                text = viewModel.mensajeExito,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = viewModel.productos,
                key = { producto -> producto.id }
            ) { producto ->
                AdminProductoItem(
                    producto = producto,
                    onEditar = {
                        productoEditandoId = producto.id
                        mostrarFormulario = true
                    },
                    onEliminar = { productoEliminandoId = producto.id }
                )
            }

            if (viewModel.productos.isEmpty() && !viewModel.cargandoLista) {
                item {
                    Text(
                        text = "No hay productos creados todavía.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }

    if (mostrarFormulario) {
        ProductoFormDialog(
            producto = productoEditando,
            guardando = viewModel.guardando,
            viewModel = viewModel,
            onDismiss = {
                if (!viewModel.guardando) {
                    mostrarFormulario = false
                    productoEditandoId = null
                    viewModel.limpiarMensajes()
                }
            },
            onGuardar = { request ->
                val idEditando = productoEditandoId
                if (idEditando == null) {
                    viewModel.crearProducto(token, request) {
                        mostrarFormulario = false
                        productoEditandoId = null
                    }
                } else {
                    viewModel.actualizarProducto(token, idEditando, request) {
                        mostrarFormulario = false
                        productoEditandoId = null
                    }
                }
            }
        )
    }

    productoEliminando?.let { producto ->
        AlertDialog(
            onDismissRequest = {
                if (!viewModel.guardando) {
                    productoEliminandoId = null
                }
            },
            title = { Text("Eliminar producto") },
            text = {
                Text(
                    "¿Seguro que quieres eliminar \"${producto.nombre}\"? " +
                        "Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarProducto(
                            token = token,
                            id = producto.id,
                            onSuccess = { productoEliminandoId = null }
                        )
                    },
                    enabled = !viewModel.guardando
                ) {
                    Text(
                        text = "ELIMINAR",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { productoEliminandoId = null },
                    enabled = !viewModel.guardando
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun AdminProductoItem(
    producto: ProductoModel,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductoImagen(
                producto = producto,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatearPrecio(producto.precio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (!producto.disponible) {
                    Text(
                        text = "No disponible",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                producto.descripcion
                    ?.takeIf { it.isNotBlank() }
                    ?.let { descripcion ->
                        Text(
                            text = descripcion,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
            }

            IconButton(onClick = onEditar) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar producto"
                )
            }

            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar producto",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ProductoFormDialog(
    producto: ProductoModel?,
    guardando: Boolean,
    viewModel: AdminProductoViewModel,
    onDismiss: () -> Unit,
    onGuardar: (ProductoRequest) -> Unit
) {
    val clave = producto?.id ?: -1L

    var nombre by rememberSaveable(clave) {
        mutableStateOf(producto?.nombre.orEmpty())
    }
    var precio by rememberSaveable(clave) {
        mutableStateOf(producto?.precio?.toString().orEmpty())
    }
    var descripcion by rememberSaveable(clave) {
        mutableStateOf(producto?.descripcion.orEmpty())
    }
    var imagenUrl by rememberSaveable(clave) {
        mutableStateOf(producto?.imagenUrl.orEmpty())
    }
    var disponible by rememberSaveable(clave) {
        mutableStateOf(producto?.disponible ?: true)
    }
    var errorLocal by rememberSaveable(clave) { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (producto == null) "Nuevo producto" else "Editar producto"
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it.take(120) },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { nuevo ->
                        if (nuevo.all { it.isDigit() }) {
                            precio = nuevo
                        }
                    },
                    label = { Text("Precio en pesos") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it.take(1000) },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it.take(500) },
                    label = { Text("URL de la imagen (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                )

                val context = LocalContext.current
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    if (uri != null) {
                        val archivo = uriToFile(context, uri)
                        if (archivo != null) {
                            viewModel.subirImagenProducto(archivo) { url ->
                                imagenUrl = url
                            }
                        } else {
                            errorLocal = "No se pudo leer la imagen seleccionada"
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = { launcher.launch("image/*") },
                        enabled = !guardando
                    ) {
                        Text("Subir imagen desde galería")
                    }
                    if (guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Disponible en el catálogo",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Si lo desactivas, no aparecerá en la tienda.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Switch(
                        checked = disponible,
                        onCheckedChange = { disponible = it }
                    )
                }

                if (errorLocal.isNotEmpty()) {
                    Text(
                        text = errorLocal,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val precioValido = precio.toIntOrNull()

                    errorLocal = when {
                        nombre.isBlank() -> "El nombre es obligatorio"
                        precioValido == null || precioValido <= 0 ->
                            "Ingresa un precio mayor a cero"
                        else -> ""
                    }

                    if (errorLocal.isEmpty()) {
                        onGuardar(
                            ProductoRequest(
                                nombre = nombre.trim(),
                                precio = precioValido!!,
                                descripcion = descripcion.trim().ifBlank { null },
                                imagenUrl = imagenUrl.trim().ifBlank { null },
                                disponible = disponible
                            )
                        )
                    }
                },
                enabled = !guardando
            ) {
                if (guardando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                } else {
                    Text("Guardar")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !guardando
            ) {
                Text("Cancelar")
            }
        }
    )
}

private fun formatearPrecio(precio: Int): String {
    val formato = NumberFormat.getIntegerInstance(Locale("es", "CL"))
    return "$" + formato.format(precio)
}

private fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "upload_{System.currentTimeMillis()}.jpg")
        file.outputStream().use { out -> input.copyTo(out) }
        input.close()
        file
    } catch (e: Exception) {
        null
    }
}
