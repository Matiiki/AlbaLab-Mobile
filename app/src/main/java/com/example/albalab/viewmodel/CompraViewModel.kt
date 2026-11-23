package com.example.albalab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.data.local.AlbaLabDatabase
import com.example.albalab.data.local.CompraEntity
import com.example.albalab.data.repository.CompraRepository
import com.example.albalab.model.HistorialCompraRequest
import com.example.albalab.model.ProductoModel
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompraViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AlbaLabDatabase.obtenerDatabase(application)
    private val repository = CompraRepository(database.albaLabDao())

    private val _compras = MutableStateFlow<List<CompraEntity>>(emptyList())
    val compras: StateFlow<List<CompraEntity>> = _compras.asStateFlow()

    fun cargarCompras(email: String, token: String = "") {
        if (email.isBlank()) {
            _compras.value = emptyList()
            return
        }
        viewModelScope.launch {
            // Intentar backend primero
            if (token.isNotBlank()) {
                try {
                    val response = RetrofitClient.apiService.obtenerComprasHistorial("Bearer $token")
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        _compras.value = body.map { item ->
                            CompraEntity(
                                id = item.id,
                                usuarioEmail = item.usuarioEmail.ifBlank { email },
                                fecha = item.fecha,
                                total = item.total,
                                productos = item.productos
                            )
                        }
                        return@launch
                    }
                } catch (_: Exception) {
                }
            }
            // Fallback local
            repository.obtenerCompras(email).collect { lista ->
                _compras.value = lista
            }
        }
    }

    fun guardarCompra(email: String, token: String = "", productos: List<ProductoModel>) {
        if (email.isBlank() || productos.isEmpty()) return
        viewModelScope.launch {
            // Guardar local
            repository.guardarCompra(email, productos)
            // Guardar backend
            if (token.isNotBlank()) {
                try {
                    val total = productos.sumOf { it.precio }
                    val detalle = productos
                        .groupBy { it.nombre }
                        .map { (nombre, items) -> "$nombre x${items.size}" }
                        .joinToString(", ")
                    RetrofitClient.apiService.guardarCompraHistorial(
                        "Bearer $token",
                        HistorialCompraRequest(productos = detalle, total = total)
                    )
                } catch (_: Exception) {
                }
            }
            cargarCompras(email, token)
        }
    }
}
