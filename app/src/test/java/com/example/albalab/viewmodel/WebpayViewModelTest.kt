package com.example.albalab.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WebpayViewModelTest {

    @Test
    fun `inicia sin transaccion ni errores`() {
        val viewModel = WebpayViewModel()

        assertNull(viewModel.webpayUrl)
        assertNull(viewModel.webpayToken)
        assertNull(viewModel.mensajeError)
        assertNull(viewModel.errorColaboracion)
        assertFalse(viewModel.cargando)
        assertFalse(viewModel.confirmando)
        assertFalse(viewModel.pagoAprobado)
        assertFalse(viewModel.guardandoColaboracion)
        assertFalse(viewModel.pagoTerminado)
        assertFalse(viewModel.sesionExpirada)
    }

    @Test
    fun `iniciar pago sin token marca la sesion como expirada`() {
        val viewModel = WebpayViewModel()

        viewModel.iniciarPago(
            pagoId = 1,
            montoTotal = 5000.0,
            tokenUsuario = "",
            ordenCompra = "DON-123"
        )

        assertTrue(viewModel.sesionExpirada)
        assertNotNull(viewModel.mensajeError)
        assertNull(viewModel.webpayUrl)
        assertNull(viewModel.webpayToken)
        assertFalse(viewModel.cargando)
        assertFalse(viewModel.pagoTerminado)
    }

    @Test
    fun `confirmar sin token de webpay muestra error y no confirma`() {
        val viewModel = WebpayViewModel()

        viewModel.confirmarPago(tokenUsuario = "jwt-de-prueba")

        assertNotNull(viewModel.mensajeError)
        assertFalse(viewModel.confirmando)
        assertFalse(viewModel.pagoAprobado)
        assertFalse(viewModel.pagoTerminado)
    }

    @Test
    fun `limpiar sesion deja el viewmodel en su estado inicial`() {
        val viewModel = WebpayViewModel()

        viewModel.iniciarPago(
            pagoId = 1,
            montoTotal = 5000.0,
            tokenUsuario = ""
        )
        assertTrue(viewModel.sesionExpirada)

        viewModel.limpiarSesion()

        assertNull(viewModel.webpayUrl)
        assertNull(viewModel.webpayToken)
        assertNull(viewModel.mensajeError)
        assertNull(viewModel.errorColaboracion)
        assertFalse(viewModel.cargando)
        assertFalse(viewModel.confirmando)
        assertFalse(viewModel.pagoAprobado)
        assertFalse(viewModel.guardandoColaboracion)
        assertFalse(viewModel.pagoTerminado)
        assertFalse(viewModel.sesionExpirada)
    }

    @Test
    fun `un segundo iniciar pago con el mismo id no reinicia el estado`() {
        val viewModel = WebpayViewModel()

        viewModel.iniciarPago(
            pagoId = 7,
            montoTotal = 5000.0,
            tokenUsuario = ""
        )
        viewModel.iniciarPago(
            pagoId = 7,
            montoTotal = 9999.0,
            tokenUsuario = ""
        )

        assertTrue(viewModel.sesionExpirada)
        assertEquals(
            "Debes iniciar sesi\u00f3n para pagar con Webpay.",
            viewModel.mensajeError
        )
    }
}
