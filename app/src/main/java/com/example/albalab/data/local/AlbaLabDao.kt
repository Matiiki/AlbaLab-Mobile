package com.example.albalab.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AlbaLabDao {

    // -------------------------
    // COLABORADORES
    // -------------------------

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertarColaborador(
        colaborador: ColaboradorEntity
    ): Long

    @Query(
        "SELECT * FROM colaboradores ORDER BY id DESC"
    )
    fun obtenerColaboradores(): Flow<List<ColaboradorEntity>>


    // -------------------------
    // DONACIONES
    // -------------------------

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertarDonacion(
        donacion: DonacionEntity
    )

    @Query(
        "SELECT * FROM donaciones ORDER BY fecha DESC"
    )
    fun obtenerDonaciones(): Flow<List<DonacionEntity>>


    // -------------------------
    // BORRAR DATOS
    // -------------------------

    @Query("DELETE FROM colaboradores")
    suspend fun borrarColaboradores()

    @Query("DELETE FROM donaciones")
    suspend fun borrarDonaciones()

    @Query(
        """
    SELECT 
        c.*,
        d.monto AS monto,
        d.fecha AS fecha
    FROM colaboradores c
    INNER JOIN donaciones d
        ON c.id = d.colaboradorId
    ORDER BY d.fecha DESC
    """
    )
    fun obtenerColaboradoresConDonaciones():
            Flow<List<ColaboradorConDonacion>>
}