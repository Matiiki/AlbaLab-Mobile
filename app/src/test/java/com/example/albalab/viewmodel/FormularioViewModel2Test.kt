package com.example.albalab.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FormularioViewModel2Test {

    private lateinit var viewModel: FormularioViewModel2

    @Before
    fun setUp() {
        viewModel = FormularioViewModel2()
    }

    @Test
    fun `el monto solo acepta digitos`() {
        viewModel.actualizarMonto("5000")

        assertEquals("5000", viewModel.formulario2.value.monto)

        viewModel.actualizarMonto("50.5")
        assertEquals("5000", viewModel.formulario2.value.monto)

        viewModel.actualizarMonto("50a")
        assertEquals("5000", viewModel.formulario2.value.monto)

        viewModel.actualizarMonto("-10")
        assertEquals("5000", viewModel.formulario2.value.monto)

        viewModel.actualizarMonto("")
        assertEquals("", viewModel.formulario2.value.monto)
    }

    @Test
    fun `seleccionar anonimo limpia nombre foto y camara`() {
        viewModel.actualizarNombre("Felipe")
        viewModel.actualizarFotoUri("content://foto")
        viewModel.actualizarAceptaCamara(true)

        viewModel.actualizarEsAnonimo(true)

        val formulario = viewModel.formulario2.value
        assertEquals(true, formulario.esAnonimo)
        assertEquals("", formulario.nombre)
        assertNull(formulario.fotoUri)
        assertFalse(formulario.aceptaCamara)
    }

    @Test
    fun `seleccionar no anonimo conserva los datos existentes`() {
        viewModel.actualizarNombre("Felipe")
        viewModel.actualizarFotoUri("content://foto")
        viewModel.actualizarAceptaCamara(true)

        viewModel.actualizarEsAnonimo(false)

        val formulario = viewModel.formulario2.value
        assertEquals(false, formulario.esAnonimo)
        assertEquals("Felipe", formulario.nombre)
        assertEquals("content://foto", formulario.fotoUri)
        assertTrue(formulario.aceptaCamara)
    }

    @Test
    fun `formulario anonimo valido no exige nombre`() {
        viewModel.actualizarMonto("5000")
        viewModel.actualizarFecha(1_700_000_000_000L)
        viewModel.actualizarEsAnonimo(true)

        val valido = viewModel.validarFormulario(esAnonimo = true)

        assertTrue(valido)
        assertNull(viewModel.errores2.value.monto)
        assertNull(viewModel.errores2.value.nombre)
        assertNull(viewModel.errores2.value.fecha)
    }

    @Test
    fun `formulario no anonimo exige un nombre valido`() {
        viewModel.actualizarMonto("5000")
        viewModel.actualizarFecha(1_700_000_000_000L)
        viewModel.actualizarEsAnonimo(false)
        viewModel.actualizarNombre("")

        assertFalse(viewModel.validarFormulario(esAnonimo = false))
        assertEquals(
            "El nombre es obligatorio",
            viewModel.errores2.value.nombre
        )

        viewModel.actualizarNombre("Fe")

        assertFalse(viewModel.validarFormulario(esAnonimo = false))
        assertEquals(
            "El nombre debe tener al menos 3 caracteres",
            viewModel.errores2.value.nombre
        )
    }

    @Test
    fun `formulario rechaza monto menor a mil y fecha nula`() {
        viewModel.actualizarMonto("999")
        viewModel.actualizarFecha(null)
        viewModel.actualizarEsAnonimo(true)

        assertFalse(viewModel.validarFormulario(esAnonimo = true))
        assertEquals(
            "El valor debe ser superior a 1000",
            viewModel.errores2.value.monto
        )
        assertEquals(
            "Debe seleccionar una fecha",
            viewModel.errores2.value.fecha
        )
    }

    @Test
    fun `preparar donacion devuelve null si no se eligio anonimato`() {
        viewModel.actualizarMonto("5000")
        viewModel.actualizarFecha(1_700_000_000_000L)

        assertNull(viewModel.prepararDonacion())
        assertNull(viewModel.donacionPendiente.value)
    }

    @Test
    fun `preparar donacion anonima usa nombre Anonimo y sin foto`() {
        viewModel.actualizarMonto("5000")
        viewModel.actualizarFecha(1_700_000_000_000L)
        viewModel.actualizarEsAnonimo(true)

        val pendiente = viewModel.prepararDonacion()

        assertNotNull(pendiente)
        assertEquals("Anonimo", pendiente!!.nombre)
        assertNull(pendiente.fotoUri)
        assertTrue(pendiente.esAnonimo)
        assertEquals(5000, pendiente.monto)
        assertEquals(1_700_000_000_000L, pendiente.fecha)
        assertTrue(pendiente.webpayOrden.startsWith("DON-"))
        assertEquals(pendiente, viewModel.donacionPendiente.value)
    }

    @Test
    fun `preparar donacion no anonima recorta el nombre y conserva la foto`() {
        viewModel.actualizarMonto("7000")
        viewModel.actualizarFecha(1_700_000_000_000L)
        viewModel.actualizarEsAnonimo(false)
        viewModel.actualizarNombre("  Felipe  ")
        viewModel.actualizarFotoUri("content://foto")

        val pendiente = viewModel.prepararDonacion()

        assertNotNull(pendiente)
        assertEquals("Felipe", pendiente!!.nombre)
        assertEquals("content://foto", pendiente.fotoUri)
        assertFalse(pendiente.esAnonimo)
        assertEquals(7000, pendiente.monto)
    }

    @Test
    fun `crear request de colaboracion conserva todos los campos y la orden`() {
        viewModel.actualizarMonto("3000")
        viewModel.actualizarFecha(1_700_000_000_000L)
        viewModel.actualizarEsAnonimo(false)
        viewModel.actualizarNombre("Matias")
        viewModel.actualizarFotoUri("content://foto")

        val pendiente = viewModel.prepararDonacion()!!
        val request = viewModel.crearRequestColaboracion(pendiente)

        assertEquals("Matias", request.nombre)
        assertEquals("content://foto", request.fotoUri)
        assertEquals(3000, request.montoDonacion)
        assertEquals(false, request.esAnonimo)
        assertEquals(pendiente.webpayOrden, request.webpayOrden)
    }

    @Test
    fun `cancelar donacion pendiente la elimina`() {
        viewModel.actualizarMonto("5000")
        viewModel.actualizarFecha(1_700_000_000_000L)
        viewModel.actualizarEsAnonimo(true)
        viewModel.prepararDonacion()

        assertNotNull(viewModel.donacionPendiente.value)

        viewModel.cancelarDonacionPendiente()

        assertNull(viewModel.donacionPendiente.value)
    }

    @Test
    fun `limpiar sesion borra donacion pendiente y resumen`() {
        viewModel.actualizarMonto("5000")
        viewModel.actualizarFecha(1_700_000_000_000L)
        viewModel.actualizarEsAnonimo(true)
        viewModel.prepararDonacion()

        viewModel.limpiarSesion()

        assertNull(viewModel.donacionPendiente.value)
        assertTrue(viewModel.misColaboraciones.value.isEmpty())
        assertEquals(0, viewModel.resumenDonaciones.value.total)
        assertEquals(0, viewModel.resumenDonaciones.value.cantidad)
        assertFalse(viewModel.cargandoMis.value)
    }

    @Test
    fun `cargar mis colaboraciones con token vacio limpia la sesion`() {
        viewModel.actualizarMonto("5000")
        viewModel.actualizarFecha(1_700_000_000_000L)
        viewModel.actualizarEsAnonimo(true)
        viewModel.prepararDonacion()

        viewModel.cargarMisColaboraciones("")

        assertNull(viewModel.donacionPendiente.value)
        assertTrue(viewModel.misColaboraciones.value.isEmpty())
    }
}
