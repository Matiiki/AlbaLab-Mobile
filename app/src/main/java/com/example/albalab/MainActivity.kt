package com.example.albalab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.albalab.ui.theme.AlbalabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la app ocupe toda la pantalla (detrás de la barra de estado)
        enableEdgeToEdge()

        setContent {
            AlbalabTheme {
                // Surface actúa como el fondo base de la app, aplicando los colores de tu tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Aquí llamamos a tu función principal que contiene el Scaffold y el menú
                    App()
                }
            }
        }
    }
}