package com.liedetector.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liedetector.app.ui.theme.LieDetectorColors
import com.liedetector.app.ui.theme.MonoFont
import com.liedetector.app.ui.theme.truthColor

@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = LieDetectorColors.primary,
            modifier = Modifier.size(32.dp)
        )
        Column {
            Text(
                text = "Internet Lie Detector",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LieDetectorColors.textPrimary
            )
            Text(
                text = "AI Communication Analyzer",
                fontFamily = MonoFont,
                fontSize = 10.sp,
                color = LieDetectorColors.textTertiary,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun AnalysisCard(
    message: String,
    truthScore: Int,
    timestamp: String = "",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scoreColor = truthColor(truthScore)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LieDetectorColors.surface)
            .border(1.dp, LieDetectorColors.border.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "\"$message\"",
                fontSize = 14.sp,
                color = LieDetectorColors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (timestamp.isNotEmpty()) {
                Text(
                    text = timestamp,
                    fontFamily = MonoFont,
                    fontSize = 10.sp,
                    color = LieDetectorColors.textTertiary
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$truthScore%",
                fontFamily = MonoFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
            Text(
                text = "TRUTH",
                fontFamily = MonoFont,
                fontSize = 8.sp,
                color = LieDetectorColors.textTertiary,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = LieDetectorColors.textPrimary
        )
        subtitle?.let {
            Text(
                text = it,
                fontFamily = MonoFont,
                fontSize = 11.sp,
                color = LieDetectorColors.textTertiary
            )
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LieDetectorColors.primary,
            contentColor = LieDetectorColors.background,
            disabledContainerColor = LieDetectorColors.surfaceVariant,
            disabledContentColor = LieDetectorColors.textTertiary
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = LieDetectorColors.primary
        ),
        border = BorderStroke(1.dp, LieDetectorColors.primary)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    items: List<String>,
    modifier: Modifier = Modifier,
    accentColor: androidx.compose.ui.graphics.Color = LieDetectorColors.primary
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LieDetectorColors.surface)
            .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            fontFamily = MonoFont,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            letterSpacing = 2.sp
        )
        items.forEach { item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "\u2022",
                    fontSize = 14.sp,
                    color = accentColor
                )
                Text(
                    text = item,
                    fontSize = 14.sp,
                    color = LieDetectorColors.textSecondary
                )
            }
        }
    }
}
