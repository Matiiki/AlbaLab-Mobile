package com.example.albalab.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.model.ProductoModel
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    var productos by mutableStateOf<List<ProductoModel>>(emptyList())
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    fun cargarProductos() {
        if (cargando) return

        viewModelScope.launch {
            try {
                cargando = true
                mensajeError = ""

                val response = RetrofitClient.productApiService.listarProductos()

                if (response.isSuccessful) {
                    productos = response.body().orEmpty()
                } else {
                    mensajeError = when (response.code()) {
                        401, 403 -> "No se pudo cargar el catálogo. Intenta nuevamente."
                        else -> "No se pudieron cargar los productos (código ${response.code()})"
                    }
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al cargar los productos"
            } finally {
                cargando = false
            }
        }
    }
}
