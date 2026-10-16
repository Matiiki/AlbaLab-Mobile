package com.example.albalab.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
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
    val formulario by
    viewModel.formulario.collectAsState()

    val errores by
    viewModel.errores.collectAsState()

    var mostrarDatePicker by
    remember {mutableStateOf(false)
    }

    val lugar = listOf(
        "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central",
        "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja",
        "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo",
        "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda",
        "Peñalolén", "Providencia", "Pudahuel", "Puente Alto", "Quilicura",
        "Quinta Normal", "Recoleta", "Renca", "San Bernardo", "San Joaquín",
        "San Miguel", "San Ramón", "Santiago", "Vitacura"
    )
    var lugarExpandida by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth() .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Contacto albalab",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        OutlinedTextField(
            value = formulario.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errores.nombre != null,
            supportingText = { errores.nombre?.let { Text(it) } }
        )
        OutlinedTextField(
            value = formulario.motivo,
            onValueChange = { viewModel.actualizarMotivo(it) },
            label = { Text("Motivo") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            isError = errores.motivo != null,
            supportingText = { errores.motivo?.let { Text(it) }}
        )

        Row(
            verticalAlignment = Alignment.CenterVertically ) {
            Checkbox( checked = formulario.aceptaTerminos,
                onCheckedChange = { viewModel.actualizarAceptaTerminos(it) } )
            Text( text = "Acepto los términos y condiciones"
            )
        }

        if (errores.aceptaTerminos != null) {
            Text( text = errores.aceptaTerminos!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding( start = 12.dp )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text( text = "Registro activo" )
            Switch( checked = formulario.activo,
                onCheckedChange = { viewModel.actualizarActivo(it) }
            )
        }

        val fechaTexto = formulario.fecha?.let { SimpleDateFormat("dd/MM/yyyy",
            Locale.getDefault()).format( Date(it)) } ?: "Seleccionar fecha"
        /*
                val fechaTexto = formulario.fecha?.let {
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).apply {
                        timeZone = java.util.TimeZone.getTimeZone("UTC")
                    }.format(Date(it))
                } ?: "Seleccionar fecha"
        */
        OutlinedTextField(
            value = fechaTexto,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth(),
            isError = errores.fecha != null
        )
        Button(
            onClick = { mostrarDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar fecha")
        }

        /*
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = fechaTexto,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fecha") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = errores.fecha != null
                    )
                    TextButton(
                        onClick = { mostrarDatePicker = true },
                        modifier = Modifier.matchParentSize()
                    ) {
                    }
                }
        */
        if (errores.fecha != null) {
            Text( text = errores.fecha!!,
                color = MaterialTheme.colorScheme.error )
        }

        ExposedDropdownMenuBox( expanded = lugarExpandida,
            onExpandedChange = { lugarExpandida = !lugarExpandida } )
        {
            OutlinedTextField( value = formulario.lugar,
                onValueChange = {},
                readOnly = true,
                label = { Text("Lugar") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = lugarExpandida) },

                // ¡AQUÍ ESTÁ EL CAMBIO! Agrega .menuAnchor() antes de .fillMaxWidth()
                modifier = Modifier.menuAnchor().fillMaxWidth(),

                isError = errores.lugar != null )

            // ... (resto de tu código del menú)

            ExposedDropdownMenu( expanded = lugarExpandida,
                onDismissRequest = { lugarExpandida = false })
            {
                lugar.forEach { lugar -> DropdownMenuItem(text = { Text(lugar) },
                    onClick = { viewModel.actualizarLugar(lugar )
                        lugarExpandida = false } )
                }
            }
        }

        if (errores.lugar != null) {
            Text( text = errores.lugar!!, color = MaterialTheme.colorScheme.error )
        }

        Spacer( modifier = Modifier.height(10.dp) )

        Button( onClick = { viewModel.guardar() },
            modifier = Modifier.fillMaxWidth() )
        {
            Text("Guardar" )
        }
    }

    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState( initialSelectedDateMillis = formulario.fecha )
        DatePickerDialog( onDismissRequest = { mostrarDatePicker = false },
            confirmButton = { TextButton( onClick = { viewModel.actualizarFecha(datePickerState.selectedDateMillis)
                mostrarDatePicker = false } )
            {
                Text("Aceptar")
            }
            },

            dismissButton = { TextButton( onClick = { mostrarDatePicker = false } ) {
                Text("Cancelar" )
            }
            } ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}
