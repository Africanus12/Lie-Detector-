package com.liedetector.app.data.repository

import com.liedetector.app.data.database.AnalysisDao
import com.liedetector.app.data.database.AnalysisEntity
import com.liedetector.app.data.model.AnalysisResult
import com.liedetector.app.data.model.RiskLevel
import kotlinx.coroutines.flow.Flow

class AnalysisRepository(private val dao: AnalysisDao) {

    fun getRecentAnalyses(): Flow<List<AnalysisEntity>> = dao.getRecentAnalyses()

    fun getAllAnalyses(): Flow<List<AnalysisEntity>> = dao.getAllAnalyses()

    suspend fun saveAnalysis(
        message: String,
        result: AnalysisResult,
        isScreenshot: Boolean = false
    ): Long {
        val entity = AnalysisEntity(
            message = message,
            truthScore = result.truthScore,
            confidence = result.confidence,
            interpretations = result.interpretations,
            linguisticSignals = result.linguisticSignals,
            hiddenMeaning = result.hiddenMeaning,
            riskLevel = result.riskLevel.name,
            isScreenshot = isScreenshot
        )
        return dao.insert(entity)
    }

    suspend fun getAnalysisById(id: Long): AnalysisEntity? = dao.getById(id)

    suspend fun getTodayCount(): Int {
        val startOfDay = System.currentTimeMillis() - (System.currentTimeMillis() % 86400000)
        return dao.getAnalysisCountSince(startOfDay)
    }
}
