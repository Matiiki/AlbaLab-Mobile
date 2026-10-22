package com.example.albalab.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.viewmodel.AuthViewModel

@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    onRegistroExitoso: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmarPassword by remember {
        mutableStateOf("")
    }

    val context =
        LocalContext.current

    LaunchedEffect(Unit) {

        viewModel.limpiarErrores()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text =
                "Crear cuenta",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium,

            fontWeight =
                FontWeight.Bold,

            color =
                MaterialTheme
                    .colorScheme
                    .primary
        )


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        // ---------------------------------------------
        // CORREO
        // ---------------------------------------------

        OutlinedTextField(
            value =
                email,

            onValueChange = {

                email = it
            },

            label = {

                Text(
                    "Correo electrónico"
                )
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine =
                true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            isError =
                viewModel.errorEmail != null,

            supportingText = {

                viewModel.errorEmail
                    ?.let {

                        Text(
                            text = it
                        )
                    }
            }
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // ---------------------------------------------
        // CONTRASEÑA
        // ---------------------------------------------

        OutlinedTextField(
            value =
                password,

            onValueChange = {

                password = it
            },

            label = {

                Text(
                    "Contraseña"
                )
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine =
                true,

            visualTransformation =
                PasswordVisualTransformation(),

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            isError =
                viewModel.errorPassword != null,

            supportingText = {

                if (
                    viewModel.errorPassword != null
                ) {

                    Text(
                        text =
                            viewModel
                                .errorPassword!!
                    )

                } else {

                    Text(
                        "Mínimo 6 caracteres"
                    )
                }
            }
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // ---------------------------------------------
        // CONFIRMAR CONTRASEÑA
        // ---------------------------------------------

        OutlinedTextField(
            value =
                confirmarPassword,

            onValueChange = {

                confirmarPassword = it
            },

            label = {

                Text(
                    "Confirmar contraseña"
                )
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine =
                true,

            visualTransformation =
                PasswordVisualTransformation(),

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            isError =
                viewModel
                    .errorConfirmarPassword != null,

            supportingText = {

                viewModel
                    .errorConfirmarPassword
                    ?.let {

                        Text(
                            text = it
                        )
                    }
            }
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // ---------------------------------------------
        // ERROR BACKEND
        // ---------------------------------------------

        if (
            viewModel
                .mensajeError
                .isNotEmpty()
        ) {

            Text(
                text =
                    viewModel
                        .mensajeError,

                color =
                    Color.Red,

                modifier =
                    Modifier.padding(
                        bottom = 12.dp
                    )
            )
        }


        // ---------------------------------------------
        // REGISTRAR
        // ---------------------------------------------

        Button(
            onClick = {

                viewModel.registrar(
                    email =
                        email,

                    password =
                        password,

                    confirmarPassword =
                        confirmarPassword,

                    onSuccess = {

                        Toast.makeText(
                            context,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT
                        ).show()

                        onRegistroExitoso()
                    }
                )
            },

            enabled =
                !viewModel.cargando,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            if (
                viewModel.cargando
            ) {

                CircularProgressIndicator()

            } else {

                Text(
                    "REGISTRARSE"
                )
            }
        }
    }
}