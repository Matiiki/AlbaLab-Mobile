package com.example.albalab.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.albalab.model.Contenido
import com.example.albalab.model.ContenidoDefaults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ContenidoRepository {

    private const val PREFS_NAME = "albalab_contenido"

    private var prefs: SharedPreferences? = null

    private val _contenidos = MutableStateFlow(ContenidoDefaults.items)
    val contenidos: StateFlow<List<Contenido>> = _contenidos.asStateFlow()

    @Synchronized
    fun inicializar(context: Context) {
        if (prefs != null) return

        val preferences = context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        prefs = preferences

        _contenidos.value = ContenidoDefaults.items.map { item ->
            item.copy(
                titulo = preferences.getString("${item.clave}_titulo", item.titulo) ?: item.titulo,
                cuerpo = preferences.getString("${item.clave}_cuerpo", item.cuerpo) ?: item.cuerpo
            )
        }
    }

    fun guardar(items: List<Contenido>) {
        val preferences = prefs ?: return

        preferences.edit().apply {
            items.forEach { item ->
                putString("${item.clave}_titulo", item.titulo)
                putString("${item.clave}_cuerpo", item.cuerpo)
            }
        }.apply()

        _contenidos.value = items
    }

    fun restaurar() {
        prefs?.edit()?.clear()?.apply()
        _contenidos.value = ContenidoDefaults.items
    }
}
