package com.liedetector.app.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liedetector.app.data.model.AnalysisResult
import com.liedetector.app.domain.DeceptionAnalyzer
import com.liedetector.app.ui.components.*
import com.liedetector.app.ui.theme.LieDetectorColors
import com.liedetector.app.ui.theme.MonoFont
import com.liedetector.app.ui.theme.truthColor

@Composable
fun AnalysisScreen(
    message: String,
    onBack: () -> Unit,
    onNewAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isScanning by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf<AnalysisResult?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LieDetectorColors.background)
    ) {
        if (isScanning) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ScanningAnimation(
                    isScanning = true,
                    onScanComplete = {
                        result = DeceptionAnalyzer.analyze(message)
                        isScanning = false
                    },
                    modifier = Modifier.padding(32.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = !isScanning && result != null,
            enter = fadeIn() + slideInVertically { it / 4 }
        ) {
            result?.let { analysisResult ->
                ResultsContent(
                    message = message,
                    result = analysisResult,
                    onBack = onBack,
                    onNewAnalysis = onNewAnalysis
                )
            }
        }
    }
}

@Composable
private fun ResultsContent(
    message: String,
    result: AnalysisResult,
    onBack: () -> Unit,
    onNewAnalysis: () -> Unit
) {
    val context = LocalContext.current
    val scoreColor = truthColor(result.truthScore)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = LieDetectorColors.textPrimary
                    )
                }
                Text(
                    text = "ANALYSIS REPORT",
                    fontFamily = MonoFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = LieDetectorColors.primary,
                    letterSpacing = 3.sp
                )
                IconButton(onClick = { shareResult(context, message, result) }) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        tint = LieDetectorColors.primary
                    )
                }
            }
        }

        // Analyzed Message
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LieDetectorColors.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "MESSAGE ANALYZED",
                    fontFamily = MonoFont,
                    fontSize = 10.sp,
                    color = LieDetectorColors.textTertiary,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"$message\"",
                    fontSize = 15.sp,
                    color = LieDetectorColors.textPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Truth Gauge
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TRUTH PROBABILITY",
                    fontFamily = MonoFont,
                    fontSize = 11.sp,
                    color = LieDetectorColors.textTertiary,
                    letterSpacing = 3.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                TruthGauge(score = result.truthScore, size = 200)
                Spacer(modifier = Modifier.height(12.dp))

                // Risk Level Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = scoreColor.copy(alpha = 0.15f),
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = result.riskLevel.label.uppercase(),
                        fontFamily = MonoFont,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Interpretations
        item {
            Spacer(modifier = Modifier.height(24.dp))
            InfoCard(
                title = "POSSIBLE INTERPRETATIONS",
                items = result.interpretations,
                modifier = Modifier.padding(horizontal = 20.dp),
                accentColor = LieDetectorColors.warning
            )
        }

        // Linguistic Signals
        item {
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(
                title = "DETECTED LINGUISTIC SIGNALS",
                items = result.linguisticSignals,
                modifier = Modifier.padding(horizontal = 20.dp),
                accentColor = LieDetectorColors.danger
            )
        }

        // Hidden Meaning
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LieDetectorColors.surface)
                    .border(
                        1.dp,
                        LieDetectorColors.primary.copy(alpha = 0.2f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "LIKELY HIDDEN MEANING",
                    fontFamily = MonoFont,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LieDetectorColors.primary,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "\"${result.hiddenMeaning}\"",
                    fontSize = 14.sp,
                    color = LieDetectorColors.textSecondary,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )
            }
        }

        // Confidence
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LieDetectorColors.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ANALYSIS CONFIDENCE",
                        fontFamily = MonoFont,
                        fontSize = 11.sp,
                        color = LieDetectorColors.textTertiary,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = result.confidence / 100f,
                        modifier = Modifier
                            .width(180.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = LieDetectorColors.primary,
                        trackColor = LieDetectorColors.surfaceVariant,
                    )
                }
                Text(
                    text = "${result.confidence}%",
                    fontFamily = MonoFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LieDetectorColors.primary
                )
            }
        }

        // Actions
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PrimaryButton(
                    text = "Share Result",
                    onClick = { shareResult(context, message, result) }
                )
                SecondaryButton(
                    text = "Analyze Another Message",
                    onClick = onNewAnalysis
                )
            }
        }

        // Viral footer
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Analyzed with Internet Lie Detector",
                fontFamily = MonoFont,
                fontSize = 10.sp,
                color = LieDetectorColors.textTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun shareResult(context: Context, message: String, result: AnalysisResult) {
    val shareText = buildString {
        appendLine("Internet Lie Detector Result")
        appendLine()
        appendLine("Message analyzed:")
        appendLine("\"$message\"")
        appendLine()
        appendLine("Truth Probability: ${result.truthScore}%")
        appendLine("Risk Level: ${result.riskLevel.label}")
        appendLine()
        appendLine("What do you think?")
        appendLine()
        appendLine("Analyzed with Internet Lie Detector")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Share Analysis"))
}
