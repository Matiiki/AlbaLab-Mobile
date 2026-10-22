package com.example.albalab.data.repository

import com.example.albalab.data.local.AlbaLabDao
import com.example.albalab.data.local.ColaboradorEntity
import com.example.albalab.data.local.DonacionEntity

class DonacionRepository(
    private val dao: AlbaLabDao
) {

    val colaboradores = dao.obtenerColaboradores()

    val donaciones = dao.obtenerDonaciones()

    val colaboradoresConDonaciones =
        dao.obtenerColaboradoresConDonaciones()

    suspend fun guardarDonacion(
        nombre: String,
        fotoUri: String?,
        esAnonimo: Boolean,
        monto: Int,
        fecha: Long
    ) {

        val colaborador = ColaboradorEntity(
            nombre = if (esAnonimo) "Anónimo" else nombre,
            fotoUri = if (esAnonimo) null else fotoUri,
            esAnonimo = esAnonimo
        )

        val colaboradorId =
            dao.insertarColaborador(colaborador)

        val donacion = DonacionEntity(
            colaboradorId = colaboradorId.toInt(),
            monto = monto,
            fecha = fecha
        )

        dao.insertarDonacion(donacion)
    }
}