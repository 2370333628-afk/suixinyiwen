package com.suiwenyiwen.app.model

/**
 * 星曜类别
 */
enum class StarCategory {
    MAJOR,      // 十四主星
    AUXILIARY,  // 辅星
    TRANSFORM   // 四化
}

/**
 * 五行属性
 */
enum class FiveElement(val display: String) {
    WOOD("木"),
    FIRE("火"),
    EARTH("土"),
    METAL("金"),
    WATER("水")
}

/**
 * 吉凶等级
 */
enum class FortuneLevel(val display: String, val score: Int) {
    VERY_GOOD("大吉", 5),
    GOOD("吉", 4),
    NEUTRAL("平", 3),
    BAD("凶", 2),
    VERY_BAD("大凶", 1)
}

/**
 * 星曜定义
 */
data class Star(
    val name: String,
    val category: StarCategory,
    val element: FiveElement,
    val nature: StarNature,  // 吉凶属性
    val description: String
)

enum class StarNature(val display: String) {
    AUSPICIOUS("吉"),
    NEUTRAL("平"),
    INAUSPICIOUS("凶"),
    ADAPTIVE("随化")
}
