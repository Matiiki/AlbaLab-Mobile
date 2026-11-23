package com.example.albalab.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compras")
data class CompraEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val usuarioEmail: String,

    val fecha: Long = System.currentTimeMillis(),

    val total: Int = 0,

    val productos: String = ""
)
