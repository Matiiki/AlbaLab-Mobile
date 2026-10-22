package com.example.albalab.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colaboradores")
data class ColaboradorEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val fotoUri: String?,

    val esAnonimo: Boolean
)