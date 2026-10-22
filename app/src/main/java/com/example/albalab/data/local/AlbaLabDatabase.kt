package com.example.albalab.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ColaboradorEntity::class,
        DonacionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AlbaLabDatabase : RoomDatabase() {

    abstract fun albaLabDao(): AlbaLabDao

    companion object {

        @Volatile
        private var INSTANCE: AlbaLabDatabase? = null

        fun obtenerDatabase(context: Context): AlbaLabDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlbaLabDatabase::class.java,
                    "albalab_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}