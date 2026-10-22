package com.example.albalab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onIrARegistro: () -> Unit,
    onLoginExitoso: (String, String) -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        viewModel.limpiarErrores()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text =
                "Iniciar sesión",

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

                viewModel.errorPassword
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
        // ERROR GENERAL
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
        // LOGIN
        // ---------------------------------------------

        Button(
            onClick = {

                viewModel.login(
                    email =
                        email,

                    password =
                        password,

                    onSuccess = {

                        onLoginExitoso(
                            email.trim(),
                            password
                        )
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
                    "INGRESAR"
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        TextButton(
            onClick =
                onIrARegistro
        ) {

            Text(
                "¿No tienes cuenta? Regístrate aquí"
            )
        }
    }
}