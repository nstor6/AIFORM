package com.nestor.aiform.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedExercise by remember { mutableStateOf<String?>(null) }
    var timeLeft by remember { mutableStateOf(0) }      // segundos restantes
    var isRunning by remember { mutableStateOf(false) }

    // Cada vez que el timer está en marcha, contamos hacia atrás
    LaunchedEffect(isRunning, selectedExercise) {
        if (isRunning && selectedExercise != null) {
            while (isRunning && timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1
            }
            if (timeLeft <= 0 && isRunning && selectedExercise != null) {
                // Vibrar al terminar el descanso
                vibrateOnce(context)
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

                exercises.forEach { exercise ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedExercise = exercise
                                timeLeft = 60   // por ejemplo 60s de descanso inicial
                                isRunning = true
                            }
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(exercise)
                        }
                    }
                }
            } else {
                Text(
                    "Ejercicio actual:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    selectedExercise ?: "",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Descanso:",
                    style = MaterialTheme.typography.bodyLarge
                )
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
                            // Reinicia el descanso para la misma serie
                            timeLeft = 60
                            isRunning = true
                        }
                    ) {
                        Text("Reiniciar descanso")
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // Siguiente serie: mismo ejercicio, nuevo descanso
                            timeLeft = 60
                            isRunning = true
                        }
                    ) {
                        Text("Siguiente serie")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Terminar ejercicio y volver a la lista
                        isRunning = false
                        selectedExercise = null
                        timeLeft = 0
                    }
                ) {
                    Text("Terminar ejercicio")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

// Helper para vibrar
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