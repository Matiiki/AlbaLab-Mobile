package com.example.albalab.viewmodel

import com.example.albalab.model.ProductoModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppViewModelTest {

    @Test
    fun `inicia en Inicio y sin sesion`() {
        val viewModel = AppViewModel()

        assertEquals("Inicio", viewModel.pantallaActual)
        assertEquals("Inicio", viewModel.pantallaPostLogin)
        assertEquals("", viewModel.emailLogueado)
        assertEquals("", viewModel.passwordLogueada)
        assertEquals("", viewModel.tokenLogueado)
        assertEquals("Carrito", viewModel.webpayOrigen)
        assertEquals(0, viewModel.pagoWebpayId)
        assertTrue(viewModel.carrito.isEmpty())
    }

    @Test
    fun `iniciar sesion guarda email password y token`() {
        val viewModel = AppViewModel()

        viewModel.iniciarSesion(
            email = "felipe@gmai.com",
            password = "hola1234",
            token = "jwt-de-prueba"
        )

        assertEquals("felipe@gmai.com", viewModel.emailLogueado)
        assertEquals("hola1234", viewModel.passwordLogueada)
        assertEquals("jwt-de-prueba", viewModel.tokenLogueado)
    }

    @Test
    fun `cerrar sesion limpia los datos de autenticacion`() {
        val viewModel = AppViewModel()
        viewModel.iniciarSesion("matias@gmail.com", "hola1234", "jwt")

        viewModel.cerrarSesion()

        assertEquals("", viewModel.emailLogueado)
        assertEquals("", viewModel.passwordLogueada)
        assertEquals("", viewModel.tokenLogueado)
    }

    @Test
    fun `navegar a webpay guarda origen monto e incrementa el intento`() {
        val viewModel = AppViewModel()

        viewModel.navegarAWebpay(origen = "Donacion", montoDonacion = 5000)

        assertEquals("Webpay", viewModel.pantallaActual)
        assertEquals("Donacion", viewModel.webpayOrigen)
        assertEquals(5000, viewModel.montoDonacionPendiente)
        assertEquals(1, viewModel.pagoWebpayId)

        viewModel.navegarAWebpay(origen = "Carrito")

        assertEquals("Webpay", viewModel.pantallaActual)
        assertEquals("Carrito", viewModel.webpayOrigen)
        assertEquals(0, viewModel.montoDonacionPendiente)
        assertEquals(2, viewModel.pagoWebpayId)
    }

    @Test
    fun `el carrito conserva y permite limpiar productos`() {
        val viewModel = AppViewModel()
        val producto = ProductoModel(
            id = 1L,
            nombre = "Producto de prueba",
            precio = 2500
        )

        viewModel.carrito.add(producto)

        assertEquals(1, viewModel.carrito.size)
        assertEquals("Producto de prueba", viewModel.carrito.first().nombre)
        assertEquals(2500, viewModel.carrito.first().precio)

        viewModel.carrito.clear()

        assertTrue(viewModel.carrito.isEmpty())
    }
}
