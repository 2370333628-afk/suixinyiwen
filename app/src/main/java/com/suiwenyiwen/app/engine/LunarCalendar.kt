package com.suiwenyiwen.app.engine

import java.time.LocalDateTime

/**
 * 农历数据表，每个条目用16位编码：
 * bits 0-3: 闰月月份 (0=无闰月)
 * bits 4-15: 12个月的大小月 (bit set=30天, clear=29天), 第4位=正月
 * 闰月天数由第16位表示
 */
private val LUNAR_INFO = intArrayOf(
    0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, // 1900-1909
    0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977, // 1910-1919
    0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, // 1920-1929
    0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950, // 1930-1939
    0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, // 1940-1949
    0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0, // 1950-1959
    0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, // 1960-1969
    0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6, // 1970-1979
    0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, // 1980-1989
    0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x05ac0, 0x0ab60, 0x096d5, 0x092e0, // 1990-1999
    0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, // 2000-2009
    0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, // 2010-2019
    0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, // 2020-2029
    0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, // 2030-2039
    0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0, // 2040-2049
    0x14b63, 0x09370, 0x049f8, 0x04970, 0x064b0, 0x168a6, 0x0ea50, 0x06b20, 0x1a6c4, 0x0aae0, // 2050-2059
    0x0a2e0, 0x0d2e3, 0x0c960, 0x0d557, 0x0d4a0, 0x0da50, 0x05d55, 0x056a0, 0x0a6d0, 0x055d4, // 2060-2069
    0x052d0, 0x0a9b8, 0x0a950, 0x0b4a0, 0x0b6a6, 0x0ad50, 0x055a0, 0x0aba4, 0x0a5b0, 0x052b0, // 2070-2079
    0x0b273, 0x06930, 0x07337, 0x06aa0, 0x0ad50, 0x14b55, 0x04b60, 0x0a570, 0x054e4, 0x0d160, // 2080-2089
    0x0e968, 0x0d520, 0x0daa0, 0x16aa6, 0x056d0, 0x04ae0, 0x0a9d4, 0x0a4d0, 0x0d150, 0x0f252, // 2090-2099
    0x0d520                                                             // 2100
)

/**
 * 农历日期数据类
 */
data class LunarDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val isLeapMonth: Boolean
)

/**
 * 公历与农历转换器
 */
object LunarCalendar {

    private const val BASE_YEAR = 1900
    private val BASE_DATE = LocalDateTime.of(1900, 1, 31, 0, 0) // 1900年正月初一

    /**
     * 公历转农历
     */
    fun solarToLunar(solar: LocalDateTime): LunarDate {
        val year = solar.year
        val month = solar.monthValue
        val day = solar.dayOfMonth

        // 计算从1900年1月31日到目标日期的偏移天数
        val targetDate = LocalDateTime.of(year, month, day, 0, 0)
        var offset = 0
        var iterDate = BASE_DATE
        while (iterDate.isBefore(targetDate)) {
            offset++
            iterDate = iterDate.plusDays(1)
        }
        // 处理目标日期早于基准日期的情况
        while (targetDate.isBefore(BASE_DATE)) {
            offset--
            // 简化处理: 旧日期暂不处理
            break
        }

        // 从1900年开始逐月查找
        var lunarYear = BASE_YEAR
        var lunarMonth = 1
        var lunarDay = 1
        var isLeap = false
        var days = offset

        while (lunarYear <= 2100) {
            val yearInfo = LUNAR_INFO[lunarYear - BASE_YEAR]
            val monthsInYear = monthsInLunarYear(yearInfo)
            var tmp = 0

            for (m in 1..monthsInYear) {
                val monthDays = daysInMonth(yearInfo, m)
                tmp += monthDays
                if (days < tmp) {
                    lunarMonth = m
                    lunarDay = days - (tmp - monthDays) + 1
                    isLeap = m > 12
                    // Normalize leap month
                    val normalizedMonth = if (isLeap) m - 1 else m
                    val leapMonth = leapMonthOf(yearInfo)
                    val actualIsLeap = isLeap || (normalizedMonth == leapMonth && isLeap)
                    return LunarDate(lunarYear, normalizedMonth.coerceAtMost(12), lunarDay,
                        isLeap)
                }
            }
            days -= tmp
            lunarYear++
        }

        // 如果超出范围，直接返回公历日期作为近似农历
        return LunarDate(lunarYear, month, day, false)
    }

    private fun leapMonthOf(yearInfo: Int): Int = yearInfo and 0xF

    private fun daysInMonth(yearInfo: Int, month: Int): Int {
        val leap = leapMonthOf(yearInfo)
        return if (month > 12) {
            // 闰月：天数由第16位决定
            if ((yearInfo shr 16) and 1 == 0) 29 else 30
        } else {
            // 闰月之后的正月，位索引需要+1（闰月占了一个位）
            val bitIndex = if (leap > 0 && month > leap) month else month - 1
            if (bitIndex > 11) 29
            else if ((yearInfo shr (4 + bitIndex)) and 1 == 1) 30 else 29
        }
    }

    private fun monthsInLunarYear(yearInfo: Int): Int {
        val leap = leapMonthOf(yearInfo)
        return if (leap > 0) 13 else 12
    }

    /**
     * 获取月份的农历表示
     */
    fun lunarMonthName(month: Int, isLeap: Boolean): String {
        val prefix = if (isLeap) "闰" else ""
        val name = when (month) {
            1 -> "正" ; 2 -> "二" ; 3 -> "三" ; 4 -> "四"
            5 -> "五" ; 6 -> "六" ; 7 -> "七" ; 8 -> "八"
            9 -> "九" ; 10 -> "十" ; 11 -> "冬" ; 12 -> "腊"
            else -> month.toString()
        }
        return "$prefix${name}月"
    }

    /**
     * 获取农历日期的中文表示
     */
    fun lunarDayName(day: Int): String {
        val prefixes = arrayOf("", "初", "十", "廿", "卅")
        val stemDay = arrayOf("","一","二","三","四","五","六","七","八","九","十")
        return when {
            day == 10 -> "初十"
            day == 20 -> "二十"
            day == 30 -> "三十"
            day < 10 -> "初${stemDay[day]}"
            day < 20 -> "十${stemDay[day - 10]}"
            day < 30 -> "廿${stemDay[day - 20]}"
            else -> day.toString()
        }
    }
}
