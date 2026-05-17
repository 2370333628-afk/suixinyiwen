package com.suiwenyiwen.app.engine

import com.suiwenyiwen.app.model.EarthlyBranch
import com.suiwenyiwen.app.model.HeavenlyStem
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * 天干地支计算器
 * 负责从公历时间推导出年、月、日、时的天干地支
 */
object StemBranch {

    /** 日干支参考点: 1900-01-01 = 甲戌日 */
    private val DAY_REF = LocalDateTime.of(1900, 1, 1, 0, 0)

    /**
     * 从公历时间计算所有干支信息
     */
    fun calculate(time: LocalDateTime): StemBranchResult {
        val yearStemIdx = yearStemIndex(time.year)
        val yearBranchIdx = yearBranchIndex(time.year)
        val monthStemIdx = monthStemIndex(time.year, time.monthValue)
        val monthBranchIdx = monthBranchIndex(time.monthValue)
        val dayIndex = dayStemBranchIndex(time)
        val dayStemIdx = dayIndex % 10
        val dayBranchIdx = dayIndex % 12
        val hourBranchIdx = hourBranchIndex(time.hour)
        val hourStemIdx = hourStemIndex(dayStemIdx, hourBranchIdx)

        return StemBranchResult(
            yearStem = HeavenlyStem.entries[yearStemIdx],
            yearBranch = EarthlyBranch.entries[yearBranchIdx],
            monthStem = HeavenlyStem.entries[monthStemIdx],
            monthBranch = EarthlyBranch.entries[monthBranchIdx],
            dayStem = HeavenlyStem.entries[dayStemIdx],
            dayBranch = EarthlyBranch.entries[dayBranchIdx],
            hourStem = HeavenlyStem.entries[hourStemIdx],
            hourBranch = EarthlyBranch.entries[hourBranchIdx]
        )
    }

    /** 年天干: (年 - 4) % 10 */
    private fun yearStemIndex(year: Int): Int = (year - 4) % 10

    /** 年地支: (年 - 4) % 12 */
    private fun yearBranchIndex(year: Int): Int = (year - 4) % 12

    /**
     * 月天干: 根据年干和月数计算
     * 五虎遁: 甲己之年丙作首, 乙庚之岁戊为头, 丙辛必定寻庚起, 丁壬壬位顺行流, 戊癸何方发, 甲寅之上好追求
     */
    private fun monthStemIndex(year: Int, month: Int): Int {
        val yearStem = yearStemIndex(year)
        val firstMonthStem = when (yearStem) {
            0, 5 -> 2  // 甲、己 → 丙(2)
            1, 6 -> 4  // 乙、庚 → 戊(4)
            2, 7 -> 6  // 丙、辛 → 庚(6)
            3, 8 -> 8  // 丁、壬 → 壬(8)
            4, 9 -> 0  // 戊、癸 → 甲(0)
            else -> 0
        }
        return (firstMonthStem + month - 1) % 10
    }

    /** 月地支: 正月=寅(2), 二月=卯(3), ... */
    private fun monthBranchIndex(month: Int): Int = (month + 1) % 12

    /**
     * 日干支: 计算从1900-01-01(甲戌)的天数偏移
     * 甲戌 = 天干0, 地支10
     * 所以日干支序号偏移 = 0 (因为甲戌在六十甲子中序号为0? 不, 甲戌是10)
     * 六十甲子中甲子=0, 甲戌=10
     * days offset from 1900-01-01: 0 → 甲戌(10)
     * 所以: index = (days + 10) % 60
     */
    private fun dayStemBranchIndex(time: LocalDateTime): Int {
        val dateOnly = time.toLocalDate().atStartOfDay()
        val days = ChronoUnit.DAYS.between(DAY_REF, dateOnly).toInt()
        // 1900-01-01 = 甲戌, 六十甲子序 = 10 (甲子=0, 乙丑=1, ..., 甲戌=10)
        return Math.floorMod(days + 10, 60)
    }

    /** 时辰地支 */
    private fun hourBranchIndex(hour: Int): Int = ((hour + 1) / 2) % 12

    /**
     * 时天干: 五鼠遁根据日干推算
     * 甲己还加甲, 乙庚丙作初, 丙辛从戊起, 丁壬庚子居, 戊癸何方发, 壬子是真途
     */
    private fun hourStemIndex(dayStem: Int, hourBranch: Int): Int {
        val ziHourStem = when (dayStem) {
            0, 5 -> 0  // 甲、己 → 甲子
            1, 6 -> 2  // 乙、庚 → 丙子
            2, 7 -> 4  // 丙、辛 → 戊子
            3, 8 -> 6  // 丁、壬 → 庚子
            4, 9 -> 8  // 戊、癸 → 壬子
            else -> 0
        }
        return (ziHourStem + hourBranch) % 10
    }
}

/**
 * 干支计算结果
 */
data class StemBranchResult(
    val yearStem: HeavenlyStem,
    val yearBranch: EarthlyBranch,
    val monthStem: HeavenlyStem,
    val monthBranch: EarthlyBranch,
    val dayStem: HeavenlyStem,
    val dayBranch: EarthlyBranch,
    val hourStem: HeavenlyStem,
    val hourBranch: EarthlyBranch
) {
    /** 年干支展示 */
    val yearStemBranch: String get() = "${yearStem.display}${yearBranch.display}"
    /** 月干支展示 */
    val monthStemBranch: String get() = "${monthStem.display}${monthBranch.display}"
    /** 日干支展示 */
    val dayStemBranch: String get() = "${dayStem.display}${dayBranch.display}"
    /** 时干支展示 */
    val hourStemBranch: String get() = "${hourStem.display}${hourBranch.display}"

    /** 获取当前时辰名称 */
    fun currentShiChen(): String {
        val branches = arrayOf("子时","丑时","寅时","卯时","辰时","巳时",
            "午时","未时","申时","酉时","戌时","亥时")
        return branches[hourBranch.index]
    }
}
