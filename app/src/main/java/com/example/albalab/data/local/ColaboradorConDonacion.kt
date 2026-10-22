package com.example.albalab.data.local

import androidx.room.Embedded

data class ColaboradorConDonacion(

    @Embedded
    val colaborador: ColaboradorEntity,

    val monto: Int,

    val fecha: Long
)