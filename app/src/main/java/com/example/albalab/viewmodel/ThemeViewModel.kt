package com.example.albalab.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencias = application.getSharedPreferences(
        "albalab_preferencias",
        Context.MODE_PRIVATE
    )

    var modoOscuro by mutableStateOf(
        preferencias.getBoolean("modo_oscuro", false)
    )
        private set

    fun actualizarModoOscuro(activo: Boolean) {
        modoOscuro = activo
        preferencias.edit()
            .putBoolean("modo_oscuro", activo)
            .apply()
    }
}
