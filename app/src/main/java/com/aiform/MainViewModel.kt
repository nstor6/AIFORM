package com.aiform

import android.app.Application
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

sealed interface Screen {
    data object Dashboard : Screen
    data object Guided : Screen
    data object Settings : Screen
}

data class GuidedUiState(
    val sessionId: String? = null,
    val exerciseIndex: Int = 0,
    val setIndex: Int = 0,
    val showObservationDialog: Boolean = false,
    val restRemainingSec: Int = 0,
    val pendingAdvance: Boolean = false
)

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val planLoader = PlanLoader(app)
    private val timeZoneManager = TimeZoneManager(app)
    private val db = AppDatabase.build(app)
    private val repository = WorkoutRepository(db.workoutDao())

    private val _screen = MutableStateFlow<Screen>(Screen.Dashboard)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    private val _plan = MutableStateFlow<DayPlan?>(null)
    val plan: StateFlow<DayPlan?> = _plan.asStateFlow()

    private val _guided = MutableStateFlow(GuidedUiState())
    val guided: StateFlow<GuidedUiState> = _guided.asStateFlow()

    init {
        viewModelScope.launch {
            timeZoneManager.onAppStartAutoCloseDay(Instant.now()) { previousDayKey, tzId, now ->
                repository.closeDayIfOpen(previousDayKey, tzId, "auto_day_rollover", now)
            }
            refreshPlan()
        }
    }

    fun refreshPlan() {
        viewModelScope.launch {
            val result = planLoader.reloadFromAssets()
            _plan.value = result.getOrNull()
        }
    }

    fun navigate(screen: Screen) {
        _screen.value = screen
    }

    fun toggleDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
    }

    fun startTraining() {
        val loadedPlan = _plan.value ?: return
        viewModelScope.launch {
            val now = Instant.now()
            val tz = timeZoneManager.getSelectedTimeZoneId()
            val dayKey = timeZoneManager.computeDayKey(now, tz).toString()
            val sessionId = repository.createSession(dayKey, tz, loadedPlan.workout.title, now)
            _guided.value = GuidedUiState(sessionId = sessionId)
            _screen.value = Screen.Guided
        }
    }

    fun markSetDone() {
        _guided.value = _guided.value.copy(showObservationDialog = true, pendingAdvance = true)
    }

    fun submitObservation(note: String?) {
        val loadedPlan = _plan.value ?: return
        val guidedState = _guided.value
        val sessionId = guidedState.sessionId ?: return
        val exercise = loadedPlan.workout.exercises[guidedState.exerciseIndex]
        val setIndex = guidedState.setIndex

        viewModelScope.launch {
            repository.logSet(sessionId, exercise, setIndex, Instant.now(), note)
            _guided.value = _guided.value.copy(showObservationDialog = false)
            startRest(exercise.restBetweenSetsSec)
        }
    }

    private fun startRest(restSec: Int) {
        viewModelScope.launch {
            for (s in restSec downTo 1) {
                _guided.value = _guided.value.copy(restRemainingSec = s)
                delay(1000)
            }
            _guided.value = _guided.value.copy(restRemainingSec = 0)
            vibrate()
            advanceSetOrExercise()
        }
    }

    private fun advanceSetOrExercise() {
        val loadedPlan = _plan.value ?: return
        val state = _guided.value
        val exercise = loadedPlan.workout.exercises[state.exerciseIndex]
        if (state.setIndex < exercise.sets.lastIndex) {
            _guided.value = state.copy(setIndex = state.setIndex + 1, pendingAdvance = false)
            return
        }
        if (state.exerciseIndex < loadedPlan.workout.exercises.lastIndex) {
            _guided.value = state.copy(exerciseIndex = state.exerciseIndex + 1, setIndex = 0, pendingAdvance = false)
            return
        }
        finishTraining()
    }

    fun finishTraining() {
        val sessionId = _guided.value.sessionId ?: run {
            _screen.value = Screen.Dashboard
            return
        }
        viewModelScope.launch {
            repository.finalizeSession(sessionId, Instant.now())
            _guided.value = GuidedUiState()
            _screen.value = Screen.Dashboard
        }
    }

    fun closeDayManually() {
        viewModelScope.launch {
            val now = Instant.now()
            val tz = timeZoneManager.getSelectedTimeZoneId()
            val dayKey = timeZoneManager.computeDayKey(now, tz).toString()
            repository.closeDayIfOpen(dayKey, tz, "manual", now)
        }
    }

    fun setSelectedTimeZone(id: String) {
        viewModelScope.launch {
            timeZoneManager.setSelectedTimeZoneId(id)
        }
    }

    private fun vibrate() {
        val vibrator = getApplication<Application>().getSystemService(Vibrator::class.java)
        vibrator?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
