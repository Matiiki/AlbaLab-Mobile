package com.example.albalab.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "donaciones",
    foreignKeys = [
        ForeignKey(
            entity = ColaboradorEntity::class,
            parentColumns = ["id"],
            childColumns = ["colaboradorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["colaboradorId"])
    ]
)
data class DonacionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val colaboradorId: Int,

    val monto: Int,

    val fecha: Long
)