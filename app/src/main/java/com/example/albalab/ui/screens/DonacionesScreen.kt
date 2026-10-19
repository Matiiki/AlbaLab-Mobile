package com.example.albalab.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.albalab.data.CamaraUriFactory
import com.example.albalab.viewmodel.FormularioViewModel2
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.RadioButton

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

    var fotoUri by remember {
        mutableStateOf<android.net.Uri?>(null)
    }
    var donacionAnonima by remember {
        mutableStateOf<Boolean?>(null)
    }

    var uriTemporal by remember {
        mutableStateOf<android.net.Uri?>(null)
    }

    fun guardarDonacion() {

        val seGuardo = viewModel.guardar()

        if (seGuardo) {

            viewModel.guardarColaboradorLocal(
                nombre = formulario.nombre,
                fotoUri = fotoUri
            )

            onGuardarExitoso()
        }
    }

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

                Toast.makeText(
                    context,
                    "Se requiere permiso para usar la cámara",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    val fragmentActivity = context as FragmentActivity

    val executor = ContextCompat.getMainExecutor(fragmentActivity)

    val biometricManager = BiometricManager.from(context)

    val biometricPrompt = remember {

        BiometricPrompt(
            fragmentActivity,
            executor,

            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {

                    Toast.makeText(
                        context,
                        "Error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {

                    guardarDonacion()
                }

                override fun onAuthenticationFailed() {

                    Toast.makeText(
                        context,
                        "Biometría no reconocida",
                        Toast.LENGTH_SHORT
                    ).show()
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

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = formulario.monto,
            onValueChange = {
                viewModel.actualizarMonto(it)
            },
            label = {
                Text("Monto")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
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
            onValueChange = {
                viewModel.actualizarNombre(it)
            },
            label = {
                Text("Nombre")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errores.nombre != null,
            supportingText = {

                errores.nombre?.let {
                    Text(it)
                }
            }
        )

        val fechaAUsar =
            formulario.fecha ?: System.currentTimeMillis()

        val fechaTexto =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).apply {

                timeZone =
                    java.util.TimeZone.getTimeZone("UTC")

            }.format(
                Date(fechaAUsar)
            )

        OutlinedTextField(
            value = fechaTexto,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = {
                Text("Fecha")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "¿Deseas realizar una donación anónima?",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            RadioButton(
                selected = donacionAnonima == true,
                onClick = {

                    donacionAnonima = true

                    // Si la donación es anónima, no necesitamos foto
                    fotoUri = null

                    viewModel.actualizarAceptaCamara(false)
                }
            )

            Text("Sí")

            RadioButton(
                selected = donacionAnonima == false,
                onClick = {

                    donacionAnonima = false

                    viewModel.actualizarAceptaCamara(true)

                    val permisoConcedido =
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                    if (permisoConcedido) {

                        val nuevaUri =
                            CamaraUriFactory.crearUri(context)

                        uriTemporal = nuevaUri

                        cameraLauncher.launch(nuevaUri)

                    } else {

                        permissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                }
            )

            Text("No")
        }

        if (donacionAnonima == false) {

            fotoUri?.let { uri ->

                AsyncImage(
                    model = uri,
                    contentDescription = "Foto del donante",
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Button(
            onClick = {

                val biometricStatus =
                    biometricManager.canAuthenticate(
                        BiometricManager.Authenticators.BIOMETRIC_WEAK
                    )

                if (
                    biometricStatus ==
                    BiometricManager.BIOMETRIC_SUCCESS
                ) {

                    biometricPrompt.authenticate(
                        promptInfo
                    )

                } else {

                    Toast.makeText(
                        context,
                        "Biometría no disponible. Continuando sin autenticación biométrica.",
                        Toast.LENGTH_SHORT
                    ).show()

                    guardarDonacion()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Pagar")
        }
    }
}