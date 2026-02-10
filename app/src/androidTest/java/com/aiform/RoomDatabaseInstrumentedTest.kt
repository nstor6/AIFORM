package com.aiform

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDatabaseInstrumentedTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: WorkoutDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        dao = db.workoutDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertSessionSetLogAndObservation_queryBack() = runBlocking {
        dao.insertSession(
            WorkoutSessionEntity(
                id = "s1",
                dayKey = "2026-02-10",
                timeZoneIdUsed = "Europe/Madrid",
                startedAtUtc = "2026-02-10T10:00:00Z",
                endedAtUtc = null,
                workoutTitle = "Upper A",
                createdAtUtc = "2026-02-10T10:00:00Z"
            )
        )

        dao.insertSetLog(
            WorkoutSetLogEntity(
                id = "l1",
                sessionId = "s1",
                exerciseId = "bench_press",
                exerciseNameSnapshot = "Press banca",
                setIndex = 0,
                targetRepsSnapshot = 10,
                targetWeightKgSnapshot = 60.0,
                actualReps = 10,
                actualWeightKg = 60.0,
                completedAtUtc = "2026-02-10T10:01:00Z",
                restSecUsed = 90
            )
        )

        dao.insertObservation(
            AiObservationEntity(
                id = "o1",
                sessionId = "s1",
                exerciseId = "bench_press",
                setIndex = 0,
                text = "RPE 8",
                createdAtUtc = "2026-02-10T10:01:10Z"
            )
        )

        assertEquals(1, dao.setLogsBySession("s1").size)
        assertEquals(1, dao.observationsBySession("s1").size)
    }
}
