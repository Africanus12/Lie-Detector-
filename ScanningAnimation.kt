package com.liedetector.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liedetector.app.ui.theme.LieDetectorColors
import com.liedetector.app.ui.theme.MonoFont
import kotlinx.coroutines.delay

@Composable
fun ScanningAnimation(
    isScanning: Boolean,
    onScanComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scanSteps = listOf(
        "Scanning linguistic signals...",
        "Detecting emotional inconsistencies...",
        "Evaluating context ambiguity...",
        "Calculating deception probability..."
    )

    var currentStep by remember { mutableIntStateOf(0) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isScanning) {
        if (isScanning) {
            currentStep = 0
            progress.snapTo(0f)
            for (i in scanSteps.indices) {
                currentStep = i
                progress.animateTo(
                    targetValue = (i + 1).toFloat() / scanSteps.size,
                    animationSpec = tween(600, easing = LinearEasing)
                )
                delay(150)
            }
            delay(300)
            onScanComplete()
        }
    }

    AnimatedVisibility(
        visible = isScanning,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            LieDetectorColors.surface,
                            LieDetectorColors.background
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pulsing dot
            val pulseAlpha by rememberInfiniteTransition(label = "pulse").animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAlpha"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LieDetectorColors.primary.copy(alpha = pulseAlpha))
                )
                Text(
                    text = "ANALYZING",
                    fontFamily = MonoFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LieDetectorColors.primary,
                    letterSpacing = 4.sp
                )
            }

            LinearProgressIndicator(
                progress = progress.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = LieDetectorColors.primary,
                trackColor = LieDetectorColors.surfaceVariant,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                scanSteps.forEachIndexed { index, step ->
                    val isActive = index <= currentStep
                    Text(
                        text = if (isActive) step else "",
                        fontFamily = MonoFont,
                        fontSize = 12.sp,
                        color = if (index == currentStep)
                            LieDetectorColors.primary
                        else
                            LieDetectorColors.textTertiary
                    )
                }
            }
        }
    }
}
