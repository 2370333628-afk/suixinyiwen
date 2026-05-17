package com.suiwenyiwen.app.engine

import com.suiwenyiwen.app.model.*

/**
 * 星曜排布引擎
 * 根据时间信息将十四主星、辅星和四化排列到十二宫中
 */
object StarPlacer {

    /** 十四主星名称 */
    private val MAJOR_STARS = listOf(
        "紫微", "天机", "太阳", "武曲", "天同", "廉贞",
        "天府", "太阴", "贪狼", "巨门", "天相", "天梁", "七杀", "破军"
    )

    /**
     * 排布所有星曜到宫位
     */
    fun placeStars(
        palaces: List<Palace>,
        sb: StemBranchResult,
        lunarDay: Int
    ): List<Palace> {
        val mutablePalaces = palaces.map { it.copy() }.toMutableList()

        placeMajorStars(mutablePalaces, sb, lunarDay)
        placeAuxiliaryStars(mutablePalaces, sb)
        applyTransformations(mutablePalaces, sb)

        return mutablePalaces
    }

    /**
     * 排十四主星
     */
    private fun placeMajorStars(palaces: MutableList<Palace>, sb: StemBranchResult, lunarDay: Int) {
        val mingPalace = palaces.first { it.type == PalaceType.MING }
        val bureau = PalaceBuilder.calculateFiveElementBureau(
            mingPalace.heavenlyStem.index, mingPalace.earthlyBranch.index
        )
        val bureauNum = bureau.bureauNumber()

        // 紫微星位置: 局数 - (日数 % 局数), 为0则取局数
        val dayMod = lunarDay % bureauNum
        val ziweiOffset = if (dayMod == 0) bureauNum else dayMod

        // 紫微宫位的十二宫索引 (寅=2为基准)
        val ziweiPalaceBranch = findPalaceIndex(palaces, ziweiOffset)

        // 紫微系: 紫微, 天机, (空一位), 太阳, 武曲, 天同, 廉贞
        val ziweiStars = listOf(
            "紫微" to 0,
            "天机" to -1,
            null to -2,  // 空一位
            "太阳" to -3,
            "武曲" to -4,
            "天同" to -5,
            null to -6,  // 空一位
            "廉贞" to -8
        )

        ziweiStars.forEach { (name, offset) ->
            if (name != null && offset != null) {
                val idx = Math.floorMod(ziweiPalaceBranch + offset, 12)
                addStar(palaces, idx, name)
            }
        }

        // 天府系: 天府与紫微(寅+戌-紫微) = (14-紫微), 天府系星由此顺排
        val tianfuPalaceBranch = Math.floorMod(14 - ziweiPalaceBranch, 12)
        val tianfuStars = listOf(
            "天府" to 0,
            "太阴" to 1,
            "贪狼" to 2,
            "巨门" to 3,
            "天相" to 4,
            "天梁" to 5,
            "七杀" to 6,
            null to 7,     // 空
            null to 8,     // 空
            null to 9,     // 空
            "破军" to 10
        )

        tianfuStars.forEach { (name, offset) ->
            if (name != null) {
                val idx = Math.floorMod(tianfuPalaceBranch + offset, 12)
                addStar(palaces, idx, name)
            }
        }
    }

    /**
     * 排辅星
     */
    private fun placeAuxiliaryStars(palaces: MutableList<Palace>, sb: StemBranchResult) {
        // 左辅: 辰上顺数至月
        val zuofuIdx = Math.floorMod(4 + sb.monthBranch.index, 12)
        addAuxStar(palaces, zuofuIdx, "左辅")

        // 右弼: 戌上逆数至月
        val youbiIdx = Math.floorMod(10 - sb.monthBranch.index, 12)
        addAuxStar(palaces, youbiIdx, "右弼")

        // 文昌: 戌上逆数至时
        val wenchangIdx = Math.floorMod(10 - sb.hourBranch.index, 12)
        addAuxStar(palaces, wenchangIdx, "文昌")

        // 文曲: 辰上顺数至时
        val wenquIdx = Math.floorMod(4 + sb.hourBranch.index, 12)
        addAuxStar(palaces, wenquIdx, "文曲")

        // 天魁: 年干决定
        val tiankuiIdx = when (sb.yearStem.index) {
            0 -> 1; 1 -> 0; 2 -> 11; 3 -> 10; 4 -> 1; 5 -> 0
            6 -> 11; 7 -> 10; 8 -> 3; 9 -> 2
            else -> 0
        }
        addAuxStar(palaces, tiankuiIdx, "天魁")

        // 天钺: 年干决定
        val tianyueIdx = when (sb.yearStem.index) {
            0 -> 7; 1 -> 6; 2 -> 9; 3 -> 8; 4 -> 7; 5 -> 6
            6 -> 9; 7 -> 8; 8 -> 5; 9 -> 4
            else -> 6
        }
        addAuxStar(palaces, tianyueIdx, "天钺")

        // 禄存: 年干决定
        val lucunIdx = when (sb.yearStem.index) {
            0 -> 2; 1 -> 3; 2 -> 4; 3 -> 5; 4 -> 6; 5 -> 7
            6 -> 8; 7 -> 9; 8 -> 11; 9 -> 0
            else -> 2
        }
        addAuxStar(palaces, lucunIdx, "禄存")

        // 擎羊: 禄存前一位
        addAuxStar(palaces, Math.floorMod(lucunIdx + 1, 12), "擎羊")

        // 陀罗: 禄存后一位
        addAuxStar(palaces, Math.floorMod(lucunIdx - 1, 12), "陀罗")

        // 火星: 时支决定
        val huoxingIdx = when (sb.hourBranch.index) {
            0, 1, 2 -> Math.floorMod(2 + sb.hourBranch.index, 12)
            3, 4, 5 -> Math.floorMod(3 + sb.hourBranch.index, 12)
            6, 7, 8 -> Math.floorMod(4 + sb.hourBranch.index, 12)
            9, 10, 11 -> Math.floorMod(5 + sb.hourBranch.index, 12)
            else -> 0
        }
        addAuxStar(palaces, huoxingIdx, "火星")

        // 铃星: 时支决定
        val lingxingIdx = when (sb.hourBranch.index) {
            0, 1, 2 -> Math.floorMod(3 + sb.hourBranch.index, 12)
            3, 4, 5 -> Math.floorMod(4 + sb.hourBranch.index, 12)
            6, 7, 8 -> Math.floorMod(5 + sb.hourBranch.index, 12)
            9, 10, 11 -> Math.floorMod(6 + sb.hourBranch.index, 12)
            else -> 6
        }
        addAuxStar(palaces, lingxingIdx, "铃星")

        // 地空: 时支逆数
        val dikongIdx = Math.floorMod(11 - sb.hourBranch.index, 12)
        addAuxStar(palaces, dikongIdx, "地空")

        // 地劫: 时支顺数
        val dijieIdx = Math.floorMod(11 + sb.hourBranch.index, 12)
        addAuxStar(palaces, dijieIdx, "地劫")

        // 天马: 年支决定 (寅午戌天马在申, 申子辰天马在寅, 巳酉丑天马在亥, 亥卯未天马在巳)
        val yearBranch = sb.yearBranch.index
        val tianmaIdx = when {
            yearBranch in listOf(2, 6, 10) -> 8   // 寅午戌 → 申
            yearBranch in listOf(0, 8, 4) -> 2     // 申子辰 → 寅
            yearBranch in listOf(5, 9, 1) -> 11    // 巳酉丑 → 亥
            yearBranch in listOf(11, 3, 7) -> 5    // 亥卯未 → 巳
            else -> 8
        }
        addAuxStar(palaces, tianmaIdx, "天马")
    }

    /**
     * 应用四化
     */
    private fun applyTransformations(palaces: MutableList<Palace>, sb: StemBranchResult) {
        val transformationMap = getTransformationMap(sb.yearStem.index)

        transformationMap.forEach { (starName, transformation) ->
            val palaceIdx = findStarPalaceIndex(palaces, starName)
            if (palaceIdx >= 0) {
                val p = palaces[palaceIdx]
                palaces[palaceIdx] = p.copy(
                    transformations = p.transformations + transformation
                )
            }
        }
    }

    /**
     * 获取四化映射表
     */
    private fun getTransformationMap(yearStemIdx: Int): Map<String, Transformation> {
        return when (yearStemIdx) {
            0 -> mapOf(  // 甲
                "廉贞" to Transformation.HUA_LU,
                "破军" to Transformation.HUA_QUAN,
                "武曲" to Transformation.HUA_KE,
                "太阳" to Transformation.HUA_JI
            )
            1 -> mapOf(  // 乙
                "天机" to Transformation.HUA_LU,
                "天梁" to Transformation.HUA_QUAN,
                "紫微" to Transformation.HUA_KE,
                "太阴" to Transformation.HUA_JI
            )
            2 -> mapOf(  // 丙
                "天同" to Transformation.HUA_LU,
                "天机" to Transformation.HUA_QUAN,
                "文昌" to Transformation.HUA_KE,
                "廉贞" to Transformation.HUA_JI
            )
            3 -> mapOf(  // 丁
                "太阴" to Transformation.HUA_LU,
                "天同" to Transformation.HUA_QUAN,
                "天机" to Transformation.HUA_KE,
                "巨门" to Transformation.HUA_JI
            )
            4 -> mapOf(  // 戊
                "贪狼" to Transformation.HUA_LU,
                "太阴" to Transformation.HUA_QUAN,
                "右弼" to Transformation.HUA_KE,
                "天机" to Transformation.HUA_JI
            )
            5 -> mapOf(  // 己
                "武曲" to Transformation.HUA_LU,
                "贪狼" to Transformation.HUA_QUAN,
                "天梁" to Transformation.HUA_KE,
                "文曲" to Transformation.HUA_JI
            )
            6 -> mapOf(  // 庚
                "太阳" to Transformation.HUA_LU,
                "武曲" to Transformation.HUA_QUAN,
                "太阴" to Transformation.HUA_KE,
                "天同" to Transformation.HUA_JI
            )
            7 -> mapOf(  // 辛
                "巨门" to Transformation.HUA_LU,
                "太阳" to Transformation.HUA_QUAN,
                "文曲" to Transformation.HUA_KE,
                "文昌" to Transformation.HUA_JI
            )
            8 -> mapOf(  // 壬
                "天梁" to Transformation.HUA_LU,
                "紫微" to Transformation.HUA_QUAN,
                "左辅" to Transformation.HUA_KE,
                "武曲" to Transformation.HUA_JI
            )
            9 -> mapOf(  // 癸
                "破军" to Transformation.HUA_LU,
                "巨门" to Transformation.HUA_QUAN,
                "太阴" to Transformation.HUA_KE,
                "贪狼" to Transformation.HUA_JI
            )
            else -> emptyMap()
        }
    }

    private fun findPalaceIndex(palaces: List<Palace>, fromYinOffset: Int): Int {
        // 寅的地支索引=2，从寅开始顺数offset位
        val targetBranch = Math.floorMod(2 + fromYinOffset - 1, 12)
        return palaces.indexOfFirst { it.earthlyBranch.index == targetBranch }
    }

    private fun findStarPalaceIndex(palaces: List<Palace>, starName: String): Int {
        return palaces.indexOfFirst {
            it.majorStars.contains(starName) || it.auxiliaryStars.contains(starName)
        }
    }

    private fun addStar(palaces: MutableList<Palace>, idx: Int, starName: String) {
        if (idx in palaces.indices) {
            val p = palaces[idx]
            palaces[idx] = p.copy(majorStars = p.majorStars + starName)
        }
    }

    private fun addAuxStar(palaces: MutableList<Palace>, idx: Int, starName: String) {
        if (idx in palaces.indices) {
            val p = palaces[idx]
            palaces[idx] = p.copy(auxiliaryStars = p.auxiliaryStars + starName)
        }
    }
}
