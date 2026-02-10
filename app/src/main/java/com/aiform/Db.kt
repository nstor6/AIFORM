package com.aiform

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey val id: String,
    val dayKey: String,
    val timeZoneIdUsed: String,
    val startedAtUtc: String,
    val endedAtUtc: String?,
    val workoutTitle: String,
    val createdAtUtc: String
)

@Entity(
    tableName = "workout_set_logs",
    foreignKeys = [ForeignKey(
        entity = WorkoutSessionEntity::class,
        parentColumns = ["id"],
        childColumns = ["sessionId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("sessionId")]
)
data class WorkoutSetLogEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val exerciseId: String,
    val exerciseNameSnapshot: String,
    val setIndex: Int,
    val targetRepsSnapshot: Int,
    val targetWeightKgSnapshot: Double,
    val actualReps: Int?,
    val actualWeightKg: Double?,
    val completedAtUtc: String,
    val restSecUsed: Int?
)

@Entity(
    tableName = "ai_observations",
    foreignKeys = [ForeignKey(
        entity = WorkoutSessionEntity::class,
        parentColumns = ["id"],
        childColumns = ["sessionId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("sessionId")]
)
data class AiObservationEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val exerciseId: String,
    val setIndex: Int,
    val text: String,
    val createdAtUtc: String
)

@Entity(tableName = "daily_summaries")
data class DailySummaryEntity(
    @PrimaryKey val dayKey: String,
    val timeZoneIdUsed: String,
    val trainingCompleted: Boolean,
    val plansCompleted: Int?,
    val calories: Int?,
    val weightKg: Double?,
    val createdAtUtc: String,
    val closeReason: String
)

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSession(entity: WorkoutSessionEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSetLog(entity: WorkoutSetLogEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertObservation(entity: AiObservationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailySummary(entity: DailySummaryEntity)

    @Query("UPDATE workout_sessions SET endedAtUtc=:endedAt WHERE id=:sessionId")
    suspend fun endSession(sessionId: String, endedAt: String)

    @Query("SELECT EXISTS(SELECT 1 FROM daily_summaries WHERE dayKey=:dayKey)")
    suspend fun isDayClosed(dayKey: String): Boolean

    @Query("SELECT * FROM workout_set_logs WHERE sessionId=:sessionId ORDER BY setIndex")
    suspend fun setLogsBySession(sessionId: String): List<WorkoutSetLogEntity>

    @Query("SELECT * FROM ai_observations WHERE sessionId=:sessionId ORDER BY setIndex")
    suspend fun observationsBySession(sessionId: String): List<AiObservationEntity>
}

@Database(
    entities = [
        WorkoutSessionEntity::class,
        WorkoutSetLogEntity::class,
        AiObservationEntity::class,
        DailySummaryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "aiform.db").build()
        }
    }
}
