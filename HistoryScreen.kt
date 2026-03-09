package com.liedetector.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liedetector.app.data.database.AnalysisEntity
import com.liedetector.app.ui.components.AnalysisCard
import com.liedetector.app.ui.components.SectionHeader
import com.liedetector.app.ui.theme.LieDetectorColors
import com.liedetector.app.ui.theme.MonoFont
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun HistoryScreen(
    analyses: List<AnalysisEntity>,
    onBack: () -> Unit,
    onAnalysisClick: (AnalysisEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LieDetectorColors.background)
    ) {
        // Top Bar
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
                    text = "Analysis History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LieDetectorColors.textPrimary
                )
                Text(
                    text = "${analyses.size} analyses",
                    fontFamily = MonoFont,
                    fontSize = 11.sp,
                    color = LieDetectorColors.textTertiary
                )
            }
        }

        if (analyses.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        tint = LieDetectorColors.textTertiary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No analyses yet",
                        fontSize = 16.sp,
                        color = LieDetectorColors.textTertiary
                    )
                    Text(
                        text = "Start by analyzing a message",
                        fontFamily = MonoFont,
                        fontSize = 12.sp,
                        color = LieDetectorColors.textTertiary
                    )
                }
            }
        } else {
            val grouped = analyses.groupBy { getRelativeDate(it.timestamp) }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                grouped.forEach { (date, items) ->
                    item {
                        SectionHeader(title = date)
                    }
                    items(items) { entity ->
                        AnalysisCard(
                            message = entity.message,
                            truthScore = entity.truthScore,
                            timestamp = formatTime(entity.timestamp),
                            onClick = { onAnalysisClick(entity) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getRelativeDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        days == 0L -> "Today"
        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}

private fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
}
