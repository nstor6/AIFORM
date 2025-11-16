package com.nestor.aiform

data class MealDefinition(
    val id: String,
    val label: String
)

data class ExerciseDefinition(
    val id: String,
    val name: String,
    val defaultRestSeconds: Int
)

// De momento, un día tipo. Más adelante: por día de la semana.
val DailyMeals: List<MealDefinition> = listOf(
    MealDefinition("breakfast", "Desayuno"),
    MealDefinition("lunch", "Comida"),
    MealDefinition("snack", "Merienda"),
    MealDefinition("dinner", "Cena")
)

// Entrenamiento tipo (puedes adaptarlo a tu rutina real)
val TrainingPlan: List<ExerciseDefinition> = listOf(
    ExerciseDefinition("press_banca", "Press banca", 90),
    ExerciseDefinition("remo_barra", "Remo con barra", 90),
    ExerciseDefinition("peso_muerto_rumano", "Peso muerto rumano", 120),
    ExerciseDefinition("sentadilla", "Sentadilla", 120),
    ExerciseDefinition("press_militar", "Press militar", 90),
    ExerciseDefinition("curl_biceps", "Curl bíceps", 60),
    ExerciseDefinition("extension_triceps", "Extensión tríceps", 60),
    ExerciseDefinition("abs", "Abdominales", 45)
)
