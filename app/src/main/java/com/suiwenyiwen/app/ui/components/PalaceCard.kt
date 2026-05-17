package com.suiwenyiwen.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suiwenyiwen.app.model.PalaceReading
import com.suiwenyiwen.app.ui.theme.*

@Composable
fun PalaceCard(
    reading: PalaceReading,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fortuneColor = getFortuneColor(reading.fortuneLevel)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // 宫位头部
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${reading.palaceType.display}宫",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldLight,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = reading.stemBranch,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
            }

            // 星曜标签
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                reading.majorStars.forEach { star ->
                    StarTag(star, isMajor = true)
                }
                reading.auxiliaryStars.forEach { star ->
                    StarTag(star, isMajor = false)
                }
            }

            // 吉凶指示
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(fortuneColor)
                )
                Text(
                    text = reading.fortuneLevel.display,
                    style = MaterialTheme.typography.labelLarge,
                    color = fortuneColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // 展开详情
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = CardBorder)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = reading.starAnalysis,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = reading.mainAnalysis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "建议：${reading.advice}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = JadeLight,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StarTag(starName: String, isMajor: Boolean) {
    val bgColor = if (isMajor) DarkSurfaceVariant.copy(alpha = 0.6f) else CardBorder.copy(alpha = 0.5f)
    val textColor = if (isMajor) GoldLight else TextSecondary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = starName,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}
