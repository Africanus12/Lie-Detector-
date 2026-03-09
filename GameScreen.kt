package com.liedetector.app.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
fun GameScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var statement by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<AnalysisResult?>(null) }
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LieDetectorColors.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Top bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = LieDetectorColors.textPrimary
                    )
                }
                Column {
                    Text(
                        text = "Lie Detector Game",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LieDetectorColors.textPrimary
                    )
                    Text(
                        text = "CAN THE AI DETECT YOUR LIE?",
                        fontFamily = MonoFont,
                        fontSize = 10.sp,
                        color = LieDetectorColors.textTertiary,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        // Challenge
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LieDetectorColors.surface)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Default.SportsEsports,
                    contentDescription = null,
                    tint = LieDetectorColors.primary,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Can the AI detect your lie?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LieDetectorColors.textPrimary
                )
                Text(
                    text = "Write a statement \u2014 truth or lie.\nThe AI will analyze it.\nShare with friends and let them guess!",
                    fontSize = 13.sp,
                    color = LieDetectorColors.textSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }

        // Input
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LieDetectorColors.surface)
                        .padding(16.dp)
                ) {
                    if (statement.isEmpty()) {
                        Text(
                            text = "Write your statement here...\n\nExample: \"I once met a celebrity at a coffee shop.\"",
                            fontSize = 14.sp,
                            color = LieDetectorColors.textTertiary,
                            lineHeight = 22.sp
                        )
                    }
                    BasicTextField(
                        value = statement,
                        onValueChange = { statement = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = LieDetectorColors.textPrimary,
                            lineHeight = 22.sp
                        ),
                        cursorBrush = SolidColor(LieDetectorColors.primary)
                    )
                }

                PrimaryButton(
                    text = "Let AI Analyze",
                    onClick = {
                        if (statement.isNotBlank()) {
                            isScanning = true
                            result = null
                        }
                    },
                    enabled = statement.isNotBlank() && !isScanning
                )
            }
        }

        // Scanning
        if (isScanning) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ScanningAnimation(
                    isScanning = true,
                    onScanComplete = {
                        result = DeceptionAnalyzer.analyze(statement)
                        isScanning = false
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        // Result
        result?.let { r ->
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "AI VERDICT",
                        fontFamily = MonoFont,
                        fontSize = 11.sp,
                        color = LieDetectorColors.primary,
                        letterSpacing = 3.sp
                    )
                    TruthGauge(score = r.truthScore, size = 180)

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = truthColor(r.truthScore).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (r.truthScore >= 50) "AI thinks this is TRUE" else "AI thinks this is a LIE",
                            fontFamily = MonoFont,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = truthColor(r.truthScore),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PrimaryButton(
                        text = "Share & Challenge Friends",
                        onClick = {
                            val shareText = buildString {
                                appendLine("Lie Detector Game")
                                appendLine()
                                appendLine("I said: \"$statement\"")
                                appendLine()
                                appendLine("The AI gave it a ${r.truthScore}% truth score.")
                                appendLine()
                                appendLine("Was I lying or telling the truth? Can you guess?")
                                appendLine()
                                appendLine("Try it yourself with Internet Lie Detector!")
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Challenge Friends"))
                        }
                    )
                    SecondaryButton(
                        text = "Try Again",
                        onClick = {
                            statement = ""
                            result = null
                        }
                    )
                }
            }
        }
    }
}
