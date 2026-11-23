package com.example.albalab.viewmodel

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminViewModelTest {

    @Test
    fun `inicia sin sesion ni administradores`() {
        val viewModel = AdminViewModel()

        assertFalse(viewModel.estaLogueado)
        assertTrue(viewModel.token.isEmpty())
        assertTrue(viewModel.emailAdmin.isEmpty())
        assertTrue(viewModel.admins.isEmpty())
        assertTrue(viewModel.mensajeError.isEmpty())
        assertTrue(viewModel.mensajeExito.isEmpty())
    }

    @Test
    fun `login con campos vacios muestra error sin llamar al backend`() {
        val viewModel = AdminViewModel()
        var exito = false

        viewModel.login("", "", { exito = true })

        assertTrue(viewModel.mensajeError.isNotBlank())
        assertFalse(exito)
        assertFalse(viewModel.cargando)
        assertFalse(viewModel.estaLogueado)
    }

    @Test
    fun `login sin email muestra error antes de validar el correo`() {
        val viewModel = AdminViewModel()

        viewModel.login("", "12345678", {})

        assertTrue(viewModel.mensajeError.isNotBlank())
        assertFalse(viewModel.cargando)
    }

    @Test
    fun `crear admin con correo vacio muestra error sin llamar al backend`() {
        val viewModel = AdminViewModel()
        var exito = false

        viewModel.crearAdmin("", "12345678", { exito = true })

        assertTrue(viewModel.mensajeError.isNotBlank())
        assertFalse(exito)
        assertFalse(viewModel.cargando)
    }

    @Test
    fun `actualizar admin con correo vacio muestra error sin llamar al backend`() {
        val viewModel = AdminViewModel()
        var exito = false

        viewModel.actualizarAdmin(1L, "", "", { exito = true })

        assertTrue(viewModel.mensajeError.isNotBlank())
        assertFalse(exito)
        assertFalse(viewModel.cargando)
    }

    @Test
    fun `limpiar mensajes borra error y exito`() {
        val viewModel = AdminViewModel()
        viewModel.login("", "", {})

        viewModel.limpiarMensajes()

        assertTrue(viewModel.mensajeError.isEmpty())
        assertTrue(viewModel.mensajeExito.isEmpty())
    }

    @Test
    fun `cerrar sesion limpia los datos de administrador`() {
        val viewModel = AdminViewModel()
        viewModel.login("", "", {})

        viewModel.cerrarSesion()

        assertFalse(viewModel.estaLogueado)
        assertTrue(viewModel.token.isEmpty())
        assertTrue(viewModel.emailAdmin.isEmpty())
        assertTrue(viewModel.admins.isEmpty())
        assertTrue(viewModel.mensajeError.isEmpty())
    }
}
