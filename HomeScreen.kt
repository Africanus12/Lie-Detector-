package com.liedetector.app.ui.screens

import androidx.compose.animation.*
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
import com.liedetector.app.domain.TrendingData
import com.liedetector.app.ui.components.*
import com.liedetector.app.ui.theme.LieDetectorColors

@Composable
fun HomeScreen(
    onAnalyze: (String) -> Unit,
    onUploadScreenshot: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToRanking: () -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    val trending = remember { TrendingData.getRandomTrending(5) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LieDetectorColors.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header
        item {
            AppHeader()
        }

        // Input Card
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Text Input
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 140.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LieDetectorColors.surface)
                        .padding(16.dp)
                ) {
                    if (messageText.isEmpty()) {
                        Text(
                            text = "Paste a message to analyze...\n\nExample: \"Sorry I couldn't make it yesterday, I was busy.\"",
                            fontSize = 14.sp,
                            color = LieDetectorColors.textTertiary,
                            lineHeight = 22.sp
                        )
                    }
                    BasicTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = LieDetectorColors.textPrimary,
                            lineHeight = 22.sp
                        ),
                        cursorBrush = SolidColor(LieDetectorColors.primary)
                    )
                }

                // Analyze Button
                PrimaryButton(
                    text = "Analyze Truth",
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onAnalyze(messageText)
                        }
                    },
                    enabled = messageText.isNotBlank()
                )

                // Upload Screenshot Button
                SecondaryButton(
                    text = "Upload Chat Screenshot",
                    onClick = onUploadScreenshot
                )
            }
        }

        // Quick Actions
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(title = "Quick Actions")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionChip(
                    icon = Icons.Default.History,
                    label = "History",
                    onClick = onNavigateToHistory,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    icon = Icons.Default.SportsEsports,
                    label = "Game",
                    onClick = onNavigateToGame,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    onClick = onNavigateToProfile,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    icon = Icons.Default.Leaderboard,
                    label = "Ranking",
                    onClick = onNavigateToRanking,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Trending
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "Most Analyzed Messages Today",
                subtitle = "TRENDING ANALYSES"
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(trending) { item ->
            AnalysisCard(
                message = item.message,
                truthScore = item.truthScore,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
        }

        // Viral CTA
        item {
            Spacer(modifier = Modifier.height(24.dp))
            ViralCTA()
        }
    }
}

@Composable
private fun QuickActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(64.dp),
        shape = RoundedCornerShape(12.dp),
        color = LieDetectorColors.surface,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = LieDetectorColors.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = LieDetectorColors.textSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ViralCTA() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LieDetectorColors.surface)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Think someone is lying to you?",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = LieDetectorColors.textPrimary
        )
        Text(
            text = "Try the Internet Lie Detector",
            fontSize = 13.sp,
            color = LieDetectorColors.primary
        )
    }
}
