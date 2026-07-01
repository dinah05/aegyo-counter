package com.aegyocounter.app

data class CountState(
    val number: Int = 0,

    val todayBest: Int = 0,
    val weekBest: Int = 0,
    val allBest: Int = 0,

    val catMessage: String = "호에엥...",
    val girlMessage: String = "오늘은 조용하네.",

    val showAchievementDialog: Boolean = false,
    val achievementCount: Int = 0,

    val achievementTitle: String = "",
    val achievementDescription: String = ""
)