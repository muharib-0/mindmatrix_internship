package com.virasat.nammaguide.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Singleton Room Database for Virasat Namma Guide.
 * Version 1 — CheckInEntity only.
 */
@Database(entities = [CheckInEntity::class], version = 1, exportSchema = false)
abstract class VirasatDatabase : RoomDatabase() {

    abstract fun checkInDao(): CheckInDao

    companion object {
        @Volatile
        private var INSTANCE: VirasatDatabase? = null

        fun getInstance(context: Context): VirasatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VirasatDatabase::class.java,
                    "virasat_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
