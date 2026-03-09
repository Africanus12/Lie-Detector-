package com.liedetector.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AnalysisEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class LieDetectorDatabase : RoomDatabase() {
    abstract fun analysisDao(): AnalysisDao

    companion object {
        @Volatile
        private var INSTANCE: LieDetectorDatabase? = null

        fun getDatabase(context: Context): LieDetectorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LieDetectorDatabase::class.java,
                    "lie_detector_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
