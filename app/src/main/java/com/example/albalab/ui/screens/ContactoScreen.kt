package com.example.albalab.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

    val azulPrimario = Color(0xFF0D47A1)
    val azulMedio = Color(0xFF1976D2)
    val celesteFondo = Color(0xFFF0F8FF)
    val celesteClaro = Color(0xFFE3F2FD)
    val blanco = Color(0xFFFFFFFF)

    val context = LocalContext.current

    val formulario by viewModel.formulario.collectAsState()
    val errores by viewModel.errores.collectAsState()

    var mostrarDatePicker by remember {
        mutableStateOf(false)
    }

    var lugarExpandido by remember {
        mutableStateOf(false)
    }

    val lugares = listOf(
        "Cerrillos",
        "Cerro Navia",
        "Conchalí",
        "El Bosque",
        "Estación Central",
        "Huechuraba",
        "Independencia",
        "La Cisterna",
        "La Florida",
        "La Granja",
        "La Pintana",
        "La Reina",
        "Las Condes",
        "Lo Barnechea",
        "Lo Espejo",
        "Lo Prado",
        "Macul",
        "Maipú",
        "Ñuñoa",
        "Pedro Aguirre Cerda",
        "Peñalolén",
        "Providencia",
        "Pudahuel",
        "Puente Alto",
        "Quilicura",
        "Quinta Normal",
        "Recoleta",
        "Renca",
        "San Bernardo",
        "San Joaquín",
        "San Miguel",
        "San Ramón",
        "Santiago",
        "Vitacura"
    )

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = azulPrimario,
        unfocusedBorderColor = azulMedio,
        focusedLabelColor = azulPrimario,
        unfocusedContainerColor = celesteClaro.copy(alpha = 0.4f),
        focusedContainerColor = blanco
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(celesteFondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Contacto",
                tint = azulMedio,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "Contacto AlbaLab",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = azulPrimario
            )

            Text(
                text = "Déjanos tu solicitud y te contactaremos",
                style = MaterialTheme.typography.bodyMedium,
                color = azulMedio
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = blanco
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = formulario.nombre,
                    onValueChange = {
                        viewModel.actualizarNombre(it)
                    },
                    label = {
                        Text("Nombre de contacto")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = azulMedio
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    isError = errores.nombre != null,
                    supportingText = {
                        errores.nombre?.let {
                            Text(it)
                        }
                    }
                )

                OutlinedTextField(
                    value = formulario.motivo,
                    onValueChange = {
                        viewModel.actualizarMotivo(it)
                    },
                    label = {
                        Text("Motivo del contacto")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = null,
                            tint = azulMedio
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    isError = errores.motivo != null,
                    supportingText = {
                        errores.motivo?.let {
                            Text(it)
                        }
                    }
                )

                OutlinedTextField(
                    value = formulario.cantidadParticipantes?.toString() ?: "",
                    onValueChange = {
                        viewModel.actualizarCantidadParticipantes(it)
                    },
                    label = {
                        Text("Cantidad de participantes")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = azulMedio
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    isError = errores.cantidadParticipantes != null,
                    supportingText = {
                        if (errores.cantidadParticipantes != null) {
                            Text(errores.cantidadParticipantes!!)
                        } else {
                            Text(
                                text = "Personas estimadas para la actividad o colaboración",
                                color = azulMedio
                            )
                        }
                    }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = formulario.aceptaTerminos,
                        onCheckedChange = {
                            viewModel.actualizarAceptaTerminos(it)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = azulPrimario
                        )
                    )

                    Text(
                        text = "Acepto los términos y condiciones",
                        color = azulPrimario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                errores.aceptaTerminos?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "¿Deseas que Alba Lab te contacte?",
                            color = azulPrimario,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Activa esta opción para solicitar seguimiento.",
                            style = MaterialTheme.typography.bodySmall,
                            color = azulMedio
                        )
                    }

                    Switch(
                        checked = formulario.solicitaSeguimiento,
                        onCheckedChange = {
                            viewModel.actualizarSolicitaSeguimiento(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = azulPrimario,
                            checkedTrackColor = celesteClaro
                        )
                    )
                }

                val fechaTexto = formulario.fecha?.let {
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(Date(it))
                } ?: "Seleccionar fecha"

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = fechaTexto,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Fecha de actividad")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = azulMedio
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        isError = errores.fecha != null
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                mostrarDatePicker = true
                            }
                    )
                }

                errores.fecha?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = lugarExpandido,
                    onExpandedChange = {
                        lugarExpandido = !lugarExpandido
                    }
                ) {
                    OutlinedTextField(
                        value = formulario.lugar,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Lugar (Comuna)")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = azulMedio
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = lugarExpandido
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        isError = errores.lugar != null
                    )

                    ExposedDropdownMenu(
                        expanded = lugarExpandido,
                        onDismissRequest = {
                            lugarExpandido = false
                        },
                        modifier = Modifier.background(blanco)
                    ) {
                        lugares.forEach { comuna ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = comuna,
                                        color = azulPrimario
                                    )
                                },
                                onClick = {
                                    viewModel.actualizarLugar(comuna)
                                    lugarExpandido = false
                                }
                            )
                        }
                    }
                }

                errores.lugar?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.guardar {
                    Toast.makeText(
                        context,
                        "Solicitud enviada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = azulPrimario,
                contentColor = blanco
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp)
            )

            Text(
                text = "ENVIAR SOLICITUD",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = formulario.fecha
        )

        DatePickerDialog(
            onDismissRequest = {
                mostrarDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.actualizarFecha(
                            datePickerState.selectedDateMillis
                        )
                        mostrarDatePicker = false
                    }
                ) {
                    Text(
                        text = "Aceptar",
                        color = azulPrimario,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDatePicker = false
                    }
                ) {
                    Text(
                        text = "Cancelar",
                        color = azulMedio
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}
