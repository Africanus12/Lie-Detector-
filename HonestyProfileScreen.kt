package com.liedetector.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liedetector.app.data.model.HonestyProfile
import com.liedetector.app.domain.DeceptionAnalyzer
import com.liedetector.app.ui.components.*
import com.liedetector.app.ui.theme.LieDetectorColors
import com.liedetector.app.ui.theme.MonoFont
import com.liedetector.app.ui.theme.truthColor

@Composable
fun HonestyProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var personName by remember { mutableStateOf("") }
    var messagesText by remember { mutableStateOf("") }
    var profile by remember { mutableStateOf<HonestyProfile?>(null) }

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
                        text = "Honesty Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LieDetectorColors.textPrimary
                    )
                    Text(
                        text = "RELATIONSHIP ANALYZER",
                        fontFamily = MonoFont,
                        fontSize = 10.sp,
                        color = LieDetectorColors.textTertiary,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        // Input
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Person's Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = LieDetectorColors.textSecondary
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LieDetectorColors.surface)
                        .padding(16.dp)
                ) {
                    if (personName.isEmpty()) {
                        Text(
                            text = "Enter name...",
                            fontSize = 14.sp,
                            color = LieDetectorColors.textTertiary
                        )
                    }
                    BasicTextField(
                        value = personName,
                        onValueChange = { personName = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = LieDetectorColors.textPrimary
                        ),
                        cursorBrush = SolidColor(LieDetectorColors.primary),
                        singleLine = true
                    )
                }

                Text(
                    text = "Paste their messages (one per line)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = LieDetectorColors.textSecondary
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LieDetectorColors.surface)
                        .padding(16.dp)
                ) {
                    if (messagesText.isEmpty()) {
                        Text(
                            text = "Paste multiple messages here...\nEach line = one message\n\nExample:\nI was busy\nI'll call you later\nSorry I forgot",
                            fontSize = 14.sp,
                            color = LieDetectorColors.textTertiary,
                            lineHeight = 22.sp
                        )
                    }
                    BasicTextField(
                        value = messagesText,
                        onValueChange = { messagesText = it },
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
                    text = "Generate Honesty Profile",
                    onClick = {
                        if (personName.isNotBlank() && messagesText.isNotBlank()) {
                            val messages = messagesText.split("\n")
                                .filter { it.trim().isNotEmpty() }
                            profile = DeceptionAnalyzer.buildHonestyProfile(personName, messages)
                        }
                    },
                    enabled = personName.isNotBlank() && messagesText.isNotBlank()
                )
            }
        }

        // Profile Result
        profile?.let { p ->
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LieDetectorColors.surface)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "HONESTY PROFILE",
                        fontFamily = MonoFont,
                        fontSize = 11.sp,
                        color = LieDetectorColors.primary,
                        letterSpacing = 3.sp
                    )

                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = LieDetectorColors.textTertiary,
                        modifier = Modifier.size(48.dp)
                    )

                    Text(
                        text = p.personName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LieDetectorColors.textPrimary
                    )

                    TruthGauge(score = p.truthfulnessScore, size = 160)

                    Text(
                        text = "Based on ${p.messageCount} messages",
                        fontFamily = MonoFont,
                        fontSize = 11.sp,
                        color = LieDetectorColors.textTertiary
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                InfoCard(
                    title = "DETECTED BEHAVIORAL PATTERNS",
                    items = p.patterns,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    accentColor = LieDetectorColors.warning
                )
            }
        }
    }
}
