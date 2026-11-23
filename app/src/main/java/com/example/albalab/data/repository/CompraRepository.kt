package com.example.albalab.data.repository

import com.example.albalab.data.local.AlbaLabDao
import com.example.albalab.data.local.CompraEntity
import com.example.albalab.model.ProductoModel
import kotlinx.coroutines.flow.Flow

class CompraRepository(
    private val dao: AlbaLabDao
) {
    fun obtenerCompras(email: String): Flow<List<CompraEntity>> =
        dao.obtenerComprasPorUsuario(email)

    suspend fun guardarCompra(
        email: String,
        productos: List<ProductoModel>
    ) {
        if (email.isBlank() || productos.isEmpty()) return

        val total = productos.sumOf { it.precio }

        val detalle = productos
            .groupBy { it.nombre }
            .map { (nombre, items) -> "$nombre x${items.size}" }
            .joinToString(", ")

        dao.insertarCompra(
            CompraEntity(
                usuarioEmail = email,
                total = total,
                productos = detalle
            )
        )
    }
}
