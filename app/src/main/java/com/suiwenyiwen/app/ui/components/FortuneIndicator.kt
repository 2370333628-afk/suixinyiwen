package com.suiwenyiwen.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.suiwenyiwen.app.model.FortuneLevel
import com.suiwenyiwen.app.ui.theme.*

@Composable
fun FortuneIndicator(
    fortuneLevel: FortuneLevel,
    modifier: Modifier = Modifier
) {
    val fortuneColor = getFortuneColor(fortuneLevel)
    val fortuneIcon = getFortuneIcon(fortuneLevel)

    // 脉冲动画
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 八卦圆环
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(160.dp)
        ) {
            // 外环
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 6.dp.toPx()
                drawCircle(
                    brush = Brush.sweepGradient(
                        listOf(fortuneColor.copy(alpha = 0.3f), fortuneColor, fortuneColor.copy(alpha = 0.3f))
                    ),
                    radius = size.minDimension / 2 - strokeWidth / 2,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                // 内环
                drawCircle(
                    color = fortuneColor.copy(alpha = 0.4f),
                    radius = size.minDimension / 2 - strokeWidth * 2.5f,
                    style = Stroke(width = 2.dp.toPx())
                )
                // 十字线
                val cx = size.width / 2
                val cy = size.height / 2
                val r = size.minDimension / 2 - strokeWidth * 3.5f
                drawLine(fortuneColor.copy(alpha = 0.2f), Offset(cx - r, cy), Offset(cx + r, cy))
                drawLine(fortuneColor.copy(alpha = 0.2f), Offset(cx, cy - r), Offset(cx, cy + r))
            }

            // 中心文字
            Text(
                text = fortuneIcon,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                color = fortuneColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        // 吉凶文字
        AnimatedContent(
            targetState = fortuneLevel,
            modifier = Modifier
        ) { fortune ->
            Text(
                text = fortune.display,
                style = MaterialTheme.typography.displayMedium,
                color = fortuneColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun getFortuneColor(level: FortuneLevel): Color = when (level) {
    FortuneLevel.VERY_GOOD -> FortuneVeryGood
    FortuneLevel.GOOD -> FortuneGood
    FortuneLevel.NEUTRAL -> FortuneNeutral
    FortuneLevel.BAD -> FortuneBad
    FortuneLevel.VERY_BAD -> FortuneVeryBad
}

private fun getFortuneIcon(level: FortuneLevel): String = when (level) {
    FortuneLevel.VERY_GOOD -> "☯"
    FortuneLevel.GOOD -> "☰"
    FortuneLevel.NEUTRAL -> "☯"
    FortuneLevel.BAD -> "☲"
    FortuneLevel.VERY_BAD -> "☵"
}
