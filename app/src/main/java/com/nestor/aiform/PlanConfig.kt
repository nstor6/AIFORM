package com.nestor.aiform

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate

object PlanProvider {

    @RequiresApi(Build.VERSION_CODES.O)
    fun todayPlan(): DayPlan {
        val today = LocalDate.now().dayOfWeek
        return samplePlanForDay(today)
    }

    private fun samplePlanForDay(day: DayOfWeek): DayPlan {
        val diet = sampleDiet(day)
        val training = sampleTraining(day)

        return DayPlan(
            dayOfWeek = day,
            diet = diet,
            training = training
        )
    }

    private fun sampleDiet(day: DayOfWeek): DietDayPlan {
        val meals = listOf(
            MealDefinition(
                id = "breakfast",
                label = "Desayuno",
                time = "08:30",
                items = listOf(
                    MealItem("Avena", amountGr = 80, units = "g"),
                    MealItem("Claras de huevo", amountGr = 200, units = "ml")
                )
            ),
            MealDefinition(
                id = "lunch",
                label = "Comida",
                time = "14:00",
                items = listOf(
                    MealItem("Arroz integral", amountGr = 100, units = "g"),
                    MealItem("Pechuga de pollo", amountGr = 150, units = "g")
                )
            ),
            MealDefinition(
                id = "snack",
                label = "Merienda",
                time = "17:30",
                items = listOf(
                    MealItem("Yogur desnatado", amountGr = 125, units = "g")
                )
            ),
            MealDefinition(
                id = "dinner",
                label = "Cena",
                time = "21:30",
                items = listOf(
                    MealItem("Pescado blanco", amountGr = 150, units = "g"),
                    MealItem("Verdura", amountGr = 200, units = "g")
                )
            )
        )

        return DietDayPlan(
            dayOfWeek = day,
            meals = meals
        )
    }

    private fun sampleTraining(day: DayOfWeek): TrainingDayPlan {
        val exercises = listOf(
            ExerciseDefinition(
                id = "press_banca",
                name = "Press banca",
                targetSets = listOf(
                    ExerciseSetScheme(sets = 4, reps = 8, rir = 2, restSeconds = 90)
                )
            ),
            ExerciseDefinition(
                id = "remo_barra",
                name = "Remo con barra",
                targetSets = listOf(
                    ExerciseSetScheme(sets = 4, reps = 10, rir = 2, restSeconds = 90)
                )
            ),
            ExerciseDefinition(
                id = "peso_muerto_rumano",
                name = "Peso muerto rumano",
                targetSets = listOf(
                    ExerciseSetScheme(sets = 3, reps = 8, rir = 2, restSeconds = 120)
                )
            ),
            ExerciseDefinition(
                id = "sentadilla",
                name = "Sentadilla",
                targetSets = listOf(
                    ExerciseSetScheme(sets = 4, reps = 8, rir = 2, restSeconds = 120)
                )
            ),
            ExerciseDefinition(
                id = "curl_biceps",
                name = "Curl bíceps",
                targetSets = listOf(
                    ExerciseSetScheme(sets = 3, reps = 12, restSeconds = 60)
                )
            ),
            ExerciseDefinition(
                id = "extension_triceps",
                name = "Extensión tríceps",
                targetSets = listOf(
                    ExerciseSetScheme(sets = 3, reps = 12, restSeconds = 60)
                )
            )
        )

        return TrainingDayPlan(
            dayOfWeek = day,
            exercises = exercises
        )
    }
}

// ---- ALIAS PARA NO ROMPER TU CÓDIGO ACTUAL ----

val DailyMeals: List<MealDefinition>
    get() = PlanProvider.todayPlan().diet.meals

val TrainingPlan: List<ExerciseDefinition>
    @RequiresApi(Build.VERSION_CODES.O)
    get() = PlanProvider.todayPlan().training.exercises
