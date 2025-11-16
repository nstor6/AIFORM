package com.nestor.aiform.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(navController: NavController) {
    val exercises = listOf(
        "Press banca",
        "Remo con barra",
        "Peso muerto rumano",
        "Sentadilla",
        "Press militar",
        "Curl bíceps",
        "Extensión tríceps",
        "Abdominales"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrenamiento") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Selecciona un ejercicio para empezar", style = MaterialTheme.typography.titleMedium)

            exercises.forEach { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Aquí meteremos el timer de descanso
                        }
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text(exercise)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    }
}
