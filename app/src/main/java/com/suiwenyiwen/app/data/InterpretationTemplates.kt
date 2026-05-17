package com.suiwenyiwen.app.data

import com.suiwenyiwen.app.model.*

/**
 * 解读文本模板库
 * 根据星曜组合和宫位生成详细的解读文本
 */
object InterpretationTemplates {

    /**
     * 获取宫位分析文本
     */
    fun getPalaceAnalysis(
        palaceType: PalaceType,
        majorStars: List<String>,
        auxStars: List<String>,
        transformations: List<Transformation>,
        fortuneLevel: FortuneLevel
    ): String {
        val parts = mutableListOf<String>()

        // 宫位基础含义
        parts.add(getPalaceBaseMeaning(palaceType))

        // 主星解读
        if (majorStars.isNotEmpty()) {
            parts.add(getMajorStarInterpretation(palaceType, majorStars))
        } else {
            parts.add("此宫无主星坐守，借对宫之力，需参考对宫星曜来判断吉凶。")
        }

        // 辅星影响
        if (auxStars.isNotEmpty()) {
            parts.add(getAuxStarInterpretation(auxStars))
        }

        // 四化影响
        if (transformations.isNotEmpty()) {
            parts.add(getTransformationInterpretation(transformations, majorStars.firstOrNull()))
        }

        // 宫位专项分析
        parts.add(getPalaceSpecificAnalysis(palaceType, fortuneLevel, majorStars))

        return parts.joinToString("\n\n")
    }

    /**
     * 获取宫位建议
     */
    fun getPalaceAdvice(
        palaceType: PalaceType,
        fortuneLevel: FortuneLevel,
        majorStars: List<String>
    ): String {
        return when (fortuneLevel) {
            FortuneLevel.VERY_GOOD -> getVeryGoodAdvice(palaceType)
            FortuneLevel.GOOD -> getGoodAdvice(palaceType)
            FortuneLevel.NEUTRAL -> getNeutralAdvice(palaceType)
            FortuneLevel.BAD -> getBadAdvice(palaceType)
            FortuneLevel.VERY_BAD -> getVeryBadAdvice(palaceType)
        }
    }

    private fun getPalaceBaseMeaning(type: PalaceType): String = when (type) {
        PalaceType.MING -> "命宫为十二宫之首，主宰一生运势根基与个性特质，是决定所问之事成败的核心所在。"
        PalaceType.BROTHERS -> "兄弟宫主手足情谊与平辈人际关系，也象征合作竞争之势。"
        PalaceType.SPOUSE -> "夫妻宫主婚姻感情、两性关系，也反映合作契约的吉凶。"
        PalaceType.CHILDREN -> "子女宫主子嗣缘分与晚辈关系，亦主创意表达与享乐之事。"
        PalaceType.WEALTH -> "财帛宫主一生财运得失与经济状况，关乎所问之事的物质基础。"
        PalaceType.HEALTH -> "疾厄宫主身体健康与灾祸意外，也反映心理压力与隐忧。"
        PalaceType.TRAVEL -> "迁移宫主外出运势与人生变迁，涉及出行、远行与环境变动。"
        PalaceType.FRIENDS -> "交友宫主朋友交际与社会关系，反映人际助力或拖累。"
        PalaceType.CAREER -> "官禄宫主事业功名与学业成就，是判断所问之事能否成功的重要参考。"
        PalaceType.PROPERTY -> "田宅宫主家庭住宅与不动产，也象征内心安全感与归属。"
        PalaceType.FORTUNE -> "福德宫主精神享受与福缘果报，关乎心态和人生格局。"
        PalaceType.PARENTS -> "父母宫主长辈缘分与上司关系，也象征天时庇护之力。"
    }

    private fun getMajorStarInterpretation(
        palaceType: PalaceType,
        stars: List<String>
    ): String {
        val interpretations = stars.map { star ->
            when (star) {
                "紫微" -> "$star 帝星坐守，主贵气加身，有利官禄事业，求事多得贵人相助。"
                "天机" -> "$star 智星当值，思虑周详，适合策划谋略，但需防举棋不定。"
                "太阳" -> "$star 中天照耀，光明磊落，适合公开事务，得人信赖。"
                "武曲" -> "$star 财星在位，执行有力，适合实干求财，但需防刚愎自用。"
                "天同" -> "$star 福星高照，心境平和，所求之事若能顺其自然，反有意外之喜。"
                "廉贞" -> "$star 刚星守宫，性情刚烈，行事果断，但易因固执而失机。"
                "天府" -> "$star 库星镇守，稳重可靠，利于积累和长期规划。"
                "太阴" -> "$star 月华内敛，柔中带刚，利于内务和隐性事务。"
                "贪狼" -> "$star 才星入位，多才多艺，但需防贪多嚼不烂。"
                "巨门" -> "$star 暗星临宫，口舌易生，谨防是非，言语需格外谨慎。"
                "天相" -> "$star 印星护持，忠实可靠，宜做辅助角色，不宜独当一面。"
                "天梁" -> "$star 寿星加持，有长者庇护，遇事可化险为夷。"
                "七杀" -> "$star 将星当道，魄力十足，适合开拓进取，但须防过度冒进。"
                "破军" -> "$star 破星在位，充满变数，适合破旧立新，但风险与机遇并存。"
                else -> "$star 临此宫，需结合全盘分析其具体影响。"
            }
        }
        return interpretations.joinToString(" ")
    }

    private fun getAuxStarInterpretation(auxStars: List<String>): String {
        val auspicious = auxStars.filter { it in listOf("左辅", "右弼", "文昌", "文曲", "天魁", "天钺", "禄存", "天马") }
        val inauspicious = auxStars.filter { it in listOf("擎羊", "陀罗", "火星", "铃星", "地空", "地劫") }

        val parts = mutableListOf<String>()

        if (auspicious.isNotEmpty()) {
            parts.add(
                when {
                    "左辅" in auspicious || "右弼" in auspicious -> "得辅弼相助，人际关系和谐，有贵人从旁协助。"
                    "文昌" in auspicious || "文曲" in auspicious -> "文星加持，利于文书考学，口才表达有所提升。"
                    "天魁" in auspicious || "天钺" in auspicious -> "魁钺贵人星照，有长辈或上级提携之助。"
                    "禄存" in auspicious -> "禄存星照，财运基础稳固，不易破财。"
                    "天马" in auspicious -> "天马星动，利于出行变动，远方发展有机遇。"
                    else -> ""
                }
            )
        }

        if (inauspicious.isNotEmpty()) {
            parts.add(
                when {
                    "擎羊" in inauspicious || "陀罗" in inauspicious -> "羊陀夹制，易有阻碍和伤害，需防意外之事。"
                    "火星" in inauspicious || "铃星" in inauspicious -> "火铃煞星临宫，突发变故多，需时刻保持警惕。"
                    "地空" in inauspicious || "地劫" in inauspicious -> "空劫星扰，计划容易落空，需多做备选方案。"
                    else -> ""
                }
            )
        }

        return parts.joinToString(" ")
    }

    private fun getTransformationInterpretation(
        transformations: List<Transformation>,
        primaryStar: String?
    ): String {
        return transformations.joinToString(" ") { trans ->
            val starRef = primaryStar ?: "主星"
            when (trans) {
                Transformation.HUA_LU -> "$starRef 化禄，财运亨通，所求之事多得实惠。"
                Transformation.HUA_QUAN -> "$starRef 化权，掌握主动权，有能力掌控局面。"
                Transformation.HUA_KE -> "$starRef 化科，名声显扬，利于考试和公开事务。"
                Transformation.HUA_JI -> "$starRef 化忌，此为凶兆，诸事多有波折，需格外谨慎。"
            }
        }
    }

    private fun getPalaceSpecificAnalysis(
        palaceType: PalaceType,
        fortuneLevel: FortuneLevel,
        majorStars: List<String>
    ): String {
        return when (palaceType) {
            PalaceType.MING -> when (fortuneLevel) {
                FortuneLevel.VERY_GOOD -> "命盘根基深厚，当前天时极佳，所问之事成功概率极高，宜果断行动。"
                FortuneLevel.GOOD -> "命宫旺相，所问之事有较大把握，只要用心经营，可望有成。"
                FortuneLevel.NEUTRAL -> "命宫平稳，所问之事结果可能喜忧参半，需权衡利弊再做决断。"
                FortuneLevel.BAD -> "命宫受克，所问之事阻力较大，建议暂缓或调整方案。"
                FortuneLevel.VERY_BAD -> "命宫被凶星压制，当前不宜有所行动，建议静候时机。"
            }
            PalaceType.WEALTH -> when (fortuneLevel) {
                FortuneLevel.VERY_GOOD -> "财运亨通，适合投资理财，所问之事经济效益可观。"
                FortuneLevel.GOOD -> "财星明亮，收入稳定可期，花费有理有据。"
                FortuneLevel.NEUTRAL -> "财运平稳，收支大致平衡，不宜大额投资。"
                FortuneLevel.BAD -> "财帛有损，慎防破财，投资需格外谨慎。"
                FortuneLevel.VERY_BAD -> "财星黯淡，当前不宜涉及金钱事务，以防不测之损。"
            }
            PalaceType.CAREER -> when (fortuneLevel) {
                FortuneLevel.VERY_GOOD -> "官禄昌隆，事业如意，适合提出新的计划和项目。"
                FortuneLevel.GOOD -> "事业运佳，工作顺利推进，有得到认可的机会。"
                FortuneLevel.NEUTRAL -> "事业平稳，按部就班即可，暂不宜有大的变动。"
                FortuneLevel.BAD -> "事业受阻，需防小人作祟，凡事多留余地。"
                FortuneLevel.VERY_BAD -> "官禄有损，事业可能遭遇重大挫折，宜低调忍耐。"
            }
            PalaceType.SPOUSE -> when (fortuneLevel) {
                FortuneLevel.VERY_GOOD -> "感情和美，与伴侣关系融洽，所问感情之事大有希望。"
                FortuneLevel.GOOD -> "夫妻宫吉，情感方面较为顺遂，可望有好的发展。"
                FortuneLevel.NEUTRAL -> "感情平稳，无大风浪亦无大惊喜，平淡即是福。"
                FortuneLevel.BAD -> "感情易生变数，需用心维护，切忌冲动行事。"
                FortuneLevel.VERY_BAD -> "夫妻宫凶，感情可能出现危机，需冷静处理。"
            }
            else -> when (fortuneLevel) {
                FortuneLevel.VERY_GOOD -> "此宫旺相大吉，所涉领域一片光明。"
                FortuneLevel.GOOD -> "此宫运势良好，可顺势而为。"
                FortuneLevel.NEUTRAL -> "此宫运势平稳，宜守不宜攻。"
                FortuneLevel.BAD -> "此宫欠吉，所涉方面需多加小心。"
                FortuneLevel.VERY_BAD -> "此宫大凶，相关事务能避则避。"
            }
        }
    }

    // 建议模板
    private fun getVeryGoodAdvice(type: PalaceType): String = when (type) {
        PalaceType.MING -> "大好时机，当勇往直前，莫失良机。"
        PalaceType.WEALTH -> "财运极佳，可适当加大投入，但也不可盲目乐观。"
        PalaceType.CAREER -> "事业黄金期，大胆施展才华，争取更大成就。"
        PalaceType.SPOUSE -> "感情浓情蜜意，适合表白或加深关系。"
        PalaceType.HEALTH -> "身心状态极佳，可适当增加运动量。"
        else -> "此领域大吉，应积极把握。"
    }

    private fun getGoodAdvice(type: PalaceType): String = when (type) {
        PalaceType.MING -> "运势向好，付诸行动之前宜做好规划。"
        PalaceType.WEALTH -> "财运不错，量入为出，可有盈余。"
        PalaceType.CAREER -> "工作顺遂，保持节奏即可取得好成绩。"
        PalaceType.SPOUSE -> "感情温暖，多些陪伴和交流会更美满。"
        else -> "此领域尚好，稳扎稳打即可。"
    }

    private fun getNeutralAdvice(type: PalaceType): String = when (type) {
        PalaceType.MING -> "运势平平，不宜冒进，静待转机。"
        PalaceType.WEALTH -> "财运一般，维持现状，不宜冒险。"
        PalaceType.CAREER -> "事业稳定，适合巩固已有成果。"
        else -> "暂守为佳，伺机而动。"
    }

    private fun getBadAdvice(type: PalaceType): String = when (type) {
        PalaceType.MING -> "运势低迷，万事小心，不宜轻举妄动。"
        PalaceType.WEALTH -> "慎防破财，暂停投资，保守理财。"
        PalaceType.CAREER -> "事业有阻，低调行事，避免冲突。"
        PalaceType.HEALTH -> "注意身体，避免劳累过度。"
        else -> "此领域欠佳，宜做减法。"
    }

    private fun getVeryBadAdvice(type: PalaceType): String = when (type) {
        PalaceType.MING -> "大凶之兆，此时不宜做任何重大决策，宜静不宜动。"
        PalaceType.WEALTH -> "财运极差，谨防损失，一切财务活动暂停为上。"
        PalaceType.CAREER -> "事业危机，忍耐为上，切勿与人冲突。"
        PalaceType.SPOUSE -> "感情有险，不要做重大感情决定。"
        PalaceType.HEALTH -> "健康亮红灯，及早就医检查。"
        else -> "此领域大凶，能避则避。"
    }
}
