package com.suiwenyiwen.app.model

/**
 * 十二宫位
 */
enum class PalaceType(val display: String, val order: Int) {
    MING("命宫", 0),
    BROTHERS("兄弟", 1),
    SPOUSE("夫妻", 2),
    CHILDREN("子女", 3),
    WEALTH("财帛", 4),
    HEALTH("疾厄", 5),
    TRAVEL("迁移", 6),
    FRIENDS("交友", 7),
    CAREER("官禄", 8),
    PROPERTY("田宅", 9),
    FORTUNE("福德", 10),
    PARENTS("父母", 11)
}

/**
 * 天干
 */
enum class HeavenlyStem(val display: String, val index: Int) {
    JIA("甲", 0),
    YI("乙", 1),
    BING("丙", 2),
    DING("丁", 3),
    WU("戊", 4),
    JI("己", 5),
    GENG("庚", 6),
    XIN("辛", 7),
    REN("壬", 8),
    GUI("癸", 9)
}

/**
 * 地支
 */
enum class EarthlyBranch(val display: String, val index: Int) {
    ZI("子", 0),
    CHOU("丑", 1),
    YIN("寅", 2),
    MAO("卯", 3),
    CHEN("辰", 4),
    SI("巳", 5),
    WU("午", 6),
    WEI("未", 7),
    SHEN("申", 8),
    YOU("酉", 9),
    XU("戌", 10),
    HAI("亥", 11)
}

/**
 * 四化
 */
enum class Transformation(val display: String) {
    HUA_LU("化禄"),
    HUA_QUAN("化权"),
    HUA_KE("化科"),
    HUA_JI("化忌")
}

/**
 * 单个宫位包含的数据
 */
data class Palace(
    val type: PalaceType,
    val heavenlyStem: HeavenlyStem,
    val earthlyBranch: EarthlyBranch,
    val majorStars: List<String> = emptyList(),
    val auxiliaryStars: List<String> = emptyList(),
    val transformations: List<Transformation> = emptyList()
) {
    val fullName: String get() = "${type.display}宫"
    val stemBranchDisplay: String get() = "${heavenlyStem.display}${earthlyBranch.display}"
}
