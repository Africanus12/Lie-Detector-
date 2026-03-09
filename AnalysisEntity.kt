package com.liedetector.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "analyses")
@TypeConverters(StringListConverter::class)
data class AnalysisEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val truthScore: Int,
    val confidence: Int,
    val interpretations: List<String>,
    val linguisticSignals: List<String>,
    val hiddenMeaning: String,
    val riskLevel: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isScreenshot: Boolean = false
)

class StringListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}
