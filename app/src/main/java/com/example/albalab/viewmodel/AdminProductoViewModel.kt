package com.example.albalab.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albalab.model.ProductoModel
import com.example.albalab.model.ProductoRequest
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Response

class AdminProductoViewModel : ViewModel() {

    var productos by mutableStateOf<List<ProductoModel>>(emptyList())
        private set

    var cargandoLista by mutableStateOf(false)
        private set

    var guardando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    var mensajeExito by mutableStateOf("")
        private set

    fun limpiarMensajes() {
        mensajeError = ""
        mensajeExito = ""
    }

    fun cargarProductos(token: String) {
        if (token.isBlank()) {
            mensajeError = "La sesión expiró. Vuelve a iniciar sesión."
            return
        }

        if (cargandoLista) return

        viewModelScope.launch {
            try {
                cargandoLista = true
                mensajeError = ""

                val response = RetrofitClient.productApiService.listarProductos(
                    "Bearer $token"
                )

                if (response.isSuccessful) {
                    productos = response.body().orEmpty()
                } else {
                    mensajeError = mensajeDeError(response, "cargar los productos")
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al cargar los productos"
            } finally {
                cargandoLista = false
            }
        }
    }

    fun crearProducto(
        token: String,
        request: ProductoRequest,
        onSuccess: () -> Unit
    ) {
        guardarProducto(token, request, null, onSuccess)
    }

    fun actualizarProducto(
        token: String,
        id: Long,
        request: ProductoRequest,
        onSuccess: () -> Unit
    ) {
        guardarProducto(token, request, id, onSuccess)
    }

    private fun guardarProducto(
        token: String,
        request: ProductoRequest,
        id: Long?,
        onSuccess: () -> Unit
    ) {
        if (guardando) return

        if (token.isBlank()) {
            mensajeError = "La sesión expiró. Vuelve a iniciar sesión."
            return
        }

        viewModelScope.launch {
            try {
                guardando = true
                mensajeError = ""
                mensajeExito = ""

                val response = if (id == null) {
                    RetrofitClient.productApiService.crearProducto(
                        "Bearer $token",
                        request
                    )
                } else {
                    RetrofitClient.productApiService.actualizarProducto(
                        "Bearer $token",
                        id,
                        request
                    )
                }

                if (response.isSuccessful) {
                    mensajeExito = if (id == null) {
                        "Producto creado correctamente"
                    } else {
                        "Producto actualizado correctamente"
                    }
                    cargarProductos(token)
                    onSuccess()
                } else {
                    mensajeError = mensajeDeError(
                        response,
                        if (id == null) "crear el producto" else "actualizar el producto"
                    )
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al guardar el producto"
            } finally {
                guardando = false
            }
        }
    }

    fun eliminarProducto(
        token: String,
        id: Long,
        onSuccess: () -> Unit
    ) {
        if (guardando) return

        if (token.isBlank()) {
            mensajeError = "La sesión expiró. Vuelve a iniciar sesión."
            return
        }

        viewModelScope.launch {
            try {
                guardando = true
                mensajeError = ""
                mensajeExito = ""

                val response = RetrofitClient.productApiService.eliminarProducto(
                    "Bearer $token",
                    id
                )

                if (response.isSuccessful) {
                    mensajeExito = "Producto eliminado correctamente"
                    cargarProductos(token)
                    onSuccess()
                } else {
                    mensajeError = mensajeDeError(response, "eliminar el producto")
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión al eliminar el producto"
            } finally {
                guardando = false
            }
        }
    }

    fun limpiarProductos() {
        productos = emptyList()
        limpiarMensajes()
    }

    private fun mensajeDeError(response: Response<*>, accion: String): String {
        return when (response.code()) {
            400 -> "Revisa los datos del producto."
            401, 403 -> "No tienes permisos para $accion."
            404 -> "El producto ya no existe."
            409 -> "Ya existe un producto con ese nombre."
            else -> "No se pudo $accion (código ${response.code()})"
        }
    }
}
