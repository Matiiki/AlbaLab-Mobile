package com.example.albalab.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.albalab.data.CamaraUriFactory
import com.example.albalab.viewmodel.FormularioViewModel2
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonacionesScreen(
    modifier: Modifier = Modifier,
    viewModel: FormularioViewModel2 = viewModel(),
    onGuardarExitoso: () -> Unit
) {
    // Paleta de colores personalizada
    val AzulPrimario = Color(0xFF0D47A1)
    val AzulMedio = Color(0xFF1976D2)
    val CelesteFondo = Color(0xFFF0F8FF)
    val CelesteClaro = Color(0xFFE3F2FD)
    val Blanco = Color(0xFFFFFFFF)

    val formulario by viewModel.formulario2.collectAsState()
    val errores by viewModel.errores2.collectAsState()

    val context = LocalContext.current

    var fotoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var donacionAnonima by remember { mutableStateOf<Boolean?>(null) }
    var uriTemporal by remember { mutableStateOf<android.net.Uri?>(null) }

    // Guardamos automáticamente la fecha actual
    LaunchedEffect(Unit) {
        if (formulario.fecha == null) {
            viewModel.actualizarFecha(System.currentTimeMillis())
        }
    }

    // ------------------------------------------------
    // GUARDAR DONACIÓN EN ROOM
    // ------------------------------------------------
    fun guardarDonacion() {
        val esAnonima = donacionAnonima
        if (esAnonima == null) {
            Toast.makeText(context, "Debes indicar si la donación será anónima", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.guardarDonacionRoom(
            esAnonimo = esAnonima,
            fotoUri = fotoUri,
            onGuardado = {
                Toast.makeText(context, "Donación guardada correctamente", Toast.LENGTH_SHORT).show()
                onGuardarExitoso()
            }
        )
    }

    // ------------------------------------------------
    // CÁMARA
    // ------------------------------------------------
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { exito ->
            if (exito) {
                fotoUri = uriTemporal
            } else {
                viewModel.actualizarAceptaCamara(false)
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val nuevaUri = CamaraUriFactory.crearUri(context)
                uriTemporal = nuevaUri
                cameraLauncher.launch(nuevaUri)
            } else {
                viewModel.actualizarAceptaCamara(false)
                Toast.makeText(context, "Se requiere permiso para usar la cámara", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // ------------------------------------------------
    // BIOMETRÍA
    // ------------------------------------------------
    val fragmentActivity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(fragmentActivity)
    val biometricManager = BiometricManager.from(context)

    val biometricPrompt = remember {
        BiometricPrompt(
            fragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(context, "Error: $errString", Toast.LENGTH_SHORT).show()
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    guardarDonacion()
                }
                override fun onAuthenticationFailed() {
                    Toast.makeText(context, "Biometría no reconocida", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirmar pago")
            .setSubtitle("Usa tu biometría para aprobar la donación")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    // ------------------------------------------------
    // INTERFAZ MODERNA
    // ------------------------------------------------
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CelesteFondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera con ícono
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Corazón donación",
                tint = AzulMedio,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Donaciones",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = AzulPrimario
            )
            Text(
                text = "Tu aporte nos ayuda a crecer",
                style = MaterialTheme.typography.bodyMedium,
                color = AzulMedio
            )
        }

        // Tarjeta del formulario
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
                // MONTO
                OutlinedTextField(
                    value = formulario.monto,
                    onValueChange = { viewModel.actualizarMonto(it) },
                    label = { Text("Monto") },
                    placeholder = { Text("Ej: 5000") },
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = "Monto", tint = AzulMedio) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errores.monto != null,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = AzulMedio,
                        focusedLabelColor = AzulPrimario,
                        unfocusedContainerColor = CelesteClaro.copy(alpha = 0.4f),
                        focusedContainerColor = Blanco
                    ),
                    supportingText = {
                        if (errores.monto != null) {
                            Text(errores.monto!!)
                        } else {
                            Text("Ingrese un valor superior a 1000", color = AzulMedio)
                        }
                    }
                )

                // ANÓNIMO
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "¿Deseas realizar una donación anónima?",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = donacionAnonima == true,
                                onClick = {
                                    donacionAnonima = true
                                    fotoUri = null
                                    uriTemporal = null
                                    viewModel.actualizarAceptaCamara(false)
                                    viewModel.actualizarNombre("") // Limpia el nombre en el ViewModel
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = AzulPrimario)
                            )
                            Text("Sí", color = AzulPrimario)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = donacionAnonima == false,
                                onClick = {
                                    donacionAnonima = false
                                    viewModel.actualizarAceptaCamara(true)

                                    val permisoConcedido = ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED

                                    if (permisoConcedido) {
                                        val nuevaUri = CamaraUriFactory.crearUri(context)
                                        uriTemporal = nuevaUri
                                        cameraLauncher.launch(nuevaUri)
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = AzulPrimario)
                            )
                            Text("No", color = AzulPrimario)
                        }
                    }
                }

                // NOMBRE
                OutlinedTextField(
                    value = if (donacionAnonima == true) "Anónimo" else formulario.nombre,
                    onValueChange = { if (donacionAnonima != true) viewModel.actualizarNombre(it) },
                    label = { Text("Nombre") },
                    enabled = donacionAnonima != true,
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Nombre", tint = if (donacionAnonima == true) Color.Gray else AzulMedio)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errores.nombre != null && donacionAnonima != true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = AzulMedio,
                        focusedLabelColor = AzulPrimario,
                        unfocusedContainerColor = if (donacionAnonima == true) Color.LightGray.copy(alpha = 0.2f) else CelesteClaro.copy(alpha = 0.4f),
                        focusedContainerColor = Blanco,
                        disabledBorderColor = Color.LightGray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.DarkGray
                    ),
                    supportingText = {
                        if (donacionAnonima != true) {
                            errores.nombre?.let { Text(it) }
                        }
                    }
                )

                // FECHA
                val fechaAUsar = formulario.fecha ?: System.currentTimeMillis()
                val fechaTexto = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.format(Date(fechaAUsar))

                OutlinedTextField(
                    value = fechaTexto,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Fecha") },
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Fecha", tint = AzulMedio) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = CelesteClaro,
                        disabledLabelColor = AzulMedio,
                        disabledTextColor = AzulPrimario,
                        disabledContainerColor = CelesteClaro.copy(alpha = 0.2f)
                    )
                )

                // FOTO
                if (donacionAnonima == false && fotoUri != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto del donante",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(3.dp, AzulMedio, CircleShape)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // BOTÓN PAGAR
        Button(
            onClick = {
                if (donacionAnonima == null) {
                    Toast.makeText(context, "Selecciona si la donación será anónima", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val formularioValido = viewModel.validarFormulario(esAnonimo = donacionAnonima == true)
                if (!formularioValido) return@Button

                val biometricStatus = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)

                if (biometricStatus == BiometricManager.BIOMETRIC_SUCCESS) {
                    biometricPrompt.authenticate(promptInfo)
                } else {
                    Toast.makeText(context, "Biometría no disponible. Continuando sin autenticación.", Toast.LENGTH_SHORT).show()
                    guardarDonacion()
                }
            },
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
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp)
            )
            Text(
                text = "PAGAR DONACIÓN",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}