package com.nestor.aiform.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api
import com.nestor.aiform.ExerciseDefinition
import com.nestor.aiform.TrainingPlan
import com.nestor.aiform.data.SettingsRepository
import com.nestor.aiform.data.SettingsState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(navController: NavController) {
    val context = LocalContext.current
    val settingsRepo = remember { SettingsRepository(context) }
    val settings by settingsRepo.settings.collectAsState(initial = SettingsState())


    var selectedExercise by remember { mutableStateOf<ExerciseDefinition?>(null) }
    var timeLeft by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, selectedExercise) {
        if (isRunning && selectedExercise != null) {
            while (isRunning && timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1
            }
            if (timeLeft <= 0 && isRunning && selectedExercise != null) {

                // AQUÍ se aplica lo de ajustes
                if (settings.vibrationEnabled) {
                    vibrateOnce(context)
                }

                isRunning = false
            }
        }
    }

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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (selectedExercise == null) {
                Text(
                    "Selecciona un ejercicio para empezar",
                    style = MaterialTheme.typography.titleMedium
                )

                // LISTA SCROLLEABLE
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(TrainingPlan) { exercise ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedExercise = exercise
                                    timeLeft = exercise.defaultRestSeconds
                                    isRunning = true
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(exercise.name)
                                Text(
                                    "Descanso: ${exercise.defaultRestSeconds}s",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            } else {
                Text("Ejercicio actual:", style = MaterialTheme.typography.bodyLarge)
                Text(
                    selectedExercise!!.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Descanso:", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "${timeLeft}s",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedExercise?.let {
                                timeLeft = selectedExercise?.defaultRestSeconds ?: 0
                                isRunning = true
                            }
                        }
                    ) {
                        Text("Reiniciar descanso")
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedExercise?.let {
                                timeLeft = selectedExercise?.defaultRestSeconds ?: 0
                                isRunning = true
                            }
                        }
                    ) {
                        Text("Siguiente serie")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isRunning = false
                        selectedExercise = null
                        timeLeft = 0
                    }
                ) {
                    Text("Terminar ejercicio")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            }
        }
    }
}

fun vibrateOnce(context: Context, millis: Long = 300L) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                millis,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    millis,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(millis)
        }
    }
}