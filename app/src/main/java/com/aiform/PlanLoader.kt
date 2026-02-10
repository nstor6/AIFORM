package com.aiform

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class PlanLoader(
    private val context: Context,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    private val _plan = MutableStateFlow<DayPlan?>(null)
    val dayPlan: StateFlow<DayPlan?> = _plan.asStateFlow()

    suspend fun reloadFromAssets(assetName: String = "plan_actual.json"): Result<DayPlan> {
        return runCatching {
            val payloadText = context.assets.open(assetName).bufferedReader().use { it.readText() }
            val payload = json.decodeFromString(PlanPayload.serializer(), payloadText)
            validate(payload)
            payload.dayPlan
        }.onSuccess { parsed ->
            _plan.value = parsed
        }
    }

    fun parseAndValidate(payloadText: String): DayPlan {
        val payload = json.decodeFromString(PlanPayload.serializer(), payloadText)
        validate(payload)
        return payload.dayPlan
    }

    private fun validate(payload: PlanPayload) {
        require(payload.schemaVersion == 1) { "Unsupported schemaVersion=${payload.schemaVersion}" }
        require(payload.dayPlan.workout.exercises.isNotEmpty()) { "Workout must include at least one exercise" }
        payload.dayPlan.workout.exercises.forEach { ex ->
            require(ex.sets.isNotEmpty()) { "Exercise ${ex.id} requires at least one set" }
        }
    }
}
