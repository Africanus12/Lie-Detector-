package com.liedetector.app.domain

import com.liedetector.app.data.model.AnalysisResult
import com.liedetector.app.data.model.ConversationAnalysis
import com.liedetector.app.data.model.HonestyProfile
import com.liedetector.app.data.model.RiskLevel
import com.liedetector.app.data.model.SuspiciousMessage
import kotlin.math.max
import kotlin.math.min

object DeceptionAnalyzer {

    private val vagueWords = listOf(
        "busy", "stuff", "things", "maybe", "probably", "idk", "dunno",
        "kind of", "sort of", "i guess", "whatever", "sometime", "later",
        "soon", "eventually", "around", "about", "something"
    )

    private val excusePatterns = listOf(
        "i was" to -15,
        "i couldn't" to -12,
        "i didn't" to -10,
        "my phone" to -18,
        "fell asleep" to -20,
        "was sleeping" to -18,
        "was busy" to -15,
        "had to" to -8,
        "forgot" to -12,
        "didn't see" to -16,
        "didn't get" to -14,
        "no signal" to -20,
        "phone died" to -22,
        "battery died" to -22,
        "was in a meeting" to -10,
        "traffic" to -8,
        "got stuck" to -12,
        "on my way" to -18,
        "almost there" to -20,
        "5 minutes" to -15,
        "just woke up" to -12
    )

    private val promisePatterns = listOf(
        "i promise" to -10,
        "i swear" to -15,
        "trust me" to -20,
        "believe me" to -18,
        "honestly" to -12,
        "to be honest" to -14,
        "not gonna lie" to -16,
        "for real" to -8,
        "no cap" to -10,
        "deadass" to -8,
        "i'll pay" to -15,
        "i'll send" to -12,
        "tomorrow" to -10,
        "next week" to -12,
        "i will" to -5
    )

    private val emotionalDistancing = listOf(
        "just", "only", "merely", "simply", "not really",
        "it's fine", "whatever", "don't worry", "no big deal",
        "it doesn't matter", "i don't care"
    )

    private val deflectionPatterns = listOf(
        "why do you" to -10,
        "you always" to -12,
        "you never" to -12,
        "that's not" to -8,
        "i never said" to -15,
        "you're overthinking" to -18,
        "you're being" to -14,
        "calm down" to -16,
        "relax" to -10,
        "it's not what" to -18,
        "you're crazy" to -20,
        "you're imagining" to -22
    )

    fun analyze(message: String): AnalysisResult {
        val lower = message.lowercase().trim()
        var score = 65 // baseline

        // Length analysis — very short messages are more suspicious
        when {
            lower.length < 15 -> score -= 10
            lower.length < 30 -> score -= 5
            lower.length > 100 -> score += 5
        }

        // Vague wording
        val vagueCount = vagueWords.count { lower.contains(it) }
        score -= vagueCount * 6

        // Excuse patterns
        for ((pattern, penalty) in excusePatterns) {
            if (lower.contains(pattern)) score += penalty
        }

        // Promise/emphasis patterns (over-assertion = suspicious)
        for ((pattern, penalty) in promisePatterns) {
            if (lower.contains(pattern)) score += penalty
        }

        // Emotional distancing
        val distanceCount = emotionalDistancing.count { lower.contains(it) }
        score -= distanceCount * 5

        // Deflection
        for ((pattern, penalty) in deflectionPatterns) {
            if (lower.contains(pattern)) score += penalty
        }

        // Lack of specifics — no numbers, times, names
        val hasSpecifics = lower.contains(Regex("\\d+:\\d+|monday|tuesday|wednesday|thursday|friday|saturday|sunday|\\d{1,2}(am|pm)"))
        if (!hasSpecifics) score -= 5

        // Exclamation marks (over-emphasis)
        val exclamationCount = message.count { it == '!' }
        if (exclamationCount > 2) score -= exclamationCount * 3

        // Ellipsis (hesitation)
        if (lower.contains("...") || lower.contains("..")) score -= 8

        // Clamp
        score = max(5, min(95, score))

        val interpretations = buildInterpretations(lower, score)
        val signals = buildSignals(lower)
        val hiddenMeaning = generateHiddenMeaning(lower, score)
        val confidence = calculateConfidence(lower, signals.size)
        val riskLevel = when {
            score >= 70 -> RiskLevel.TRUTHFUL
            score >= 50 -> RiskLevel.UNCERTAIN
            score >= 30 -> RiskLevel.SUSPICIOUS
            else -> RiskLevel.DECEPTIVE
        }

        return AnalysisResult(
            truthScore = score,
            confidence = confidence,
            interpretations = interpretations,
            linguisticSignals = signals,
            hiddenMeaning = hiddenMeaning,
            riskLevel = riskLevel
        )
    }

    fun analyzeConversation(messages: List<String>): ConversationAnalysis {
        val results = messages.map { it to analyze(it) }
        val avgScore = results.map { it.second.truthScore }.average().toInt()
        val suspicious = results
            .filter { it.second.truthScore < 50 }
            .map { SuspiciousMessage(it.first, it.second.truthScore) }
            .sortedBy { it.truthScore }

        val assessment = when {
            avgScore >= 70 -> "This conversation appears generally honest with consistent messaging."
            avgScore >= 50 -> "Mixed signals detected. Some messages show signs of evasion or uncertainty."
            avgScore >= 30 -> "Multiple deception indicators found. Proceed with caution."
            else -> "High deception probability detected across multiple messages."
        }

        return ConversationAnalysis(avgScore, suspicious, assessment)
    }

    fun buildHonestyProfile(personName: String, messages: List<String>): HonestyProfile {
        val results = messages.map { analyze(it) }
        val avgScore = results.map { it.truthScore }.average().toInt()
        val allSignals = results.flatMap { it.linguisticSignals }.distinct()
        val patterns = mutableListOf<String>()

        if (allSignals.any { it.contains("vague") }) patterns.add("Vague communication style")
        if (allSignals.any { it.contains("emotional") }) patterns.add("Emotional distancing")
        if (allSignals.any { it.contains("deflect") }) patterns.add("Deflection behavior")
        if (allSignals.any { it.contains("excuse") }) patterns.add("Frequent excuse-making")
        if (allSignals.any { it.contains("over-assertion") }) patterns.add("Over-assertion of truthfulness")
        if (results.any { it.truthScore < 30 }) patterns.add("Contains highly suspicious messages")
        if (avgScore < 50) patterns.add("Below-average honesty indicators")

        if (patterns.isEmpty()) patterns.add("No significant deception patterns detected")

        return HonestyProfile(personName, avgScore, patterns, messages.size)
    }

    private fun buildInterpretations(text: String, score: Int): List<String> {
        val interps = mutableListOf<String>()

        if (excusePatterns.any { text.contains(it.first) })
            interps.add("Possible excuse or justification")
        if (emotionalDistancing.any { text.contains(it) })
            interps.add("Emotional distancing detected")
        if (promisePatterns.any { text.contains(it.first) })
            interps.add("Over-emphasis on truthfulness")
        if (deflectionPatterns.any { text.contains(it.first) })
            interps.add("Deflection or blame-shifting")
        if (vagueWords.any { text.contains(it) })
            interps.add("Deliberately vague language")
        if (score < 40)
            interps.add("Low engagement intent")
        if (score >= 70)
            interps.add("Consistent and direct communication")

        if (interps.isEmpty()) {
            interps.add("Neutral statement")
            interps.add("Requires more context for definitive assessment")
        }

        return interps.take(4)
    }

    private fun buildSignals(text: String): List<String> {
        val signals = mutableListOf<String>()

        if (vagueWords.any { text.contains(it) }) signals.add("Vague wording detected")
        if (text.length < 20) signals.add("Unusually brief response")
        if (!text.contains(Regex("\\d"))) signals.add("Lack of specific details")
        if (emotionalDistancing.any { text.contains(it) }) signals.add("Emotional distancing language")
        if (promisePatterns.any { text.contains(it.first) }) signals.add("Over-assertion of truth")
        if (excusePatterns.any { text.contains(it.first) }) signals.add("Excuse pattern detected")
        if (deflectionPatterns.any { text.contains(it.first) }) signals.add("Deflection pattern detected")
        if (text.contains("...") || text.contains("..")) signals.add("Hesitation markers")
        if (text.count { it == '!' } > 2) signals.add("Excessive emphasis")

        if (signals.isEmpty()) signals.add("No significant deception markers")

        return signals.take(5)
    }

    private fun generateHiddenMeaning(text: String, score: Int): String {
        val hasExcuse = excusePatterns.any { text.contains(it.first) }
        val hasPromise = promisePatterns.any { text.contains(it.first) }
        val hasDeflection = deflectionPatterns.any { text.contains(it.first) }
        val hasDistancing = emotionalDistancing.any { text.contains(it) }

        return when {
            hasDeflection && score < 40 ->
                "The sender is likely trying to shift focus away from the real issue to avoid accountability."
            hasExcuse && hasPromise ->
                "This combines an excuse with reassurance, suggesting the sender knows their explanation is weak."
            hasExcuse && score < 30 ->
                "The excuse provided is likely fabricated or heavily exaggerated to avoid confrontation."
            hasExcuse ->
                "The sender may be providing a convenient explanation rather than the actual reason."
            hasPromise && score < 40 ->
                "The over-emphasis on being truthful suggests the opposite may be true."
            hasPromise ->
                "The need to assert truthfulness may indicate some level of guilt or awareness of doubt."
            hasDistancing && score < 50 ->
                "The sender is creating emotional distance, possibly to avoid deeper engagement."
            hasDistancing ->
                "There may be underlying feelings being suppressed or minimized."
            score >= 70 ->
                "The message appears straightforward with no significant hidden agenda detected."
            score >= 50 ->
                "Some ambiguity detected, but not enough evidence for a definitive hidden meaning."
            else ->
                "The communication style suggests information is being withheld or modified."
        }
    }

    private fun calculateConfidence(text: String, signalCount: Int): Int {
        var confidence = 50
        if (text.length > 30) confidence += 10
        if (text.length > 80) confidence += 10
        confidence += min(signalCount * 8, 25)
        if (text.split(" ").size > 5) confidence += 5
        return min(95, max(30, confidence))
    }
}
