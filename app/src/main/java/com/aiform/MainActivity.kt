package com.aiform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.ZoneId

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AiformApp() }
    }
}

@Composable
fun AiformApp(vm: MainViewModel = viewModel()) {
    val screen by vm.screen.collectAsState()
    val darkMode by vm.darkMode.collectAsState()
    MaterialTheme(colorScheme = if (darkMode) androidx.compose.material3.darkColorScheme() else androidx.compose.material3.lightColorScheme()) {
        when (screen) {
            Screen.Dashboard -> Dashboard(vm)
            Screen.Guided -> GuidedSessionScreen(vm)
            Screen.Settings -> SettingsScreen(vm)
        }
    }
}

@Composable
private fun Dashboard(vm: MainViewModel) {
    val plan by vm.plan.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dashboard", style = MaterialTheme.typography.headlineSmall)
        Text("Plan: ${plan?.workout?.title ?: "No cargado"}")
        Button(onClick = vm::refreshPlan) { Text("Actualizar plan") }
        Button(onClick = vm::startTraining, enabled = plan != null) { Text("Iniciar entrenamiento") }
        Button(onClick = { vm.navigate(Screen.Settings) }) { Text("Ajustes") }
        Button(onClick = vm::closeDayManually) { Text("Cerrar día") }
    }
}

@Composable
private fun GuidedSessionScreen(vm: MainViewModel) {
    val plan by vm.plan.collectAsState()
    val guided by vm.guided.collectAsState()
    var observationText by remember { mutableStateOf("") }

    val exercise = plan?.workout?.exercises?.getOrNull(guided.exerciseIndex)
    val set = exercise?.sets?.getOrNull(guided.setIndex)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Entrenamiento guiado", style = MaterialTheme.typography.headlineSmall)
        Text("Ejercicio: ${exercise?.name ?: "-"}")
        Text("Serie ${guided.setIndex + 1} / ${(exercise?.sets?.size ?: 0)}")
        Text("Objetivo reps: ${set?.targetReps ?: "-"}")
        Text("Objetivo peso: ${set?.targetWeightKg ?: "-"} kg")
        if (guided.restRemainingSec > 0) {
            Card { Text("Descanso: ${guided.restRemainingSec}s", modifier = Modifier.padding(12.dp)) }
        }
        Button(onClick = vm::markSetDone, enabled = exercise != null && guided.restRemainingSec == 0) {
            Text("Hecho")
        }
        Button(onClick = vm::finishTraining) { Text("Finalizar entrenamiento") }
    }

    if (guided.showObservationDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Observaciones para IA") },
            text = {
                OutlinedTextField(
                    value = observationText,
                    onValueChange = { observationText = it },
                    label = { Text("Nota") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.submitObservation(observationText.ifBlank { null })
                    observationText = ""
                }) { Text("Guardar y seguir") }
            },
            dismissButton = {
                TextButton(onClick = {
                    vm.submitObservation(null)
                    observationText = ""
                }) { Text("Saltar") }
            }
        )
    }
}

@Composable
private fun SettingsScreen(vm: MainViewModel) {
    val darkMode by vm.darkMode.collectAsState()
    val topZones = listOf(
        "Europe/Madrid",
        "America/Mexico_City",
        "America/New_York",
        "America/Bogota",
        "America/Buenos_Aires",
        "UTC",
        "Europe/London",
        "Asia/Tokyo",
        "Asia/Dubai",
        "Australia/Sydney"
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Ajustes", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { vm.navigate(Screen.Dashboard) }) { Text("Volver") }

        Text("Dark mode")
        Switch(checked = darkMode, onCheckedChange = vm::toggleDarkMode)

        Text("Zona horaria")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(topZones) { zone ->
                Button(onClick = { vm.setSelectedTimeZone(zone) }) { Text(zone) }
            }
            items(ZoneId.getAvailableZoneIds().sorted().take(30)) { zone ->
                TextButton(onClick = { vm.setSelectedTimeZone(zone) }) { Text(zone) }
            }
        }
    }
}
