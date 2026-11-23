package com.example.albalab.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class ApiErrorsTest {

    private fun responseWithBody(code: Int, json: String): Response<Any> {
        val body = json.toResponseBody("application/json".toMediaType())
        return Response.error(code, body)
    }

    @Test
    fun `usa detail cuando viene en ProblemDetail`() {
        val response = responseWithBody(
            400,
            """{"title":"Bad Request","detail":"El monto es obligatorio"}"""
        )

        assertEquals(
            "El monto es obligatorio",
            response.mensajeDeError("mensaje por defecto")
        )
    }

    @Test
    fun `usa message si detail esta vacio`() {
        val response = responseWithBody(
            409,
            """{"detail":"","message":"Ya existe una colaboracion con esa orden"}"""
        )

        assertEquals(
            "Ya existe una colaboracion con esa orden",
            response.mensajeDeError("mensaje por defecto")
        )
    }

    @Test
    fun `junta la lista de errores de validacion`() {
        val response = responseWithBody(
            400,
            """{"errores":["El nombre es obligatorio","El monto debe ser positivo"]}"""
        )

        assertEquals(
            "El nombre es obligatorio\nEl monto debe ser positivo",
            response.mensajeDeError("mensaje por defecto")
        )
    }

    @Test
    fun `usa el mensaje por defecto si no hay body`() {
        val response = Response.success<Any>(Unit)

        assertEquals(
            "mensaje por defecto",
            response.mensajeDeError("mensaje por defecto")
        )
    }

    @Test
    fun `usa el mensaje por defecto si el body no es JSON valido`() {
        val response = responseWithBody(500, "esto no es json")

        assertEquals(
            "mensaje por defecto",
            response.mensajeDeError("mensaje por defecto")
        )
    }

    @Test
    fun `usa el mensaje por defecto si no viene ningun campo util`() {
        val response = responseWithBody(
            400,
            """{"title":"Bad Request"}"""
        )

        assertEquals(
            "mensaje por defecto",
            response.mensajeDeError("mensaje por defecto")
        )
    }
}
