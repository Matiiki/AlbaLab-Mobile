package com.example.albalab.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResumenCompraViewModelTest {

    @Test
    fun `inicia con el formulario vacio y sin errores`() {
        val viewModel = ResumenCompraViewModel()

        assertEquals("", viewModel.formulario.value.nombre)
        assertEquals("", viewModel.formulario.value.correo)
        assertEquals("", viewModel.formulario.value.direccion)
        assertNull(viewModel.nombreError.value)
        assertNull(viewModel.correoError.value)
        assertNull(viewModel.direccionError.value)
    }

    @Test
    fun `actualiza los campos del formulario`() {
        val viewModel = ResumenCompraViewModel()

        viewModel.actualizarNombre("Felipe")
        viewModel.actualizarCorreo("felipe@test.cl")
        viewModel.actualizarDireccion("Av. Siempre Viva 123")

        assertEquals("Felipe", viewModel.formulario.value.nombre)
        assertEquals("felipe@test.cl", viewModel.formulario.value.correo)
        assertEquals(
            "Av. Siempre Viva 123",
            viewModel.formulario.value.direccion
        )
    }

    @Test
    fun `un formulario completo y valido retorna true`() {
        val viewModel = ResumenCompraViewModel()
        viewModel.actualizarNombre("Felipe")
        viewModel.actualizarCorreo("felipe@test.cl")
        viewModel.actualizarDireccion("Av. Siempre Viva 123")

        assertTrue(viewModel.validarFormulario())
        assertNull(viewModel.nombreError.value)
        assertNull(viewModel.correoError.value)
        assertNull(viewModel.direccionError.value)
    }

    @Test
    fun `un formulario vacio retorna false y marca los tres errores`() {
        val viewModel = ResumenCompraViewModel()

        assertFalse(viewModel.validarFormulario())

        assertNotNull(viewModel.nombreError.value)
        assertNotNull(viewModel.correoError.value)
        assertNotNull(viewModel.direccionError.value)
    }

    @Test
    fun `nombre corto correo invalido y direccion vacia retornan false`() {
        val viewModel = ResumenCompraViewModel()
        viewModel.actualizarNombre("Fe")
        viewModel.actualizarCorreo("correo-sin-arroba")
        viewModel.actualizarDireccion("")

        assertFalse(viewModel.validarFormulario())

        assertEquals(
            "El nombre debe tener al menos 3 caracteres",
            viewModel.nombreError.value
        )
        assertNotNull(viewModel.correoError.value)
        assertNotNull(viewModel.direccionError.value)
    }

    @Test
    fun `al corregir los datos los errores se limpian`() {
        val viewModel = ResumenCompraViewModel()

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.nombreError.value)

        viewModel.actualizarNombre("Felipe")
        viewModel.actualizarCorreo("felipe@test.cl")
        viewModel.actualizarDireccion("Av. Siempre Viva 123")

        assertTrue(viewModel.validarFormulario())
        assertNull(viewModel.nombreError.value)
        assertNull(viewModel.correoError.value)
        assertNull(viewModel.direccionError.value)
    }
}
