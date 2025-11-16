package com.nestor.aiform

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = remember { WeightRepository(context) }
    val scope = rememberCoroutineScope()

    var weightInput by remember { mutableStateOf("") }
    val lastWeight by repo.lastWeight.collectAsState(initial = null)

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
            if (lastWeight != null) {
                Text(
                    text = "Último peso guardado: ${"%.1f".format(lastWeight)} kg",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "Todavía no has guardado ningún peso.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it },
                label = { Text("Peso de hoy (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val w = weightInput.replace(",", ".").toFloatOrNull()
                    if (w != null) {
                        scope.launch {
                            repo.saveWeight(w)
                            weightInput = ""
                        }
                    }
                },
                enabled = weightInput.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}
