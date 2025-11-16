package com.nestor.aiform.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Weight : Screen("weight")
    object Diet : Screen("diet")
    object Training : Screen("training")
    object History : Screen("history")
    object Settings : Screen("settings")
}
