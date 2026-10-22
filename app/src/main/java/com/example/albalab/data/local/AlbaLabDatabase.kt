package com.example.albalab.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        ColaboradorEntity::class,
        DonacionEntity::class,
        ContactoEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AlbaLabDatabase :
    RoomDatabase() {

    abstract fun albaLabDao():
            AlbaLabDao

    companion object {

        @Volatile
        private var INSTANCE:
                AlbaLabDatabase? = null

        // De versión 1 a versión 2:
        // crea la tabla contactos con los nombres antiguos.

        private val MIGRATION_1_2 =
            object : Migration(1, 2) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {

                    db.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS contactos (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            nombre TEXT NOT NULL,
                            motivo TEXT NOT NULL,
                            cantidadPersonas INTEGER NOT NULL,
                            aceptaTerminos INTEGER NOT NULL,
                            activo INTEGER NOT NULL,
                            fecha INTEGER NOT NULL,
                            lugar TEXT NOT NULL
                        )
                        """.trimIndent()
                    )
                }
            }

        // De versión 2 a versión 3:
        // cambiamos los nombres para que tengan
        // sentido con Alba Lab.

        private val MIGRATION_2_3 =
            object : Migration(2, 3) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {

                    db.execSQL(
                        """
                        CREATE TABLE contactos_nuevo (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            nombre TEXT NOT NULL,
                            motivo TEXT NOT NULL,
                            cantidadParticipantes INTEGER NOT NULL,
                            aceptaTerminos INTEGER NOT NULL,
                            solicitaSeguimiento INTEGER NOT NULL,
                            fecha INTEGER NOT NULL,
                            lugar TEXT NOT NULL
                        )
                        """.trimIndent()
                    )

                    db.execSQL(
                        """
                        INSERT INTO contactos_nuevo (
                            id,
                            nombre,
                            motivo,
                            cantidadParticipantes,
                            aceptaTerminos,
                            solicitaSeguimiento,
                            fecha,
                            lugar
                        )
                        SELECT
                            id,
                            nombre,
                            motivo,
                            cantidadPersonas,
                            aceptaTerminos,
                            activo,
                            fecha,
                            lugar
                        FROM contactos
                        """.trimIndent()
                    )

                    db.execSQL(
                        "DROP TABLE contactos"
                    )

                    db.execSQL(
                        """
                        ALTER TABLE contactos_nuevo
                        RENAME TO contactos
                        """.trimIndent()
                    )
                }
            }

        fun obtenerDatabase(
            context: Context
        ): AlbaLabDatabase {

            return INSTANCE
                ?: synchronized(this) {

                    val instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            AlbaLabDatabase::class.java,
                            "albalab_database"
                        )
                            .addMigrations(
                                MIGRATION_1_2,
                                MIGRATION_2_3
                            )
                            .build()

                    INSTANCE =
                        instance

                    instance
                }
        }
    }
}