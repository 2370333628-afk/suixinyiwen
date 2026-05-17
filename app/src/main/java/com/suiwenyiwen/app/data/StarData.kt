package com.suiwenyiwen.app.data

import com.suiwenyiwen.app.model.*

/**
 * 星曜属性数据库
 */
object StarData {

    /**
     * 十四主星完整属性
     */
    val MAJOR_STAR_PROPERTIES: Map<String, Star> = mapOf(
        "紫微" to Star("紫微", StarCategory.MAJOR, FiveElement.EARTH, StarNature.AUSPICIOUS,
            "紫微为帝星，北斗之首，司掌官禄，主贵气与权威。入命宫则气质高贵，领导力强。"),
        "天机" to Star("天机", StarCategory.MAJOR, FiveElement.WOOD, StarNature.ADAPTIVE,
            "天机为谋星，南斗第三星，主智慧谋略，思维敏捷。善变通，适合策划与参谋之职。"),
        "太阳" to Star("太阳", StarCategory.MAJOR, FiveElement.FIRE, StarNature.AUSPICIOUS,
            "太阳为中天主星，光明正大，主博爱、热情与公权力。日生人得之更吉，夜生则减色。"),
        "武曲" to Star("武曲", StarCategory.MAJOR, FiveElement.METAL, StarNature.ADAPTIVE,
            "武曲为财星，北斗第六星，主刚毅果断，善理财。性格耿直，执行力强。"),
        "天同" to Star("天同", StarCategory.MAJOR, FiveElement.WATER, StarNature.AUSPICIOUS,
            "天同为福星，南斗第四星，主温和谦让，知足常乐。性情平和，不喜争斗。"),
        "廉贞" to Star("廉贞", StarCategory.MAJOR, FiveElement.FIRE, StarNature.ADAPTIVE,
            "廉贞为囚星，北斗第五星，主执法公正，但也易陷固执。性情刚烈，爱憎分明。"),
        "天府" to Star("天府", StarCategory.MAJOR, FiveElement.EARTH, StarNature.AUSPICIOUS,
            "天府为库星，南斗之首，主储藏与包容。性格稳重，善于管理，有容人之量。"),
        "太阴" to Star("太阴", StarCategory.MAJOR, FiveElement.WATER, StarNature.AUSPICIOUS,
            "太阴为中天月星，主柔美、内敛与富足。性情温婉，直觉敏锐，夜生人更吉。"),
        "贪狼" to Star("贪狼", StarCategory.MAJOR, FiveElement.WOOD.also { FiveElement.WATER },
            StarNature.ADAPTIVE, "贪狼为桃花星，北斗第一星，主欲望与才艺。多才多艺但需防贪欲过度。"),
        "巨门" to Star("巨门", StarCategory.MAJOR, FiveElement.WATER, StarNature.INAUSPICIOUS,
            "巨门为暗星，北斗第二星，主口舌是非。擅长辩论，但需防言辞伤人。"),
        "天相" to Star("天相", StarCategory.MAJOR, FiveElement.WATER, StarNature.AUSPICIOUS,
            "天相为印星，南斗第五星，主辅佐与服务。为人忠实可靠，适合幕后工作。"),
        "天梁" to Star("天梁", StarCategory.MAJOR, FiveElement.EARTH, StarNature.AUSPICIOUS,
            "天梁为寿星，南斗第二星，主长寿与庇荫。有长者之风，乐于助人。"),
        "七杀" to Star("七杀", StarCategory.MAJOR, FiveElement.METAL, StarNature.INAUSPICIOUS,
            "七杀为将星，南斗第六星，主杀伐果断。勇猛刚强，但需防冲动伤身。"),
        "破军" to Star("破军", StarCategory.MAJOR, FiveElement.WATER, StarNature.INAUSPICIOUS,
            "破军为耗星，北斗第七星，主破旧立新。敢于突破，但需防过于激进。")
    )

    /**
     * 辅星属性
     */
    val AUXILIARY_STAR_PROPERTIES: Map<String, Star> = mapOf(
        "左辅" to Star("左辅", StarCategory.AUXILIARY, FiveElement.EARTH, StarNature.AUSPICIOUS,
            "左辅为助星，主得贵人扶持，平辈相助。"),
        "右弼" to Star("右弼", StarCategory.AUXILIARY, FiveElement.WATER, StarNature.AUSPICIOUS,
            "右弼为助星，主暗中得助，化解困难。"),
        "文昌" to Star("文昌", StarCategory.AUXILIARY, FiveElement.METAL, StarNature.AUSPICIOUS,
            "文昌主文采学业，利考试与文书事务。"),
        "文曲" to Star("文曲", StarCategory.AUXILIARY, FiveElement.WATER, StarNature.AUSPICIOUS,
            "文曲主才艺口才，擅长表达与艺术。"),
        "天魁" to Star("天魁", StarCategory.AUXILIARY, FiveElement.FIRE, StarNature.AUSPICIOUS,
            "天魁为贵人星，主得长辈提携，逢凶化吉。"),
        "天钺" to Star("天钺", StarCategory.AUXILIARY, FiveElement.FIRE, StarNature.AUSPICIOUS,
            "天钺为贵人星，主得女性贵人相助。"),
        "禄存" to Star("禄存", StarCategory.AUXILIARY, FiveElement.EARTH, StarNature.AUSPICIOUS,
            "禄存主财禄丰足，衣食无忧。"),
        "天马" to Star("天马", StarCategory.AUXILIARY, FiveElement.FIRE, StarNature.AUSPICIOUS,
            "天马主奔波变动，利于出行与远方发展。"),
        "擎羊" to Star("擎羊", StarCategory.AUXILIARY, FiveElement.METAL, StarNature.INAUSPICIOUS,
            "擎羊为刑星，主争执伤害，需防意外。"),
        "陀罗" to Star("陀罗", StarCategory.AUXILIARY, FiveElement.METAL, StarNature.INAUSPICIOUS,
            "陀罗为忌星，主拖延阻滞，事情难成。"),
        "火星" to Star("火星", StarCategory.AUXILIARY, FiveElement.FIRE, StarNature.INAUSPICIOUS,
            "火星为煞星，主突发变故，来势凶猛。"),
        "铃星" to Star("铃星", StarCategory.AUXILIARY, FiveElement.FIRE, StarNature.INAUSPICIOUS,
            "铃星为煞星，主暗中破坏，防不胜防。"),
        "地空" to Star("地空", StarCategory.AUXILIARY, FiveElement.EARTH, StarNature.INAUSPICIOUS,
            "地空主落空失望，计划容易落空。"),
        "地劫" to Star("地劫", StarCategory.AUXILIARY, FiveElement.EARTH, StarNature.INAUSPICIOUS,
            "地劫主损失劫难，财物流失。")
    )

    /** 根据名称获取星曜信息 */
    fun getStarInfo(name: String): Star? {
        return MAJOR_STAR_PROPERTIES[name] ?: AUXILIARY_STAR_PROPERTIES[name]
    }
}
