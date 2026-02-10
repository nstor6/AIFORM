package com.aiform

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private val Context.dataStore by preferencesDataStore(name = "aiform_prefs")

class TimeZoneManager(private val context: Context) {
    private object Keys {
        val SelectedTimeZoneId = stringPreferencesKey("selected_time_zone_id")
        val LastOpenedDayKey = stringPreferencesKey("last_opened_day_key")
    }

    suspend fun getSelectedTimeZoneId(): String {
        val prefs = context.dataStore.data.first()
        return prefs[Keys.SelectedTimeZoneId] ?: ZoneId.systemDefault().id
    }

    suspend fun setSelectedTimeZoneId(timeZoneId: String) {
        context.dataStore.edit { it[Keys.SelectedTimeZoneId] = timeZoneId }
    }

    fun computeDayKey(nowUtc: Instant, timeZoneId: String): LocalDate {
        return nowUtc.atZone(ZoneId.of(timeZoneId)).toLocalDate()
    }

    suspend fun getLastOpenedDayKey(): String? = context.dataStore.data.first()[Keys.LastOpenedDayKey]

    suspend fun setLastOpenedDayKey(dayKey: String) {
        context.dataStore.edit { it[Keys.LastOpenedDayKey] = dayKey }
    }

    suspend fun onAppStartAutoCloseDay(
        nowUtc: Instant,
        closeDayIfNeeded: suspend (previousDayKey: String, tzId: String, now: Instant) -> Unit
    ) {
        val tz = getSelectedTimeZoneId()
        val currentDayKey = computeDayKey(nowUtc, tz).toString()
        val lastOpenedDayKey = getLastOpenedDayKey()
        if (lastOpenedDayKey != null && lastOpenedDayKey != currentDayKey) {
            closeDayIfNeeded(lastOpenedDayKey, tz, nowUtc)
        }
        setLastOpenedDayKey(currentDayKey)
    }
}
