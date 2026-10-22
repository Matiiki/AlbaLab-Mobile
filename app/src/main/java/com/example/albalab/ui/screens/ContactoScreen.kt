package com.example.albalab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.viewmodel.FormularioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(
    modifier: Modifier = Modifier,
    viewModel: FormularioViewModel = viewModel()
) {
    // Paleta de colores personalizada
    val AzulPrimario = Color(0xFF0D47A1)
    val AzulMedio = Color(0xFF1976D2)
    val CelesteFondo = Color(0xFFF0F8FF)
    val CelesteClaro = Color(0xFFE3F2FD)
    val Blanco = Color(0xFFFFFFFF)

    // Estados
    val formulario by viewModel.formulario.collectAsState()
    val errores by viewModel.errores.collectAsState()
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var lugarExpandida by remember { mutableStateOf(false) }

    val lugar = listOf(
        "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central",
        "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja",
        "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo",
        "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda",
        "Peñalolén", "Providencia", "Pudahuel", "Puente Alto", "Quilicura",
        "Quinta Normal", "Recoleta", "Renca", "San Bernardo", "San Joaquín",
        "San Miguel", "San Ramón", "Santiago", "Vitacura"
    )

    // Estilo común para los campos de texto
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AzulPrimario,
        unfocusedBorderColor = AzulMedio,
        focusedLabelColor = AzulPrimario,
        unfocusedContainerColor = CelesteClaro.copy(alpha = 0.4f),
        focusedContainerColor = Blanco
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CelesteFondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Cabecera
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Ícono Contacto",
                tint = AzulMedio,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Contacto AlbaLab",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = AzulPrimario
            )
            Text(
                text = "Déjanos tu mensaje y te contactaremos",
                style = MaterialTheme.typography.bodyMedium,
                color = AzulMedio
            )
        }

        // Tarjeta principal del formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // NOMBRE
                OutlinedTextField(
                    value = formulario.nombre,
                    onValueChange = { viewModel.actualizarNombre(it) },
                    label = { Text("Nombre") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AzulMedio) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    isError = errores.nombre != null,
                    supportingText = { errores.nombre?.let { Text(it) } }
                )

                // MOTIVO
                OutlinedTextField(
                    value = formulario.motivo,
                    onValueChange = { viewModel.actualizarMotivo(it) },
                    label = { Text("Motivo del contacto") },
                    leadingIcon = { Icon(Icons.Default.Message, contentDescription = null, tint = AzulMedio) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    isError = errores.motivo != null,
                    supportingText = { errores.motivo?.let { Text(it) } }
                )

                // TÉRMINOS Y CONDICIONES
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = formulario.aceptaTerminos,
                        onCheckedChange = { viewModel.actualizarAceptaTerminos(it) },
                        colors = CheckboxDefaults.colors(checkedColor = AzulPrimario)
                    )
                    Text(
                        text = "Acepto los términos y condiciones",
                        color = AzulPrimario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (errores.aceptaTerminos != null) {
                    Text(
                        text = errores.aceptaTerminos!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 0.dp)
                    )
                }

                // REGISTRO ACTIVO (Switch)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mantener registro activo",
                        color = AzulPrimario,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = formulario.activo,
                        onCheckedChange = { viewModel.actualizarActivo(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = AzulPrimario, checkedTrackColor = CelesteClaro)
                    )
                }

                // FECHA (Clickable Box)
                val fechaTexto = formulario.fecha?.let {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                } ?: "Seleccionar fecha"

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = fechaTexto,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fecha") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = AzulMedio) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        isError = errores.fecha != null
                    )
                    // Caja invisible sobre el TextField para capturar el clic y abrir el calendario
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { mostrarDatePicker = true }
                    )
                }
                if (errores.fecha != null) {
                    Text(
                        text = errores.fecha!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                // LUGAR (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = lugarExpandida,
                    onExpandedChange = { lugarExpandida = !lugarExpandida }
                ) {
                    OutlinedTextField(
                        value = formulario.lugar,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Lugar (Comuna)") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = AzulMedio) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = lugarExpandida) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        isError = errores.lugar != null
                    )

                    ExposedDropdownMenu(
                        expanded = lugarExpandida,
                        onDismissRequest = { lugarExpandida = false },
                        modifier = Modifier.background(Blanco)
                    ) {
                        lugar.forEach { comuna ->
                            DropdownMenuItem(
                                text = { Text(comuna, color = AzulPrimario) },
                                onClick = {
                                    viewModel.actualizarLugar(comuna)
                                    lugarExpandida = false
                                }
                            )
                        }
                    }
                }
                if (errores.lugar != null) {
                    Text(
                        text = errores.lugar!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        // BOTÓN GUARDAR
        Button(
            onClick = { viewModel.guardar() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulPrimario,
                contentColor = Blanco
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp).size(20.dp)
            )
            Text(
                text = "ENVIAR MENSAJE",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    // DATE PICKER DIALOG
    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = formulario.fecha)
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.actualizarFecha(datePickerState.selectedDateMillis)
                        mostrarDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = AzulPrimario, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text("Cancelar", color = AzulMedio)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}