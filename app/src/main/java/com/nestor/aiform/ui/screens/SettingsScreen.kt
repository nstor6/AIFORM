package com.nestor.aiform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api
import com.nestor.aiform.data.DietRepository
import com.nestor.aiform.data.SettingsRepository
import com.nestor.aiform.data.SettingsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val settingsRepo = remember { SettingsRepository(context) }
    val dietRepo = remember { DietRepository(context) }
    val scope = rememberCoroutineScope()

    val settings by settingsRepo.settings.collectAsState(initial = SettingsState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Vibración ON/OFF
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Vibración al terminar el descanso",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (settings.vibrationEnabled) "Activada" else "Desactivada",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Switch(
                    checked = settings.vibrationEnabled,
                    onCheckedChange = { enabled ->
                        scope.launch {
                            settingsRepo.setVibrationEnabled(enabled)
                        }
                    }
                )
            }

            Divider()

            Text(
                text = "Dieta",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    scope.launch {
                        dietRepo.resetDay()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reiniciar checklist de hoy")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}
