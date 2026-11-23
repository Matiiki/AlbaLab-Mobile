package com.example.albalab.viewmodel

import com.example.albalab.model.ProductoRequest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminProductoViewModelTest {

    private val request = ProductoRequest(
        nombre = "Producto de prueba",
        precio = 2500
    )

    @Test
    fun `cargar productos sin token muestra error de sesion`() {
        val viewModel = AdminProductoViewModel()

        viewModel.cargarProductos("")

        assertTrue(viewModel.mensajeError.isNotBlank())
        assertFalse(viewModel.cargandoLista)
    }

    @Test
    fun `crear producto sin token no llama al callback de exito`() {
        val viewModel = AdminProductoViewModel()
        var exito = false

        viewModel.crearProducto("", request) { exito = true }

        assertTrue(viewModel.mensajeError.isNotBlank())
        assertFalse(exito)
        assertFalse(viewModel.guardando)
    }

    @Test
    fun `actualizar producto sin token no llama al callback de exito`() {
        val viewModel = AdminProductoViewModel()
        var exito = false

        viewModel.actualizarProducto("", 1L, request) { exito = true }

        assertTrue(viewModel.mensajeError.isNotBlank())
        assertFalse(exito)
        assertFalse(viewModel.guardando)
    }

    @Test
    fun `eliminar producto sin token no llama al callback de exito`() {
        val viewModel = AdminProductoViewModel()
        var exito = false

        viewModel.eliminarProducto("", 1L) { exito = true }

        assertTrue(viewModel.mensajeError.isNotBlank())
        assertFalse(exito)
        assertFalse(viewModel.guardando)
    }

    @Test
    fun `limpiar mensajes borra error y exito`() {
        val viewModel = AdminProductoViewModel()
        viewModel.cargarProductos("")

        viewModel.limpiarMensajes()

        assertTrue(viewModel.mensajeError.isEmpty())
        assertTrue(viewModel.mensajeExito.isEmpty())
    }

    @Test
    fun `limpiar productos deja la lista vacia`() {
        val viewModel = AdminProductoViewModel()

        viewModel.limpiarProductos()

        assertTrue(viewModel.productos.isEmpty())
        assertTrue(viewModel.mensajeError.isEmpty())
        assertTrue(viewModel.mensajeExito.isEmpty())
    }
}
