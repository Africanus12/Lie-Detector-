package com.liedetector.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liedetector.app.data.model.ConversationAnalysis
import com.liedetector.app.domain.DeceptionAnalyzer
import com.liedetector.app.ui.components.*
import com.liedetector.app.ui.theme.LieDetectorColors
import com.liedetector.app.ui.theme.MonoFont
import com.liedetector.app.ui.theme.truthColor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun ScreenshotAnalyzerScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isProcessing by remember { mutableStateOf(false) }
    var extractedText by remember { mutableStateOf<String?>(null) }
    var analysis by remember { mutableStateOf<ConversationAnalysis?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isProcessing = true
            errorMessage = null
            try {
                val image = InputImage.fromFilePath(context, it)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val text = visionText.text
                        if (text.isNotBlank()) {
                            extractedText = text
                            val messages = text.split("\n")
                                .filter { line -> line.trim().length > 3 }
                                .map { line -> line.trim() }
                            analysis = DeceptionAnalyzer.analyzeConversation(messages)
                        } else {
                            errorMessage = "No text could be extracted from this image"
                        }
                        isProcessing = false
                    }
                    .addOnFailureListener {
                        errorMessage = "Failed to process image. Please try another screenshot."
                        isProcessing = false
                    }
            } catch (e: Exception) {
                errorMessage = "Failed to load image"
                isProcessing = false
            }
        }
    }

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
                        text = "Screenshot Analyzer",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LieDetectorColors.textPrimary
                    )
                    Text(
                        text = "CHAT ANALYSIS",
                        fontFamily = MonoFont,
                        fontSize = 10.sp,
                        color = LieDetectorColors.textTertiary,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        // Upload area
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LieDetectorColors.surface)
                    .border(
                        2.dp,
                        LieDetectorColors.border.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = LieDetectorColors.primary,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Upload Chat Screenshot",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LieDetectorColors.textPrimary
                )
                Text(
                    text = "WhatsApp \u2022 SMS \u2022 Telegram \u2022 Instagram \u2022 iMessage",
                    fontFamily = MonoFont,
                    fontSize = 10.sp,
                    color = LieDetectorColors.textTertiary
                )
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = "Select Image",
                    onClick = { imagePicker.launch("image/*") }
                )
            }
        }

        // Processing
        if (isProcessing) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ScanningAnimation(
                    isScanning = true,
                    onScanComplete = {},
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        // Error
        errorMessage?.let { error ->
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    fontSize = 14.sp,
                    color = LieDetectorColors.danger,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        // Results
        analysis?.let { result ->
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CONVERSATION HONESTY SCORE",
                        fontFamily = MonoFont,
                        fontSize = 11.sp,
                        color = LieDetectorColors.textTertiary,
                        letterSpacing = 3.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TruthGauge(score = result.honestyScore, size = 180)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = result.overallAssessment,
                    fontSize = 14.sp,
                    color = LieDetectorColors.textSecondary,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    lineHeight = 22.sp
                )
            }

            if (result.suspiciousMessages.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionHeader(
                        title = "Suspicious Messages",
                        subtitle = "FLAGGED CONTENT"
                    )
                }

                items(result.suspiciousMessages) { msg ->
                    AnalysisCard(
                        message = msg.text,
                        truthScore = msg.truthScore,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
