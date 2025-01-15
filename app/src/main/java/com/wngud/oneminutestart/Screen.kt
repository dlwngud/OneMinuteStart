package com.wngud.oneminutestart

import androidx.annotation.DrawableRes

sealed class Screen(
    val route: String,
    val name: String,
    @DrawableRes val icon: Int
) {
    object TimerScreen : Screen("TimerScreen", "타이머", R.drawable.baseline_timer_24)
    object StatisticsScreen : Screen("statisticsScreen", "통계", R.drawable.baseline_bar_chart_24)
    object SettingScreen : Screen("settingScreen", "설정", R.drawable.baseline_settings_24)
    object OneMinuteScreen : Screen("oneMinuteScreenScreen/{id}", "", -1) {
        fun createRoute(id: Long) = "oneMinuteScreenScreen/$id"
    }
}