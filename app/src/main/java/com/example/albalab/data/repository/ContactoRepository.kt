package com.example.albalab.data.repository

import com.example.albalab.data.local.AlbaLabDao
import com.example.albalab.data.local.ContactoEntity

class ContactoRepository(
    private val dao: AlbaLabDao
) {

    val contactos =
        dao.obtenerContactos()

    suspend fun guardarContacto(
        nombre: String,
        motivo: String,
        cantidadParticipantes: Int,
        aceptaTerminos: Boolean,
        solicitaSeguimiento: Boolean,
        fecha: Long,
        lugar: String
    ) {

        val contacto =
            ContactoEntity(
                nombre = nombre,
                motivo = motivo,
                cantidadParticipantes = cantidadParticipantes,
                aceptaTerminos = aceptaTerminos,
                solicitaSeguimiento = solicitaSeguimiento,
                fecha = fecha,
                lugar = lugar
            )

        dao.insertarContacto(contacto)
    }
}