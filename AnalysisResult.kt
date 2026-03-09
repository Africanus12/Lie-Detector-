package com.liedetector.app.data.model

data class AnalysisResult(
    val truthScore: Int,
    val confidence: Int,
    val interpretations: List<String>,
    val linguisticSignals: List<String>,
    val hiddenMeaning: String,
    val riskLevel: RiskLevel
)

enum class RiskLevel(val label: String) {
    TRUTHFUL("Likely Truthful"),
    UNCERTAIN("Uncertain"),
    SUSPICIOUS("Suspicious"),
    DECEPTIVE("Likely Deceptive")
}

data class ConversationAnalysis(
    val honestyScore: Int,
    val suspiciousMessages: List<SuspiciousMessage>,
    val overallAssessment: String
)

data class SuspiciousMessage(
    val text: String,
    val truthScore: Int
)

data class HonestyProfile(
    val personName: String,
    val truthfulnessScore: Int,
    val patterns: List<String>,
    val messageCount: Int
)
