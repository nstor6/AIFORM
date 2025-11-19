package com.nestor.aiform

import java.time.DayOfWeek

// ---- DIETA ----

data class MacroBreakdown(
    val kcal: Int? = null,
    val proteinGr: Int? = null,
    val carbsGr: Int? = null,
    val fatsGr: Int? = null
)

data class MealItem(
    val name: String,
    val amountGr: Int? = null,
    val units: String? = null,
    val notes: String? = null,
    val macros: MacroBreakdown? = null
)

data class MealDefinition(
    val id: String,
    val label: String,
    val time: String? = null,
    val items: List<MealItem> = emptyList(),
    val targetMacros: MacroBreakdown? = null
)

data class DietDayPlan(
    val dayOfWeek: DayOfWeek,
    val meals: List<MealDefinition>
)

// ---- ENTRENAMIENTO ----

data class ExerciseSetScheme(
    val sets: Int,
    val reps: Int,
    val rir: Int? = null,
    val restSeconds: Int
)

data class ExerciseDefinition(
    val id: String,
    val name: String,
    val targetSets: List<ExerciseSetScheme>,
    val notes: String? = null
)

data class TrainingDayPlan(
    val dayOfWeek: DayOfWeek,
    val exercises: List<ExerciseDefinition>
)

// ---- PLAN COMPLETO DEL DÍA ----

data class DayPlan(
    val dayOfWeek: DayOfWeek,
    val diet: DietDayPlan,
    val training: TrainingDayPlan
)

// ---- EXTENSIÓN PARA EL DESCANSO POR DEFECTO ----

fun ExerciseDefinition.defaultRestSeconds(): Int =
    this.targetSets.firstOrNull()?.restSeconds ?: 60
