# AIFORM — Diseño operativo: DayKey, Zona Horaria, JSON fuente de verdad y Registro en BD

## 1) Capas del sistema

### A) Plan (source of truth mutable)
- El plan diario vive en `app/src/main/assets/plan_actual.json`.
- Es recargable por acción de usuario (`Actualizar plan`) y se valida por `schemaVersion` + campos obligatorios.
- El plan define **objetivos** (`targetReps`, `targetWeightKg`), no resultados reales.

### B) Día operativo (`DayKey`)
- `DayKey = LocalDate.now(ZoneId.of(selectedTimeZoneId))`.
- El reset ocurre a medianoche de la zona seleccionada por usuario.
- Si el usuario cambia `selected_time_zone_id`, el cálculo de “hoy” cambia desde ese instante para flujos futuros.

### C) Registro (inmutable, analítica)
- Al finalizar entreno y/o cerrar día, se persiste en BD inmutable.
- Si mañana cambia el JSON, no se reescriben sesiones previas.
- Se guardan `day_key`, `time_zone_id_used` y timestamps UTC para auditoría.

## 2) Preferencias y zona horaria

- Setting persistido: `selected_time_zone_id` (IANA).
- Valor por defecto primera ejecución: zona horaria del dispositivo.
- Cambio de zona:
  - Impacta DayKey actual, resets y recordatorios futuros.
  - No recalcula históricos ya cerrados.

## 3) Flujo de entrenamiento guiado (por series, no por repeticiones)

1. Pantalla “Entrenamiento de hoy” carga ejercicios del `dayPlan`.
2. Usuario pulsa “Iniciar entrenamiento” y entra a sesión guiada.
3. Se muestra solo serie actual del ejercicio actual:
   - nombre de ejercicio
   - serie actual (`X / total`)
   - `targetReps`
   - `targetWeightKg`
   - botón principal “Hecho” (marca fin de **serie**)
4. Al pulsar “Hecho”, modal obligatorio “Observaciones para IA”:
   - campo texto
   - acciones: “Guardar y seguir” / “Saltar”
5. Se persiste observación (si aplica) ligada a:
   - `day_key`, `session_id`, `exercise_id`, `set_index`, `created_at_utc`
6. Tras modal, inicia descanso automático de serie (`restBetweenSetsSec`).
7. Al finalizar descanso:
   - vibración
   - avanzar a siguiente serie o siguiente ejercicio
8. Al completar ejercicio:
   - opcional descanso entre ejercicios (`restAfterExerciseSec`)
9. Fin último ejercicio: CTA “Finalizar entrenamiento”.

## 4) Cierre de día robusto (manual + automático)

No depender de jobs exactos a medianoche.

### Mecanismo manual
- Botón “Cerrar día” en Dashboard/Settings.
- Genera `DailySummary` y marca el día como cerrado.

### Mecanismo automático al abrir app
- Al abrir app:
  - calcular `currentDayKey`
  - leer `lastOpenedDayKey`
  - si difieren y el anterior no está cerrado, cerrar automáticamente con `close_reason=auto_day_rollover`.

## 5) Algoritmos clave (pseudocódigo)

```kotlin
fun computeDayKey(selectedTimeZoneId: String, nowInstant: Instant): LocalDate {
  val zone = ZoneId.of(selectedTimeZoneId)
  return nowInstant.atZone(zone).toLocalDate()
}
```

```kotlin
fun onAppOpened(now: Instant) {
  val tz = settings.selectedTimeZoneId
  val current = computeDayKey(tz, now)
  val lastOpened = settings.lastOpenedDayKey

  if (lastOpened != null && current != lastOpened && !dailySummaryRepo.isClosed(lastOpened)) {
    dailySummaryRepo.closeDay(
      dayKey = lastOpened,
      timeZoneIdUsed = tz,
      closeReason = "auto_day_rollover",
      createdAtUtc = now
    )
  }

  settings.lastOpenedDayKey = current
}
```

```kotlin
fun onSetCompleted(sessionId: UUID, exercise: Exercise, setIndex: Int, now: Instant, note: String?) {
  setLogRepo.insert(
    sessionId = sessionId,
    exerciseId = exercise.id,
    exerciseNameSnapshot = exercise.name,
    setIndex = setIndex,
    targetRepsSnapshot = exercise.sets[setIndex].targetReps,
    targetWeightKgSnapshot = exercise.sets[setIndex].targetWeightKg,
    completedAtUtc = now
  )

  if (!note.isNullOrBlank()) {
    aiObservationRepo.insert(sessionId, exercise.id, setIndex, note, now)
  }

  timer.start(seconds = exercise.restBetweenSetsSec)
}
```

## 6) Definición de hecho (DoD) trazable

- Cambio de zona horaria altera DayKey y resets futuros.
- La sesión guiada sigue: `set -> popup observación -> descanso -> siguiente set`.
- Observaciones siempre vinculadas por `exerciseId + setIndex`.
- Históricos inmutables aunque cambie el JSON.
- Cierre de día manual y automático al detectar rollover.
