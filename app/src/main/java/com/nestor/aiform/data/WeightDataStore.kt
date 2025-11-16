package com.nestor.aiform.data

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear el DataStore
val Context.weightDataStore by preferencesDataStore(name = "weight_prefs")

// Claves
private val LAST_WEIGHT_KEY = floatPreferencesKey("last_weight")
private val HISTORY_KEY = stringPreferencesKey("weight_history")

// Entrada de historial
data class WeightEntry(
    val timestamp: Long,
    val weight: Float
)

class WeightRepository(private val context: Context) {

    // Último peso
    val lastWeight: Flow<Float?> = context.weightDataStore.data.map { prefs ->
        if (prefs.contains(LAST_WEIGHT_KEY)) prefs[LAST_WEIGHT_KEY] else null
    }

    // Historial de pesos
    val history: Flow<List<WeightEntry>> = context.weightDataStore.data.map { prefs ->
        val raw = prefs[HISTORY_KEY] ?: ""
        if (raw.isBlank()) {
            emptyList()
        } else {
            raw.split("|")
                .mapNotNull { token ->
                    val parts = token.split(";")
                    if (parts.size != 2) return@mapNotNull null
                    val ts = parts[0].toLongOrNull()
                    val w = parts[1].toFloatOrNull()
                    if (ts != null && w != null) WeightEntry(ts, w) else null
                }
                .sortedByDescending { it.timestamp } // del más reciente al más antiguo
        }
    }

    // Guardar nuevo peso (actualiza último + añade al historial)
    suspend fun saveWeight(weight: Float) {
        val now = System.currentTimeMillis()

        context.weightDataStore.edit { prefs ->
            // último peso
            prefs[LAST_WEIGHT_KEY] = weight

            // historial
            val old = prefs[HISTORY_KEY] ?: ""
            val newEntry = "$now;$weight"
            val updated = if (old.isBlank()) newEntry else "$old|$newEntry"

            prefs[HISTORY_KEY] = updated
        }
    }
}