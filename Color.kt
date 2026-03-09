package com.liedetector.app.ui.theme

import androidx.compose.ui.graphics.Color

object LieDetectorColors {
    val background = Color(0xFF0F172A)
    val surface = Color(0xFF1E293B)
    val surfaceVariant = Color(0xFF334155)
    val primary = Color(0xFF22C55E)
    val primaryDim = Color(0xFF16A34A)
    val warning = Color(0xFFF59E0B)
    val danger = Color(0xFFEF4444)
    val textPrimary = Color(0xFFFFFFFF)
    val textSecondary = Color(0xFF94A3B8)
    val textTertiary = Color(0xFF64748B)
    val border = Color(0xFF334155)
    val cardBackground = Color(0xFF1E293B)
    val scanLine = Color(0xFF22C55E)
    val shimmer = Color(0xFF475569)
}

fun truthColor(score: Int): Color = when {
    score >= 70 -> LieDetectorColors.primary
    score >= 40 -> LieDetectorColors.warning
    else -> LieDetectorColors.danger
}
