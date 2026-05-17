package com.suiwenyiwen.app.engine

import com.suiwenyiwen.app.model.*

/**
 * 十二宫排列构建器
 * 以当前时间的时辰地支、日干支为基准确定命宫位置，逆时针排列十二宫
 */
object PalaceBuilder {

    /**
     * 构建十二宫
     */
    fun buildPalaces(sb: StemBranchResult, lunarMonth: Int): List<Palace> {
        // 命宫地支 = 从寅起正月，顺数至当前月，再逆数至当前时
        val mingBranchIdx = calculateMingPalaceBranch(lunarMonth, sb.hourBranch.index)

        // 十二宫逆时针排列：命宫 → 兄弟 → 夫妻 → 子女 → 财帛 → 疾厄 → 迁移 → 交友 → 官禄 → 田宅 → 福德 → 父母
        val palaceTypes = PalaceType.entries.sortedBy { it.order }

        return palaceTypes.mapIndexed { i, type ->
            // 逆时针: 命宫位置 - i
            val branchIdx = Math.floorMod(mingBranchIdx - i, 12)
            val branch = EarthlyBranch.entries[branchIdx]

            // 根据该宫的地支和年天干确定天干 (五虎遁)
            val stem = calculatePalaceStem(sb.yearStem, branchIdx)

            Palace(
                type = type,
                heavenlyStem = stem,
                earthlyBranch = branch
            )
        }
    }

    /**
     * 安命宫：从寅宫起正月，顺数至当前月，再逆数至当前时
     */
    private fun calculateMingPalaceBranch(lunarMonth: Int, hourBranchIdx: Int): Int {
        // 寅=2 作为起始(正月), 顺数月份
        var pos = (2 + lunarMonth - 1) % 12
        // 逆数时辰
        pos = Math.floorMod(pos - hourBranchIdx, 12)
        return pos
    }

    /**
     * 计算某宫的天干：以五虎遁为基准
     * 年干决定寅(正月)的天干，依次顺推
     */
    private fun calculatePalaceStem(yearStem: HeavenlyStem, branchIdx: Int): HeavenlyStem {
        // 五虎遁：年干决定寅(地支索引2)的天干
        val yinStemIdx = when (yearStem.index) {
            0, 5 -> 2  // 甲、己 → 丙寅
            1, 6 -> 4  // 乙、庚 → 戊寅
            2, 7 -> 6  // 丙、辛 → 庚寅
            3, 8 -> 8  // 丁、壬 → 壬寅
            4, 9 -> 0  // 戊、癸 → 甲寅
            else -> 0
        }
        // 从寅(2)到目标地支，天干顺推
        val offset = Math.floorMod(branchIdx - 2, 12)
        val stemIdx = (yinStemIdx + offset) % 10
        return HeavenlyStem.entries[stemIdx]
    }

    /**
     * 计算五行局
     * 根据命宫的天干地支推算五行局数
     * 水2局、木3局、金4局、土5局、火6局
     */
    fun calculateFiveElementBureau(mingPalaceStem: Int, mingPalaceBranch: Int): FiveElement {
        // 纳音五行推算表：天干地支配对的纳音
        return when {
            // 水二局
            mingPalaceStem in listOf(0, 5) && mingPalaceBranch in listOf(2, 3) -> FiveElement.WATER   // 甲己、寅卯
            mingPalaceStem in listOf(1, 6) && mingPalaceBranch in listOf(4, 5) -> FiveElement.WATER   // 乙庚、辰巳
            mingPalaceStem in listOf(2, 7) && mingPalaceBranch in listOf(6, 7) -> FiveElement.WATER   // 丙辛、午未
            mingPalaceStem in listOf(3, 8) && mingPalaceBranch in listOf(8, 9) -> FiveElement.WATER   // 丁壬、申酉
            mingPalaceStem in listOf(4, 9) && mingPalaceBranch in listOf(10, 11) -> FiveElement.WATER // 戊癸、戌亥
            // 木三局
            mingPalaceStem in listOf(3, 8) && mingPalaceBranch in listOf(4, 5) -> FiveElement.WOOD
            mingPalaceStem in listOf(4, 9) && mingPalaceBranch in listOf(6, 7) -> FiveElement.WOOD
            mingPalaceStem in listOf(0, 5) && mingPalaceBranch in listOf(8, 9) -> FiveElement.WOOD
            mingPalaceStem in listOf(1, 6) && mingPalaceBranch in listOf(10, 11) -> FiveElement.WOOD
            mingPalaceStem in listOf(2, 7) && mingPalaceBranch in listOf(2, 3) -> FiveElement.WOOD
            // 金四局
            mingPalaceStem in listOf(0, 5) && mingPalaceBranch in listOf(0, 1) -> FiveElement.METAL
            mingPalaceStem in listOf(1, 6) && mingPalaceBranch in listOf(2, 3) -> FiveElement.METAL
            mingPalaceStem in listOf(2, 7) && mingPalaceBranch in listOf(4, 5) -> FiveElement.METAL
            mingPalaceStem in listOf(3, 8) && mingPalaceBranch in listOf(6, 7) -> FiveElement.METAL
            mingPalaceStem in listOf(4, 9) && mingPalaceBranch in listOf(8, 9) -> FiveElement.METAL
            // 土五局
            mingPalaceStem in listOf(0, 5) && mingPalaceBranch in listOf(4, 5) -> FiveElement.EARTH
            mingPalaceStem in listOf(1, 6) && mingPalaceBranch in listOf(6, 7) -> FiveElement.EARTH
            mingPalaceStem in listOf(2, 7) && mingPalaceBranch in listOf(8, 9) -> FiveElement.EARTH
            mingPalaceStem in listOf(3, 8) && mingPalaceBranch in listOf(10, 11) -> FiveElement.EARTH
            mingPalaceStem in listOf(4, 9) && mingPalaceBranch in listOf(0, 1) -> FiveElement.EARTH
            // 火六局
            mingPalaceStem in listOf(1, 6) && mingPalaceBranch in listOf(0, 1) -> FiveElement.FIRE
            mingPalaceStem in listOf(2, 7) && mingPalaceBranch in listOf(10, 11) -> FiveElement.FIRE
            mingPalaceStem in listOf(3, 8) && mingPalaceBranch in listOf(2, 3) -> FiveElement.FIRE
            mingPalaceStem in listOf(4, 9) && mingPalaceBranch in listOf(4, 5) -> FiveElement.FIRE
            mingPalaceStem in listOf(0, 5) && mingPalaceBranch in listOf(6, 7) -> FiveElement.FIRE
            else -> FiveElement.EARTH // 默认土五局
        }
    }

    /** 五行局数 */
    fun FiveElement.bureauNumber(): Int = when (this) {
        FiveElement.WATER -> 2
        FiveElement.WOOD -> 3
        FiveElement.METAL -> 4
        FiveElement.EARTH -> 5
        FiveElement.FIRE -> 6
    }
}
