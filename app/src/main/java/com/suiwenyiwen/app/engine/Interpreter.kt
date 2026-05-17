package com.suiwenyiwen.app.engine

import com.suiwenyiwen.app.data.InterpretationTemplates
import com.suiwenyiwen.app.model.*

/**
 * 紫微斗数解读引擎
 * 综合分析各宫星曜组合，生成详细的运势解读
 */
object Interpreter {

    /**
     * 生成完整解读
     */
    fun interpret(chart: ZiWeiChart): FortuneResult {
        val palaceReadings = chart.palaces.map { interpretPalace(it, chart) }

        // 综合评估整体运势
        val overallFortune = calculateOverallFortune(palaceReadings)

        // 关键影响星曜
        val keyStars = identifyKeyStars(chart)

        // 有利方向
        val favorableDirections = identifyFavorableDirections(palaceReadings)

        // 注意事项
        val cautions = identifyCautions(palaceReadings, chart)

        // 综合建议
        val generalAdvice = generateAdvice(overallFortune, keyStars, palaceReadings)

        return FortuneResult(
            overallFortune = overallFortune,
            overallSummary = generateOverallSummary(overallFortune, chart),
            palaceReadings = palaceReadings,
            keyStars = keyStars,
            favorableDirections = favorableDirections,
            cautions = cautions,
            generalAdvice = generalAdvice,
            queryTimeDescription = buildString {
                append("${chart.queryTime.year}年${chart.queryTime.monthValue}月${chart.queryTime.dayOfMonth}日")
                append(" ${chart.hourBranch.display}时")
                append(" (农历${chart.lunarYear}年")
                val lunar = LunarCalendar.lunarMonthName(chart.lunarMonth, chart.isLeapMonth)
                append("$lunar${LunarCalendar.lunarDayName(chart.lunarDay)})")
                append(" 年柱${chart.yearStem.display}${chart.yearBranch.display}")
                append(" 日柱${chart.dayStem.display}${chart.dayBranch.display}")
                append(" 时柱${chart.hourStem.display}${chart.hourBranch.display}")
            }
        )
    }

    private fun interpretPalace(palace: Palace, chart: ZiWeiChart): PalaceReading {
        val allStars = palace.majorStars + palace.auxiliaryStars
        val starAnalysis = analyzeStarCombination(palace, allStars)
        val fortuneLevel = evaluatePalaceFortune(palace)

        val mainAnalysis = InterpretationTemplates.getPalaceAnalysis(
            palace.type,
            palace.majorStars,
            palace.auxiliaryStars,
            palace.transformations,
            fortuneLevel
        )

        val advice = InterpretationTemplates.getPalaceAdvice(
            palace.type, fortuneLevel, palace.majorStars
        )

        return PalaceReading(
            palaceType = palace.type,
            stemBranch = palace.stemBranchDisplay,
            majorStars = palace.majorStars,
            auxiliaryStars = palace.auxiliaryStars,
            fortuneLevel = fortuneLevel,
            mainAnalysis = mainAnalysis,
            starAnalysis = starAnalysis,
            advice = advice
        )
    }

    /**
     * 分析星曜组合
     */
    private fun analyzeStarCombination(palace: Palace, allStars: List<String>): String {
        val parts = mutableListOf<String>()

        // 分析主星
        if (palace.majorStars.isNotEmpty()) {
            val majorDesc = palace.majorStars.joinToString("、") { star ->
                val prefix = when {
                    hasTransformation(palace, "化禄", star) -> "$star 化禄"
                    hasTransformation(palace, "化权", star) -> "$star 化权"
                    hasTransformation(palace, "化科", star) -> "$star 化科"
                    hasTransformation(palace, "化忌", star) -> "$star 化忌"
                    else -> star
                }
                prefix
            }
            parts.add("主星: $majorDesc")
        }

        // 分析辅星
        if (palace.auxiliaryStars.isNotEmpty()) {
            val auspicious = palace.auxiliaryStars.filter { isAuspiciousStar(it) }
            val inauspicious = palace.auxiliaryStars.filter { isInauspiciousStar(it) }

            if (auspicious.isNotEmpty()) {
                parts.add("吉辅: ${auspicious.joinToString("、")}")
            }
            if (inauspicious.isNotEmpty()) {
                parts.add("煞星: ${inauspicious.joinToString("、")}")
            }
        }

        return parts.joinToString("\n")
    }

    /**
     * 评估单个宫位吉凶
     */
    private fun evaluatePalaceFortune(palace: Palace): FortuneLevel {
        var score = 3 // 起始为平

        // 主星吉凶评分
        for (star in palace.majorStars) {
            score += starNatureScore(star)
        }

        // 辅星影响
        for (star in palace.auxiliaryStars) {
            if (isAuspiciousStar(star)) score += 1
            if (isInauspiciousStar(star)) score -= 1
        }

        // 四化影响
        for (transformation in palace.transformations) {
            score += when (transformation) {
                Transformation.HUA_LU -> 2
                Transformation.HUA_QUAN -> 1
                Transformation.HUA_KE -> 1
                Transformation.HUA_JI -> -2
            }
        }

        // 综合评分 -> 等级
        return when {
            score >= 6 -> FortuneLevel.VERY_GOOD
            score >= 4 -> FortuneLevel.GOOD
            score >= 2 -> FortuneLevel.NEUTRAL
            score >= 0 -> FortuneLevel.BAD
            else -> FortuneLevel.VERY_BAD
        }
    }

    /** 主星吉凶评分 */
    private fun starNatureScore(starName: String): Int = when (starName) {
        // 吉星
        "紫微", "天府", "天相" -> 2
        "太阳", "太阴", "天梁" -> 1
        "天同", "天机" -> 1
        "武曲" -> 1
        // 中性星
        "廉贞" -> 0
        "七杀", "破军" -> -1
        "贪狼" -> -1
        // 凶星
        "巨门" -> -2
        else -> 0
    }

    private fun isAuspiciousStar(name: String): Boolean = name in listOf(
        "左辅", "右弼", "文昌", "文曲", "天魁", "天钺", "禄存", "天马"
    )

    private fun isInauspiciousStar(name: String): Boolean = name in listOf(
        "擎羊", "陀罗", "火星", "铃星", "地空", "地劫"
    )

    private fun hasTransformation(palace: Palace, transDisplay: String, forStar: String): Boolean {
        val target = when (transDisplay) {
            "化禄" -> Transformation.HUA_LU
            "化权" -> Transformation.HUA_QUAN
            "化科" -> Transformation.HUA_KE
            "化忌" -> Transformation.HUA_JI
            else -> null
        }
        return target != null && target in palace.transformations &&
                forStar in palace.majorStars
    }

    /**
     * 计算整体运势
     */
    private fun calculateOverallFortune(readings: List<PalaceReading>): FortuneLevel {
        // 重点宫位权重更高
        val weights = mapOf(
            PalaceType.MING to 3,
            PalaceType.CAREER to 2,
            PalaceType.WEALTH to 2,
            PalaceType.FORTUNE to 2
        )

        var totalScore = 0.0
        var totalWeight = 0.0

        for (reading in readings) {
            val weight = weights[reading.palaceType] ?: 1
            val score = when (reading.fortuneLevel) {
                FortuneLevel.VERY_GOOD -> 5
                FortuneLevel.GOOD -> 4
                FortuneLevel.NEUTRAL -> 3
                FortuneLevel.BAD -> 2
                FortuneLevel.VERY_BAD -> 1
            }
            totalScore += score * weight
            totalWeight += weight
        }

        val avgScore = totalScore / totalWeight
        return when {
            avgScore >= 4.5 -> FortuneLevel.VERY_GOOD
            avgScore >= 3.5 -> FortuneLevel.GOOD
            avgScore >= 2.5 -> FortuneLevel.NEUTRAL
            avgScore >= 1.5 -> FortuneLevel.BAD
            else -> FortuneLevel.VERY_BAD
        }
    }

    private fun identifyKeyStars(chart: ZiWeiChart): List<String> {
        val keyStars = mutableListOf<String>()

        // 命宫主星
        val mingPalace = chart.getPalace(PalaceType.MING)
        keyStars.addAll(mingPalace.majorStars)

        // 四化星
        for (palace in chart.palaces) {
            if (palace.transformations.isNotEmpty()) {
                for (i in palace.transformations.indices) {
                    if (i < palace.majorStars.size) {
                        val trans = palace.transformations[i]
                        keyStars.add("${palace.majorStars[i]}${trans.display}")
                    }
                }
            }
        }

        return keyStars.distinct().take(5)
    }

    private fun identifyFavorableDirections(readings: List<PalaceReading>): List<String> {
        val directions = mutableListOf<String>()

        // 找出吉利的宫位作为有利方向
        for (reading in readings) {
            if (reading.fortuneLevel == FortuneLevel.VERY_GOOD ||
                reading.fortuneLevel == FortuneLevel.GOOD) {
                directions.add("${reading.palaceType.display}宫有利")
            }
        }

        if (directions.isEmpty()) {
            directions.add("宜静不宜动，暂守成局")
        }

        return directions.take(4)
    }

    private fun identifyCautions(readings: List<PalaceReading>, chart: ZiWeiChart): List<String> {
        val cautions = mutableListOf<String>()

        // 凶宫警告
        for (reading in readings) {
            if (reading.fortuneLevel == FortuneLevel.VERY_BAD ||
                reading.fortuneLevel == FortuneLevel.BAD) {
                cautions.add("${reading.palaceType.display}宫欠安，需留意${getPalaceDomain(reading.palaceType)}")
            }
        }

        // 化忌所在
        for (palace in chart.palaces) {
            if (palace.transformations.contains(Transformation.HUA_JI)) {
                cautions.add("${palace.type.display}宫有化忌，${getHuaJiWarning(palace.type)}")
            }
        }

        return cautions
    }

    private fun getPalaceDomain(type: PalaceType): String = when (type) {
        PalaceType.MING -> "自身状态"
        PalaceType.BROTHERS -> "人际关系"
        PalaceType.SPOUSE -> "感情事务"
        PalaceType.CHILDREN -> "子女或创意"
        PalaceType.WEALTH -> "财务方面"
        PalaceType.HEALTH -> "健康问题"
        PalaceType.TRAVEL -> "出行变动"
        PalaceType.FRIENDS -> "社交交际"
        PalaceType.CAREER -> "事业学业"
        PalaceType.PROPERTY -> "居家环境"
        PalaceType.FORTUNE -> "精神情绪"
        PalaceType.PARENTS -> "家庭长辈"
    }

    private fun getHuaJiWarning(type: PalaceType): String = when (type) {
        PalaceType.MING -> "凡事多有阻滞，宜低调行事"
        PalaceType.WEALTH -> "慎防破财，不宜投资"
        PalaceType.CAREER -> "事业多波折，需耐心应对"
        PalaceType.SPOUSE -> "感情易生变，多沟通为上"
        PalaceType.HEALTH -> "注意身体健康，勿过劳"
        PalaceType.TRAVEL -> "出行注意安全"
        else -> "此领域需多加留意"
    }

    private fun generateOverallSummary(fortune: FortuneLevel, chart: ZiWeiChart): String {
        val mingPalace = chart.getPalace(PalaceType.MING)
        val mainStar = mingPalace.majorStars.firstOrNull() ?: ""

        return buildString {
            append("此时${chart.hourBranch.display}时排盘，")
            append("命宫坐${mingPalace.stemBranchDisplay}，")
            if (mainStar.isNotEmpty()) {
                append("主星$mainStar")
                if (mingPalace.majorStars.size > 1) {
                    append("、${mingPalace.majorStars[1]}")
                }
                append("守命。")
            }
            append(when (fortune) {
                FortuneLevel.VERY_GOOD -> "命盘气象鼎盛，诸事皆宜，所问之事大有可为。"
                FortuneLevel.GOOD -> "命盘吉多于凶，顺势而为，所问之事有望达成。"
                FortuneLevel.NEUTRAL -> "命盘喜忧参半，当审时度势，所问之事需权衡得失。"
                FortuneLevel.BAD -> "命盘煞星较重，多有阻碍，所问之事暂不宜急进。"
                FortuneLevel.VERY_BAD -> "命盘凶星聚集，时运不济，所问之事恐难如愿，宜静观其变。"
            })
        }
    }

    private fun generateAdvice(
        fortune: FortuneLevel,
        keyStars: List<String>,
        readings: List<PalaceReading>
    ): String {
        return buildString {
            when (fortune) {
                FortuneLevel.VERY_GOOD -> {
                    append("当前天时地利人和兼备。")
                    append(keyStars.take(2).joinToString("、") { it }.let {
                        if (it.isNotEmpty()) " $it 加持命盘，气势如虹。" else ""
                    })
                    append(" 建议把握时机，大胆行动，但也不可疏忽细节。")
                }
                FortuneLevel.GOOD -> {
                    append("运势总体向好，但仍需用心经营。")
                    append(" 从盘面来看，")
                    val favorable = readings.filter { it.fortuneLevel.score >= 4 }
                    if (favorable.isNotEmpty()) {
                        append(favorable.take(2).joinToString("、") {
                            it.palaceType.display
                        }.let { "$it等方面较有优势。" })
                    }
                    append(" 建议顺势而为，发挥所长。")
                }
                FortuneLevel.NEUTRAL -> {
                    append("运程起伏，需明辨方向。")
                    append(" 凡事不可冒进，需审时度势。")
                    append(" 建议暂守成局，等待更佳时机再行动。多听取他人意见，切勿独断专行。")
                }
                FortuneLevel.BAD -> {
                    append("运势低迷，诸事不宜强行推进。")
                    append(" 当前不宜做重大决策，建议养精蓄锐、以守为攻。")
                    append(" 如有不得不为之事情，务必做好万全准备。")
                }
                FortuneLevel.VERY_BAD -> {
                    append("大凶之兆，凡事需倍加谨慎。")
                    append(" 目前不宜任何冒险之举，建议以静制动、韬光养晦。")
                    append(" 心中所愿之事，建议暂时搁置，静待天时转好再议。")
                }
            }
        }
    }
}
