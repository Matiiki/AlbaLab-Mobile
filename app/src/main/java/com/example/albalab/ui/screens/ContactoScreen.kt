package com.example.albalab.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
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

    val context =
        LocalContext.current

    var mostrarDatePicker by
    remember {
        mutableStateOf(false)
    }

    var lugarExpandido by
    remember {
        mutableStateOf(false)
    }

    val lugares =
        listOf(
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        Text(
            text = "Contacto Alba Lab",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Text(
            text =
                "Completa los datos para solicitar información o coordinar una actividad con Alba Lab.",
            style =
                MaterialTheme
                    .typography
                    .bodyMedium
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        // NOMBRE

        OutlinedTextField(
            value =
                formulario.nombre,
            onValueChange = {
                viewModel.actualizarNombre(it)
            },
            label = {
                Text("Nombre de contacto")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true,
            isError =
                errores.nombre != null,
            supportingText = {

                errores.nombre?.let {
                    Text(it)
                }
            }
        )

        // MOTIVO

        OutlinedTextField(
            value =
                formulario.motivo,
            onValueChange = {
                viewModel.actualizarMotivo(it)
            },
            label = {
                Text(
                    "Motivo de contacto"
                )
            },
            modifier =
                Modifier.fillMaxWidth(),
            minLines = 3,
            isError =
                errores.motivo != null,
            supportingText = {

                errores.motivo?.let {
                    Text(it)
                }
            }
        )

        // CANTIDAD DE PARTICIPANTES - INT

        OutlinedTextField(
            value =
                formulario
                    .cantidadParticipantes
                    ?.toString()
                    ?: "",
            onValueChange = {

                viewModel
                    .actualizarCantidadParticipantes(
                        it
                    )
            },
            label = {
                Text(
                    "Cantidad de participantes"
                )
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Number
                ),
            isError =
                errores
                    .cantidadParticipantes != null,
            supportingText = {

                if (
                    errores
                        .cantidadParticipantes != null
                ) {

                    Text(
                        errores
                            .cantidadParticipantes!!
                    )

                } else {

                    Text(
                        "Personas estimadas para la actividad o colaboración"
                    )
                }
            }
        )

        // CHECKBOX

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Checkbox(
                checked =
                    formulario
                        .aceptaTerminos,
                onCheckedChange = {

                    viewModel
                        .actualizarAceptaTerminos(
                            it
                        )
                }
            )

            Text(
                "Acepto los términos y condiciones"
            )
        }

        errores.aceptaTerminos
            ?.let {

                Text(
                    text = it,
                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }

        // SWITCH

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        "¿Deseas que Alba Lab te contacte?"
                )

                Text(
                    text =
                        "Activa esta opción para solicitar seguimiento.",
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall
                )
            }

            Switch(
                checked =
                    formulario
                        .solicitaSeguimiento,
                onCheckedChange = {

                    viewModel
                        .actualizarSolicitaSeguimiento(
                            it
                        )
                }
            )
        }

        // FECHA

        val fechaTexto =
            formulario.fecha?.let {

                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(
                    Date(it)
                )

            } ?: "Seleccionar fecha"

        OutlinedTextField(
            value = fechaTexto,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    "Fecha de actividad"
                )
            },
            modifier =
                Modifier.fillMaxWidth(),
            isError =
                errores.fecha != null,
            supportingText = {

                errores.fecha?.let {
                    Text(it)
                }
            }
        )

        Button(
            onClick = {
                mostrarDatePicker = true
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                "Seleccionar fecha"
            )
        }

        // LUGAR

        ExposedDropdownMenuBox(
            expanded =
                lugarExpandido,
            onExpandedChange = {

                lugarExpandido =
                    !lugarExpandido
            }
        ) {

            OutlinedTextField(
                value =
                    formulario.lugar,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        "Comuna o lugar"
                    )
                },
                trailingIcon = {

                    ExposedDropdownMenuDefaults
                        .TrailingIcon(
                            expanded =
                                lugarExpandido
                        )
                },
                modifier =
                    Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                isError =
                    errores.lugar != null,
                supportingText = {

                    errores.lugar?.let {
                        Text(it)
                    }
                }
            )

            ExposedDropdownMenu(
                expanded =
                    lugarExpandido,
                onDismissRequest = {

                    lugarExpandido =
                        false
                }
            ) {

                lugares.forEach {
                        lugar ->

                    DropdownMenuItem(
                        text = {
                            Text(lugar)
                        },
                        onClick = {

                            viewModel
                                .actualizarLugar(
                                    lugar
                                )

                            lugarExpandido =
                                false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        // GUARDAR

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
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                "ENVIAR SOLICITUD"
            )
        }
    }

    // DATE PICKER

    if (mostrarDatePicker) {

        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    formulario.fecha
            )

        DatePickerDialog(
            onDismissRequest = {
                mostrarDatePicker = false
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        viewModel
                            .actualizarFecha(
                                datePickerState
                                    .selectedDateMillis
                            )

                        mostrarDatePicker =
                            false
                    }
                ) {

                    Text("Aceptar")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarDatePicker =
                            false
                    }
                ) {

                    Text("Cancelar")
                }
            }
        ) {

            DatePicker(
                state =
                    datePickerState
            )
        }
    }
}