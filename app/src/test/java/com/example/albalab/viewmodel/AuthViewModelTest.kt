package com.example.albalab.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class AuthViewModelTest {

    @Test
    fun `login con campos vacios marca los errores sin llamar al backend`() {
        val viewModel = AuthViewModel()
        var exito = false

        viewModel.login("", "", { exito = true })

        assertNotNull(viewModel.errorEmail)
        assertNotNull(viewModel.errorPassword)
        assertFalse(exito)
        assertFalse(viewModel.cargando)
        assertEquals("", viewModel.token)
    }

    @Test
    fun `login sin email pero con password solo marca el error de email`() {
        val viewModel = AuthViewModel()

        viewModel.login("", "123456", {})

        assertNotNull(viewModel.errorEmail)
        assertNull(viewModel.errorPassword)
    }

    @Test
    fun `registro con campos vacios marca los tres errores`() {
        val viewModel = AuthViewModel()
        var exito = false

        viewModel.registrar("", "", "", { exito = true })

        assertNotNull(viewModel.errorEmail)
        assertNotNull(viewModel.errorPassword)
        assertNotNull(viewModel.errorConfirmarPassword)
        assertFalse(exito)
        assertFalse(viewModel.cargando)
    }

    @Test
    fun `registro con passwords distintas marca error de confirmacion`() {
        val viewModel = AuthViewModel()

        viewModel.registrar("", "123456", "654321", {})

        assertNotNull(viewModel.errorEmail)
        assertNull(viewModel.errorPassword)
        assertNotNull(viewModel.errorConfirmarPassword)
    }

    @Test
    fun `limpiar errores deja los mensajes en null y vacio`() {
        val viewModel = AuthViewModel()
        viewModel.login("", "", {})

        viewModel.limpiarErrores()

        assertNull(viewModel.errorEmail)
        assertNull(viewModel.errorPassword)
        assertNull(viewModel.errorConfirmarPassword)
        assertEquals("", viewModel.mensajeError)
    }
}
