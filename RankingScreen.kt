package com.liedetector.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liedetector.app.domain.TrendingData
import com.liedetector.app.ui.components.AnalysisCard
import com.liedetector.app.ui.components.SectionHeader
import com.liedetector.app.ui.theme.LieDetectorColors
import com.liedetector.app.ui.theme.MonoFont
import com.liedetector.app.ui.theme.truthColor

@Composable
fun RankingScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topLies = remember { TrendingData.topLiesThisWeek }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LieDetectorColors.background)
    ) {
        // Top bar
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
                    text = "Excuse Lie Ranking",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LieDetectorColors.textPrimary
                )
                Text(
                    text = "TOP LIES THIS WEEK",
                    fontFamily = MonoFont,
                    fontSize = 10.sp,
                    color = LieDetectorColors.textTertiary,
                    letterSpacing = 2.sp
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Trophy header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = LieDetectorColors.warning,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Global Lie Rankings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LieDetectorColors.textPrimary
                    )
                    Text(
                        text = "Most analyzed excuses with lowest truth scores",
                        fontFamily = MonoFont,
                        fontSize = 11.sp,
                        color = LieDetectorColors.textTertiary
                    )
                }
            }

            itemsIndexed(topLies) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Rank number
                    Text(
                        text = "#${index + 1}",
                        fontFamily = MonoFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (index) {
                            0 -> LieDetectorColors.danger
                            1 -> LieDetectorColors.warning
                            2 -> LieDetectorColors.primary
                            else -> LieDetectorColors.textTertiary
                        },
                        modifier = Modifier.width(36.dp)
                    )

                    AnalysisCard(
                        message = item.message,
                        truthScore = item.truthScore,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
