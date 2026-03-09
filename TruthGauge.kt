package com.liedetector.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liedetector.app.ui.theme.LieDetectorColors
import com.liedetector.app.ui.theme.MonoFont
import com.liedetector.app.ui.theme.truthColor

@Composable
fun TruthGauge(
    score: Int,
    modifier: Modifier = Modifier,
    size: Int = 200
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(score) {
        animatedProgress.animateTo(
            targetValue = score / 100f,
            animationSpec = tween(1500, easing = FastOutSlowInEasing)
        )
    }

    val color = truthColor(score)
    val trackColor = LieDetectorColors.surfaceVariant

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size.dp)
    ) {
        Canvas(modifier = Modifier.size(size.dp)) {
            val strokeWidth = 12.dp.toPx()
            val arcSize = Size(this.size.width - strokeWidth, this.size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

            // Track
            drawArc(
                color = trackColor,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress
            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle = 270f * animatedProgress.value,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(animatedProgress.value * 100).toInt()}%",
                fontFamily = MonoFont,
                fontSize = (size / 4.5).sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = "TRUTH",
                fontFamily = MonoFont,
                fontSize = (size / 16).sp,
                color = LieDetectorColors.textSecondary,
                letterSpacing = 3.sp
            )
        }
    }
}
