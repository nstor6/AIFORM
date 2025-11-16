package com.nestor.aiform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nestor.aiform.navigation.Screen
import com.nestor.aiform.ui.screens.*
import com.nestor.aiform.ui.theme.AIFORMTheme // o el nombre que te haya creado el proyecto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIFORMTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(navController)
                        }
                        composable(Screen.Dashboard.route) {
                            DashboardScreen(navController)
                        }
                        composable(Screen.Weight.route) {
                            WeightScreen(navController)
                        }
                        composable(Screen.Diet.route) {
                            DietScreen(navController)
                        }
                        composable(Screen.Training.route) {
                            TrainingScreen(navController)
                        }
                        composable(Screen.History.route) {
                            HistoryScreen(navController)
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
