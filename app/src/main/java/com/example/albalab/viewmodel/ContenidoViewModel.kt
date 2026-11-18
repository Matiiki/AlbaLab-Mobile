package com.example.albalab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.albalab.data.repository.ContenidoRepository
import com.example.albalab.model.Contenido

class ContenidoViewModel(
    application: Application
) : AndroidViewModel(application) {

    init {
        ContenidoRepository.inicializar(application)
    }

    val contenidos = ContenidoRepository.contenidos

    fun guardar(items: List<Contenido>) {
        ContenidoRepository.guardar(items)
    }

    fun restaurar() {
        ContenidoRepository.restaurar()
    }
}
