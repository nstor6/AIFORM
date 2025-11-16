package com.nestor.aiform.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore para ajustes
val Context.settingsDataStore by preferencesDataStore(name = "settings_prefs")

private val VIBRATION_ENABLED_KEY = booleanPreferencesKey("vibration_enabled")

data class SettingsState(
    val restDurationSeconds: Int = 60,
    val vibrationEnabled: Boolean = true
)

class SettingsRepository(private val context: Context) {

    val settings: Flow<SettingsState> = context.settingsDataStore.data.map { prefs ->
        SettingsState(
            vibrationEnabled = prefs[VIBRATION_ENABLED_KEY] ?: true
        )
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[VIBRATION_ENABLED_KEY] = enabled
        }
    }
}