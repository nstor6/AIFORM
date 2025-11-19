package com.nestor.aiform.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore para la dieta
private val Context.dietDataStore by preferencesDataStore(name = "diet_prefs")

private val BREAKFAST_KEY = booleanPreferencesKey("breakfast_done")
private val LUNCH_KEY = booleanPreferencesKey("lunch_done")
private val SNACK_KEY = booleanPreferencesKey("snack_done")
private val DINNER_KEY = booleanPreferencesKey("dinner_done")

data class DietChecklistState(
    val breakfastDone: Boolean = false,
    val lunchDone: Boolean = false,
    val snackDone: Boolean = false,
    val dinnerDone: Boolean = false
)

class DietRepository(private val context: Context) {

    val dietState: Flow<DietChecklistState> = context.dietDataStore.data.map { prefs ->
        DietChecklistState(
            breakfastDone = prefs[BREAKFAST_KEY] ?: false,
            lunchDone = prefs[LUNCH_KEY] ?: false,
            snackDone = prefs[SNACK_KEY] ?: false,
            dinnerDone = prefs[DINNER_KEY] ?: false
        )
    }

    suspend fun setBreakfast(done: Boolean) {
        context.dietDataStore.edit { prefs ->
            prefs[BREAKFAST_KEY] = done
        }
    }

    suspend fun setLunch(done: Boolean) {
        context.dietDataStore.edit { prefs ->
            prefs[LUNCH_KEY] = done
        }
    }

    suspend fun setSnack(done: Boolean) {
        context.dietDataStore.edit { prefs ->
            prefs[SNACK_KEY] = done
        }
    }

    suspend fun setDinner(done: Boolean) {
        context.dietDataStore.edit { prefs ->
            prefs[DINNER_KEY] = done
        }
    }

    suspend fun resetDay() {
        context.dietDataStore.edit { prefs ->
            prefs[BREAKFAST_KEY] = false
            prefs[LUNCH_KEY] = false
            prefs[SNACK_KEY] = false
            prefs[DINNER_KEY] = false
        }
    }
}
