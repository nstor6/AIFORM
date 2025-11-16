package com.nestor.aiform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api
import com.nestor.aiform.data.DietChecklistState
import com.nestor.aiform.data.DietRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = remember { DietRepository(context) }
    val scope = rememberCoroutineScope()

    val dietState by repo.dietState.collectAsState(initial = DietChecklistState())

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
                Checkbox(
                    checked = dietState.breakfastDone,
                    onCheckedChange = { checked ->
                        scope.launch { repo.setBreakfast(checked) }
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comida")
                Checkbox(
                    checked = dietState.lunchDone,
                    onCheckedChange = { checked ->
                        scope.launch { repo.setLunch(checked) }
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Merienda")
                Checkbox(
                    checked = dietState.snackDone,
                    onCheckedChange = { checked ->
                        scope.launch { repo.setSnack(checked) }
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cena")
                Checkbox(
                    checked = dietState.dinnerDone,
                    onCheckedChange = { checked ->
                        scope.launch { repo.setDinner(checked) }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}