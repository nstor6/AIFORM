package com.nestor.aiform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nestor.aiform.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AIFORM · Panel") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¿Qué quieres registrar hoy?", style = MaterialTheme.typography.titleLarge)

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.Weight.route) }
            ) {
                Text("Peso corporal")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.Diet.route) }
            ) {
                Text("Dieta · Checklists")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.Training.route) }
            ) {
                Text("Entrenamiento")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.History.route) }
            ) {
                Text("Historial de peso")
            }
        }
    }
}
