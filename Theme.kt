package com.liedetector.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val DarkScheme = darkColorScheme(
    primary = LieDetectorColors.primary,
    onPrimary = LieDetectorColors.background,
    surface = LieDetectorColors.surface,
    onSurface = LieDetectorColors.textPrimary,
    background = LieDetectorColors.background,
    onBackground = LieDetectorColors.textPrimary,
    surfaceVariant = LieDetectorColors.surfaceVariant,
    error = LieDetectorColors.danger,
)

val LocalLieDetectorColors = staticCompositionLocalOf { LieDetectorColors }

object LieDetectorTheme {
    val colors: LieDetectorColors
        @Composable get() = LocalLieDetectorColors.current
}

@Composable
fun LieDetectorTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLieDetectorColors provides LieDetectorColors) {
        MaterialTheme(
            colorScheme = DarkScheme,
            typography = LieDetectorTypography,
            content = content
        )
    }
}
