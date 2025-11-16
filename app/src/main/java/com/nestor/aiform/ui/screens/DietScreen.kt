package com.nestor.aiform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen(navController: NavController) {
    var breakfastDone by remember { mutableStateOf(false) }
    var lunchDone by remember { mutableStateOf(false) }
    var snackDone by remember { mutableStateOf(false) }
    var dinnerDone by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dieta · Checklists") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Desayuno")
                Checkbox(checked = breakfastDone, onCheckedChange = { breakfastDone = it })
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comida")
                Checkbox(checked = lunchDone, onCheckedChange = { lunchDone = it })
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Merienda")
                Checkbox(checked = snackDone, onCheckedChange = { snackDone = it })
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cena")
                Checkbox(checked = dinnerDone, onCheckedChange = { dinnerDone = it })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    }
}
