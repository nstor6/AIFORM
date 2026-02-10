package com.aiform

import java.time.Instant
import java.util.UUID

class WorkoutRepository(private val dao: WorkoutDao) {
    suspend fun createSession(dayKey: String, timeZoneId: String, workoutTitle: String, now: Instant): String {
        val sessionId = UUID.randomUUID().toString()
        dao.insertSession(
            WorkoutSessionEntity(
                id = sessionId,
                dayKey = dayKey,
                timeZoneIdUsed = timeZoneId,
                startedAtUtc = now.toString(),
                endedAtUtc = null,
                workoutTitle = workoutTitle,
                createdAtUtc = now.toString()
            )
        )
        return sessionId
    }

    suspend fun logSet(
        sessionId: String,
        exercise: Exercise,
        setIndex: Int,
        now: Instant,
        note: String?,
        actualReps: Int? = null,
        actualWeightKg: Double? = null
    ) {
        val set = exercise.sets[setIndex]
        dao.insertSetLog(
            WorkoutSetLogEntity(
                id = UUID.randomUUID().toString(),
                sessionId = sessionId,
                exerciseId = exercise.id,
                exerciseNameSnapshot = exercise.name,
                setIndex = setIndex,
                targetRepsSnapshot = set.targetReps,
                targetWeightKgSnapshot = set.targetWeightKg,
                actualReps = actualReps,
                actualWeightKg = actualWeightKg,
                completedAtUtc = now.toString(),
                restSecUsed = exercise.restBetweenSetsSec
            )
        )

        if (!note.isNullOrBlank()) {
            dao.insertObservation(
                AiObservationEntity(
                    id = UUID.randomUUID().toString(),
                    sessionId = sessionId,
                    exerciseId = exercise.id,
                    setIndex = setIndex,
                    text = note,
                    createdAtUtc = now.toString()
                )
            )
        }
    }

    suspend fun finalizeSession(sessionId: String, now: Instant) {
        dao.endSession(sessionId, now.toString())
    }

    suspend fun closeDayIfOpen(dayKey: String, tzId: String, reason: String, now: Instant) {
        if (!dao.isDayClosed(dayKey)) {
            dao.insertDailySummary(
                DailySummaryEntity(
                    dayKey = dayKey,
                    timeZoneIdUsed = tzId,
                    trainingCompleted = false,
                    plansCompleted = null,
                    calories = null,
                    weightKg = null,
                    createdAtUtc = now.toString(),
                    closeReason = reason
                )
            )
        }
    }
}
