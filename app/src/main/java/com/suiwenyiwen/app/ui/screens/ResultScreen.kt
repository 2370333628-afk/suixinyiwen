package com.suiwenyiwen.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suiwenyiwen.app.model.FortuneResult
import com.suiwenyiwen.app.model.PalaceType
import com.suiwenyiwen.app.ui.components.FortuneIndicator
import com.suiwenyiwen.app.ui.components.PalaceCard
import com.suiwenyiwen.app.ui.components.getFortuneColor
import com.suiwenyiwen.app.ui.theme.*

@Composable
fun ResultScreen(
    fortuneResult: FortuneResult,
    onBack: () -> Unit,
    onAskAgain: () -> Unit
) {
    val expandedPalaces = remember { mutableStateMapOf<PalaceType, Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 整体运势
        item {
            FortuneIndicator(
                fortuneLevel = fortuneResult.overallFortune,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // 排盘信息
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "排盘信息",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = fortuneResult.queryTimeDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // 总体解读
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground.copy(alpha = 0.8f)),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    getFortuneColor(fortuneResult.overallFortune).copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "总体解读",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldLight,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = fortuneResult.overallSummary,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }
        }

        // 关键星曜
        if (fortuneResult.keyStars.isNotEmpty()) {
            item {
                Text(
                    text = "关键星曜",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldLight,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    fortuneResult.keyStars.forEach { star ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldPrimary.copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = star,
                                style = MaterialTheme.typography.labelLarge,
                                color = GoldPrimary
                            )
                        }
                    }
                }
            }
        }

        // 十二宫详解
        item {
            Text(
                text = "十二宫详解",
                style = MaterialTheme.typography.titleLarge,
                color = GoldLight,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = "点击各宫查看详细解读",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }

        itemsIndexed(fortuneResult.palaceReadings) { _, reading ->
            PalaceCard(
                reading = reading,
                isExpanded = expandedPalaces[reading.palaceType] ?: false,
                onToggle = {
                    expandedPalaces[reading.palaceType] =
                        !(expandedPalaces[reading.palaceType] ?: false)
                }
            )
        }

        // 有利方向
        if (fortuneResult.favorableDirections.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = JadeGreen.copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, JadeGreen.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "有利方向",
                            style = MaterialTheme.typography.titleMedium,
                            color = JadeLight,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        fortuneResult.favorableDirections.forEach { direction ->
                            Text(
                                text = "• $direction",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // 注意事项
        if (fortuneResult.cautions.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = RedAccent.copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RedAccent.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "注意事项",
                            style = MaterialTheme.typography.titleMedium,
                            color = RedLight,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        fortuneResult.cautions.forEach { caution ->
                            Text(
                                text = "⚠ $caution",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // 综合建议
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = getFortuneColor(fortuneResult.overallFortune).copy(alpha = 0.1f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    getFortuneColor(fortuneResult.overallFortune).copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "综合建议",
                        style = MaterialTheme.typography.titleMedium,
                        color = getFortuneColor(fortuneResult.overallFortune),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = fortuneResult.generalAdvice,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }
        }

        // 底部按钮
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text("返回首页")
                }

                Button(
                    onClick = onAskAgain,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text(
                        "再问一次",
                        color = DarkBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
