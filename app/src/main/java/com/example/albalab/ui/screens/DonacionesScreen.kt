package com.example.albalab.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.viewmodel.FormularioViewModel2
import androidx.compose.foundation.layout.size
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.text.SimpleDateFormat
import coil.compose.AsyncImage
import java.util.Date
import java.util.Locale

// IMPORTANTE: Asegúrate de que esta ruta coincida con tu proyecto real
import com.example.albalab.data.CamaraUriFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonacionesScreen(
    modifier: Modifier = Modifier,
    viewModel: FormularioViewModel2 = viewModel(),
    onGuardarExitoso: () -> Unit
) {
    val formulario by viewModel.formulario2.collectAsState()
    val errores by viewModel.errores2.collectAsState()

    val context = LocalContext.current

    // Ahora guardamos una Uri en lugar de un Bitmap
    var fotoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var uriTemporal by remember { mutableStateOf<android.net.Uri?>(null) }

    // 1. El lanzador que abre la cámara usando la URI
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { exito ->
            if (exito) {
                // Si toma la foto, guardamos la URI final
                fotoUri = uriTemporal
            } else {
                // Si cancela, desmarcamos el checkbox
                viewModel.actualizarAceptaCamara(false)
            }
        }
    )

    // 2. El lanzador que pide el permiso
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Si acepta el permiso, generamos la URI con el código de tu profe y abrimos la cámara
                val nuevaUri = CamaraUriFactory.crearUri(context)
                uriTemporal = nuevaUri
                cameraLauncher.launch(nuevaUri)
            } else {
                viewModel.actualizarAceptaCamara(false)
                Toast.makeText(context, "Se requiere permiso para usar la cámara", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Aseguramos que el contexto sea el FragmentActivity que configuraste
    val fragmentActivity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(fragmentActivity)

    // Configuramos qué pasa cuando el escáner lee la huella
    val biometricPrompt = remember {
        BiometricPrompt(
            fragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(context, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    val seGuardo = viewModel.guardar()
                    if (seGuardo) {
                        // ¡NUEVO! Guardamos el colaborador en la lista local
                        viewModel.guardarColaboradorLocal(
                            nombre = formulario.nombre,
                            fotoUri = fotoUri
                        )
                        onGuardarExitoso()
                    }
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(context, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // Diseñamos los textos de la ventanita que saldrá
    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirmar pago")
            .setSubtitle("Usa tu huella para aprobar la donación")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Donaciones",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = formulario.monto,
            onValueChange = { viewModel.actualizarMonto(it) },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number ),
            isError = errores.monto != null,
            supportingText = {
                if (errores.monto != null) {
                    Text(errores.monto!!)
                } else {
                    Text("Ingrese un valor superior a 1000")
                }
            }
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

        val fechaAUsar = formulario.fecha ?: System.currentTimeMillis()
        val fechaTexto = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }.format(Date(fechaAUsar))

        OutlinedTextField(
            value = fechaTexto,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = formulario.aceptaCamara,
                onCheckedChange = { activado ->
                    viewModel.actualizarAceptaCamara(activado)

                    if (activado) {
                        val permisoConcedido = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        if (permisoConcedido) {
                            // Generamos la URI con el código de tu profe
                            val nuevaUri = CamaraUriFactory.crearUri(context)
                            uriTemporal = nuevaUri
                            // Lanzamos la cámara enviándole la ruta donde debe guardar la foto
                            cameraLauncher.launch(nuevaUri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    } else {
                        // Si desmarca, borramos la ruta
                        fotoUri = null
                    }
                }
            )
            Text(text = "Quiero que me vean")
        }

        fotoUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Foto guardada",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        Button(
            onClick = {
                // Al presionar pagar, lanzamos el escáner de huellas
                biometricPrompt.authenticate(promptInfo)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pagar")
        }
    }
}