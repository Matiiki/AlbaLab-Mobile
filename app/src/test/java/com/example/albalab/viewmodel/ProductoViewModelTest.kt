package com.example.albalab.viewmodel

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductoViewModelTest {

    @Test
    fun `inicia sin productos ni errores`() {
        val viewModel = ProductoViewModel()

        assertTrue(viewModel.productos.isEmpty())
        assertFalse(viewModel.cargando)
        assertTrue(viewModel.mensajeError.isEmpty())
    }
}
