package com.nestor.aiform

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear el DataStore
val Context.weightDataStore by preferencesDataStore(name = "weight_prefs")

// Clave para el peso
private val LAST_WEIGHT_KEY = floatPreferencesKey("last_weight")

class WeightRepository(private val context: Context) {

    val lastWeight: Flow<Float?> = context.weightDataStore.data.map { prefs ->
        if (prefs.contains(LAST_WEIGHT_KEY)) prefs[LAST_WEIGHT_KEY] else null
    }

    suspend fun saveWeight(weight: Float) {
        context.weightDataStore.edit { prefs ->
            prefs[LAST_WEIGHT_KEY] = weight
        }
    }
}
