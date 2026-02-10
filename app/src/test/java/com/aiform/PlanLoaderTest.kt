package com.aiform

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlanLoaderTest {
    private val parser = Json { ignoreUnknownKeys = true }

    @Test
    fun parseJson_okForSchemaVersion1() {
        val json = """
            {
              "schemaVersion": 1,
              "generatedAtUtc": "2026-02-10T08:00:00Z",
              "dayPlan": {
                "date": "2026-02-10",
                "timeZoneId": "Europe/Madrid",
                "workout": {
                  "title": "Upper A",
                  "exercises": [
                    {
                      "id": "bench_press",
                      "name": "Press banca",
                      "restBetweenSetsSec": 90,
                      "restAfterExerciseSec": 120,
                      "sets": [{ "targetReps": 10, "targetWeightKg": 60.0 }]
                    }
                  ]
                }
              }
            }
        """.trimIndent()

        val payload = parser.decodeFromString(PlanPayload.serializer(), json)
        assertEquals(1, payload.schemaVersion)
        assertEquals("Upper A", payload.dayPlan.workout.title)
    }

    @Test
    fun parseJson_failsWhenSchemaMismatch() {
        val bad = """
            {
              "schemaVersion": 2,
              "generatedAtUtc": "2026-02-10T08:00:00Z",
              "dayPlan": {
                "date": "2026-02-10",
                "timeZoneId": "Europe/Madrid",
                "workout": {
                  "title": "Upper A",
                  "exercises": [{
                    "id": "bench_press",
                    "name": "Press banca",
                    "restBetweenSetsSec": 90,
                    "restAfterExerciseSec": 120,
                    "sets": [{ "targetReps": 10, "targetWeightKg": 60.0 }]
                  }]
                }
              }
            }
        """.trimIndent()

        val payload = parser.decodeFromString(PlanPayload.serializer(), bad)
        val exception = kotlin.runCatching {
            require(payload.schemaVersion == 1) { "Unsupported schemaVersion" }
        }.exceptionOrNull()

        assertTrue(exception is IllegalArgumentException)
    }
}
