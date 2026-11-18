package com.example.albalab.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.albalab.model.Administrador
import com.example.albalab.viewmodel.AdminViewModel

@Composable
fun AdminPanelScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel,
    onEditarContenido: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    var emailNuevo by rememberSaveable { mutableStateOf("") }
    var passwordNuevo by rememberSaveable { mutableStateOf("") }
    var adminEditandoId by rememberSaveable { mutableStateOf<Long?>(null) }
    var adminEliminandoId by rememberSaveable { mutableStateOf<Long?>(null) }
    var adminsCargados by rememberSaveable { mutableStateOf(false) }

    val adminEditando = adminEditandoId?.let { id ->
        viewModel.admins.firstOrNull { it.id == id }
    }
    val adminEliminando = adminEliminandoId?.let { id ->
        viewModel.admins.firstOrNull { it.id == id }
    }

    LaunchedEffect(Unit) {
        if (!adminsCargados) {
            viewModel.cargarAdmins()
            adminsCargados = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Gestión de administradores",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = viewModel.emailAdmin,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            TextButton(
                onClick = {
                    viewModel.cerrarSesion()
                    onCerrarSesion()
                }
            ) {
                Text("Salir")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Agregar administrador",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = emailNuevo,
                    onValueChange = { emailNuevo = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = passwordNuevo,
                    onValueChange = { passwordNuevo = it },
                    label = { Text("Contraseña (mínimo 8 caracteres)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    onClick = {
                        viewModel.crearAdmin(
                            email = emailNuevo,
                            password = passwordNuevo,
                            onSuccess = {
                                emailNuevo = ""
                                passwordNuevo = ""
                            }
                        )
                    },
                    enabled = !viewModel.cargando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (viewModel.cargando) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        Text("AGREGAR ADMINISTRADOR")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            onClick = onEditarContenido,
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = "Editar textos",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Editar textos de Nosotros",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Personaliza la información que ven los usuarios.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        if (viewModel.mensajeError.isNotEmpty()) {
            Text(
                text = viewModel.mensajeError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (viewModel.mensajeExito.isNotEmpty()) {
            Text(
                text = viewModel.mensajeExito,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Administradores (${viewModel.admins.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            if (viewModel.cargandoLista) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            }

            IconButton(onClick = { viewModel.cargarAdmins() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Actualizar lista"
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = viewModel.admins,
                key = { admin -> admin.id }
            ) { admin ->
                AdminItem(
                    admin = admin,
                    onEditar = { adminEditandoId = admin.id },
                    onEliminar = { adminEliminandoId = admin.id }
                )
            }

            if (viewModel.admins.isEmpty() && !viewModel.cargandoLista) {
                item {
                    Text(
                        text = "No hay administradores registrados.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }

    adminEditando?.let { admin ->
        EditarAdminDialog(
            admin = admin,
            onDismiss = { adminEditandoId = null },
            onGuardar = { email, password ->
                viewModel.actualizarAdmin(
                    id = admin.id,
                    email = email,
                    password = password,
                    onSuccess = { adminEditandoId = null }
                )
            }
        )
    }

    adminEliminando?.let { admin ->
        AlertDialog(
            onDismissRequest = { adminEliminandoId = null },
            title = { Text("Eliminar administrador") },
            text = {
                Text(
                    if (admin.email == viewModel.emailAdmin) {
                        "Estás a punto de eliminar tu propia cuenta de administrador. ¿Continuar?"
                    } else {
                        "¿Seguro que quieres eliminar a ${admin.email}? Esta acción no se puede deshacer."
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarAdmin(
                            id = admin.id,
                            onSuccess = { adminEliminandoId = null }
                        )
                    }
                ) {
                    Text(
                        text = "ELIMINAR",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { adminEliminandoId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun AdminItem(
    admin: Administrador,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = admin.email,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rol: ${admin.role}",
                    style = MaterialTheme.typography.bodySmall
                )
                admin.fechaCreacion?.let { fecha ->
                    Text(
                        text = "Creado: ${fecha.take(10)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(onClick = onEditar) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar administrador"
                )
            }

            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar administrador",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EditarAdminDialog(
    admin: Administrador,
    onDismiss: () -> Unit,
    onGuardar: (String, String) -> Unit
) {
    var email by rememberSaveable(admin.id) { mutableStateOf(admin.email) }
    var password by rememberSaveable(admin.id) { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar administrador") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Nueva contraseña") },
                    supportingText = {
                        Text("Déjala vacía para conservar la actual")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onGuardar(email, password) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
