package com.nestor.aiform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nestor.aiform.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "AIFORM", style = MaterialTheme.typography.headlineLarge)
                Text(text = "Versión 0.1 · Acceso rápido")

                Button(
                    onClick = { navController.navigate(Screen.Dashboard.route) }
                ) {
                    Text("Entrar")
                }
            }
        }
    }
}
