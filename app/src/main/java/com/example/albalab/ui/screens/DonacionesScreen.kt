package com.example.albalab.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
    token: String = "",
    onIrALogin: () -> Unit = {},
    onIrAWebpay: (Int) -> Unit = {}
) {
    // Paleta de colores personalizada
    val AzulPrimario = MaterialTheme.colorScheme.primary
    val AzulMedio = MaterialTheme.colorScheme.secondary
    val CelesteFondo = MaterialTheme.colorScheme.background
    val CelesteClaro = MaterialTheme.colorScheme.primaryContainer
    val Blanco = MaterialTheme.colorScheme.surface

    val formulario by viewModel.formulario2.collectAsState()
    val errores by viewModel.errores2.collectAsState()

    val context = LocalContext.current

    var uriTemporal by rememberSaveable { mutableStateOf<String?>(null) }

    // Guardamos automáticamente la fecha actual
    LaunchedEffect(Unit) {
        if (formulario.fecha == null) {
            viewModel.actualizarFecha(System.currentTimeMillis())
        }
    }

    // ------------------------------------------------
    // PREPARAR PAGO CON WEBPAY
    // ------------------------------------------------
    fun prepararPagoDonacion() {
        val pendiente = viewModel.prepararDonacion()
        if (pendiente == null) {
            Toast.makeText(context, "Revisa los datos de la donación", Toast.LENGTH_SHORT).show()
            return
        }

        onIrAWebpay(pendiente.monto)
    }

    // ------------------------------------------------
    // CÁMARA
    // ------------------------------------------------
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { exito ->
            if (exito) {
                viewModel.actualizarFotoUri(uriTemporal)
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
                uriTemporal = nuevaUri.toString()
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
                    prepararPagoDonacion()
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
            .imePadding()
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
                                selected = formulario.esAnonimo == true,
                                onClick = {
                                    viewModel.actualizarEsAnonimo(true)
                                    uriTemporal = null
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = AzulPrimario)
                            )
                            Text("Sí", color = AzulPrimario)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = formulario.esAnonimo == false,
                                onClick = {
                                    viewModel.actualizarEsAnonimo(false)
                                    viewModel.actualizarAceptaCamara(true)

                                    val permisoConcedido = ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED

                                    if (permisoConcedido) {
                                        val nuevaUri = CamaraUriFactory.crearUri(context)
                                        uriTemporal = nuevaUri.toString()
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
                    value = if (formulario.esAnonimo == true) "Anónimo" else formulario.nombre,
                    onValueChange = { if (formulario.esAnonimo != true) viewModel.actualizarNombre(it) },
                    label = { Text("Nombre") },
                    enabled = formulario.esAnonimo != true,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Nombre",
                            tint = if (formulario.esAnonimo == true) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                AzulMedio
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errores.nombre != null && formulario.esAnonimo != true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = AzulMedio,
                        focusedLabelColor = AzulPrimario,
                        unfocusedContainerColor = if (formulario.esAnonimo == true) {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        } else {
                            CelesteClaro.copy(alpha = 0.4f)
                        },
                        focusedContainerColor = Blanco,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    supportingText = {
                        if (formulario.esAnonimo != true) {
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
                if (formulario.esAnonimo == false) {
                    formulario.fotoUri?.let { foto ->
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = Uri.parse(foto),
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
        }

        if (token.isBlank()) {
            Text(
                text = "Inicia sesión para pagar la donación con Webpay.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // BOTÓN PAGAR
        Button(
            onClick = {
                val esAnonima = formulario.esAnonimo
                if (esAnonima == null) {
                    Toast.makeText(context, "Selecciona si la donación será anónima", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val formularioValido = viewModel.validarFormulario(esAnonimo = esAnonima)
                if (!formularioValido) return@Button

                if (token.isBlank()) {
                    Toast.makeText(context, "Debes iniciar sesión para pagar con Webpay", Toast.LENGTH_LONG).show()
                    onIrALogin()
                    return@Button
                }

                val biometricStatus = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)

                if (biometricStatus == BiometricManager.BIOMETRIC_SUCCESS) {
                    biometricPrompt.authenticate(promptInfo)
                } else {
                    Toast.makeText(context, "Biometría no disponible. Continuando sin autenticación.", Toast.LENGTH_SHORT).show()
                    prepararPagoDonacion()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulPrimario,
                contentColor = MaterialTheme.colorScheme.onPrimary
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
                text = "PAGAR CON WEBPAY",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}