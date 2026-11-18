package com.example.albalab

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.ui.theme.AlbaLabTheme
import com.example.albalab.viewmodel.ThemeViewModel
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la app ocupe toda la pantalla (detrás de la barra de estado)
        enableEdgeToEdge()

        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val modoOscuro = themeViewModel.modoOscuro

            // Mantiene los íconos de las barras del sistema legibles según el tema
            val view = LocalView.current
            SideEffect {
                val activity = view.context as? Activity ?: return@SideEffect
                WindowCompat.getInsetsController(activity.window, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = !modoOscuro
                }
            }

            AlbaLabTheme(darkTheme = modoOscuro) {
                // Surface actúa como el fondo base de la app, aplicando los colores del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(
                        modoOscuro = modoOscuro,
                        onModoOscuroChange = themeViewModel::actualizarModoOscuro
                    )
                }
            }
        }
    }
}
