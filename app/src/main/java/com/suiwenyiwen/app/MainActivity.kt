package com.suiwenyiwen.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.suiwenyiwen.app.engine.Interpreter
import com.suiwenyiwen.app.engine.LunarCalendar
import com.suiwenyiwen.app.engine.PalaceBuilder
import com.suiwenyiwen.app.engine.StarPlacer
import com.suiwenyiwen.app.engine.StemBranch
import com.suiwenyiwen.app.model.FortuneResult
import com.suiwenyiwen.app.model.ZiWeiChart
import com.suiwenyiwen.app.ui.screens.HomeScreen
import com.suiwenyiwen.app.ui.screens.ResultScreen
import com.suiwenyiwen.app.ui.theme.SuiWenYiWenTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SuiWenYiWenTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen {
    object Home : Screen()
    data class Result(val fortune: FortuneResult) : Screen()
}

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    // 每秒刷新当前时间(仅在首页)
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = LocalDateTime.now()
        }
    }

    when (val screen = currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                currentTime = currentTime,
                onAskClicked = {
                    val chart = buildChart(currentTime)
                    val fortune = Interpreter.interpret(chart)
                    currentScreen = Screen.Result(fortune)
                }
            )
        }

        is Screen.Result -> {
            ResultScreen(
                fortuneResult = screen.fortune,
                onBack = { currentScreen = Screen.Home },
                onAskAgain = {
                    val newTime = LocalDateTime.now()
                    val chart = buildChart(newTime)
                    val fortune = Interpreter.interpret(chart)
                    currentScreen = Screen.Result(fortune)
                }
            )
        }
    }
}

/**
 * 构建紫微斗数命盘
 */
private fun buildChart(time: LocalDateTime): ZiWeiChart {
    val sb = StemBranch.calculate(time)
    val lunar = LunarCalendar.solarToLunar(time)

    // 构建十二宫
    val palaces = PalaceBuilder.buildPalaces(sb, lunar.month)

    // 排布星曜
    val placedPalaces = StarPlacer.placeStars(palaces, sb, lunar.day)

    // 计算五行局
    val mingPalace = placedPalaces.first { it.type == com.suiwenyiwen.app.model.PalaceType.MING }
    val bureau = PalaceBuilder.calculateFiveElementBureau(
        mingPalace.heavenlyStem.index, mingPalace.earthlyBranch.index
    )

    return ZiWeiChart(
        queryTime = time,
        lunarYear = lunar.year,
        lunarMonth = lunar.month,
        lunarDay = lunar.day,
        isLeapMonth = lunar.isLeapMonth,
        yearStem = sb.yearStem,
        yearBranch = sb.yearBranch,
        monthStem = sb.monthStem,
        monthBranch = sb.monthBranch,
        dayStem = sb.dayStem,
        dayBranch = sb.dayBranch,
        hourStem = sb.hourStem,
        hourBranch = sb.hourBranch,
        palaces = placedPalaces,
        fiveElementBureau = bureau
    )
}
