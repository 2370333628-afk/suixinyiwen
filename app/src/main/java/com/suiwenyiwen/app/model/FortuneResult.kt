package com.suiwenyiwen.app.model

/**
 * 单个宫位的解读
 */
data class PalaceReading(
    val palaceType: PalaceType,
    val stemBranch: String,
    val majorStars: List<String>,
    val auxiliaryStars: List<String>,
    val fortuneLevel: FortuneLevel,
    val mainAnalysis: String,
    val starAnalysis: String,
    val advice: String
)

/**
 * 完整解读结果
 */
data class FortuneResult(
    val overallFortune: FortuneLevel,
    val overallSummary: String,
    val palaceReadings: List<PalaceReading>,
    val keyStars: List<String>,           // 关键影响星曜
    val favorableDirections: List<String>, // 有利方向/领域
    val cautions: List<String>,            // 注意事项
    val generalAdvice: String,             // 综合建议
    val queryTimeDescription: String       // 时间描述
)
