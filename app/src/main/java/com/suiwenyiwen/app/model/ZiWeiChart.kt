package com.suiwenyiwen.app.model

import java.time.LocalDateTime

/**
 * 完整紫微斗数命盘
 */
data class ZiWeiChart(
    val queryTime: LocalDateTime,
    val lunarYear: Int,
    val lunarMonth: Int,
    val lunarDay: Int,
    val isLeapMonth: Boolean = false,
    val yearStem: HeavenlyStem,
    val yearBranch: EarthlyBranch,
    val monthStem: HeavenlyStem,
    val monthBranch: EarthlyBranch,
    val dayStem: HeavenlyStem,
    val dayBranch: EarthlyBranch,
    val hourStem: HeavenlyStem,
    val hourBranch: EarthlyBranch,
    val palaces: List<Palace>,        // 12 palaces in order
    val fiveElementBureau: FiveElement
) {
    fun getPalace(type: PalaceType): Palace = palaces.first { it.type == type }
}
