package com.suiwenyiwen.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.suiwenyiwen.app.engine.LunarCalendar
import com.suiwenyiwen.app.engine.LunarDate
import com.suiwenyiwen.app.engine.StemBranch
import com.suiwenyiwen.app.engine.StemBranchResult
import com.suiwenyiwen.app.ui.theme.GoldLight
import com.suiwenyiwen.app.ui.theme.TextMuted
import com.suiwenyiwen.app.ui.theme.TextSecondary
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeDisplay(
    currentTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val sb = StemBranch.calculate(currentTime)
    val lunar = LunarCalendar.solarToLunar(currentTime)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 公历时间
        AnimatedContent(targetState = currentTime) { time ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = time.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                    color = GoldLight,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    style = androidx.compose.material3.MaterialTheme.typography.displayMedium,
                    color = GoldLight,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 农历时间
        Text(
            text = "农历${formatLunarDate(lunar)}",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            color = TextSecondary
        )

        // 干支信息
        Text(
            text = "${sb.yearStemBranch}年 ${sb.monthStemBranch}月 ${sb.dayStemBranch}日 ${sb.hourStemBranch}时",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            color = TextMuted
        )

        // 时辰
        Text(
            text = "【${sb.currentShiChen()}】",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            color = GoldLight,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatLunarDate(lunar: LunarDate): String {
    val monthName = LunarCalendar.lunarMonthName(lunar.month, lunar.isLeapMonth)
    val dayName = LunarCalendar.lunarDayName(lunar.day)
    return "$monthName$dayName"
}
