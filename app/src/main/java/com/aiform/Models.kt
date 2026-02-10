package com.aiform

import kotlinx.serialization.Serializable

@Serializable
data class PlanPayload(
    val schemaVersion: Int,
    val generatedAtUtc: String,
    val dayPlan: DayPlan
)

@Serializable
data class DayPlan(
    val date: String,
    val timeZoneId: String,
    val workout: Workout
)

@Serializable
data class Workout(
    val title: String,
    val exercises: List<Exercise>
)

@Serializable
data class Exercise(
    val id: String,
    val name: String,
    val restBetweenSetsSec: Int,
    val restAfterExerciseSec: Int,
    val sets: List<ExerciseSet>
)

@Serializable
data class ExerciseSet(
    val targetReps: Int,
    val targetWeightKg: Double
)
