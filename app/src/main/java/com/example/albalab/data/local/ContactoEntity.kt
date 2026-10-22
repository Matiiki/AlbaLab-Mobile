package com.example.albalab.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contactos")
data class ContactoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val motivo: String,

    val cantidadParticipantes: Int,

    val aceptaTerminos: Boolean,

    val solicitaSeguimiento: Boolean,

    val fecha: Long,

    val lugar: String
)