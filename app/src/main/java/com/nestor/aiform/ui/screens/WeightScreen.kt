package com.nestor.aiform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightScreen(navController: NavController) {
    var weightInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Peso corporal") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it },
                label = { Text("Peso de hoy (kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    // Aquí luego guardaremos el peso en DataStore
                },
                enabled = weightInput.isNotBlank()
            ) {
                Text("Guardar")
            }

            Button(
                onClick = { navController.popBackStack() }
            ) {
                Text("Volver")
            }
        }
    }
}
