package com.nestor.aiform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api
import com.nestor.aiform.DailyMeals
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
            DailyMeals.forEach { meal ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(meal.label)

                    val checked = when (meal.id) {
                        "breakfast" -> dietState.breakfastDone
                        "lunch" -> dietState.lunchDone
                        "snack" -> dietState.snackDone
                        "dinner" -> dietState.dinnerDone
                        else -> false
                    }

                    Checkbox(
                        checked = checked,
                        onCheckedChange = { value ->
                            scope.launch {
                                when (meal.id) {
                                    "breakfast" -> repo.setBreakfast(value)
                                    "lunch" -> repo.setLunch(value)
                                    "snack" -> repo.setSnack(value)
                                    "dinner" -> repo.setDinner(value)
                                }
                            }
                        }
                    )
                }
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