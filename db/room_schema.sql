-- Room-oriented SQL blueprint for immutable workout logging and daily close snapshots.

CREATE TABLE IF NOT EXISTS workout_sessions (
  id TEXT PRIMARY KEY NOT NULL,
  day_key TEXT NOT NULL,
  time_zone_id_used TEXT NOT NULL,
  started_at_utc TEXT NOT NULL,
  ended_at_utc TEXT,
  workout_title TEXT NOT NULL,
  created_at_utc TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_workout_sessions_day_key
  ON workout_sessions(day_key);

CREATE TABLE IF NOT EXISTS workout_set_logs (
  id TEXT PRIMARY KEY NOT NULL,
  session_id TEXT NOT NULL,
  exercise_id TEXT NOT NULL,
  exercise_name_snapshot TEXT NOT NULL,
  set_index INTEGER NOT NULL,
  target_reps_snapshot INTEGER NOT NULL,
  target_weight_kg_snapshot REAL NOT NULL,
  actual_reps INTEGER,
  actual_weight_kg REAL,
  completed_at_utc TEXT NOT NULL,
  rest_sec_used INTEGER,
  FOREIGN KEY(session_id) REFERENCES workout_sessions(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_workout_set_logs_session
  ON workout_set_logs(session_id);

CREATE TABLE IF NOT EXISTS ai_observations (
  id TEXT PRIMARY KEY NOT NULL,
  session_id TEXT NOT NULL,
  exercise_id TEXT NOT NULL,
  set_index INTEGER NOT NULL,
  text TEXT NOT NULL,
  created_at_utc TEXT NOT NULL,
  FOREIGN KEY(session_id) REFERENCES workout_sessions(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_ai_observations_session
  ON ai_observations(session_id);

CREATE TABLE IF NOT EXISTS daily_summaries (
  day_key TEXT PRIMARY KEY NOT NULL,
  time_zone_id_used TEXT NOT NULL,
  training_completed INTEGER NOT NULL,
  plans_completed INTEGER,
  calories INTEGER,
  weight_kg REAL,
  created_at_utc TEXT NOT NULL,
  close_reason TEXT NOT NULL CHECK(close_reason IN ('manual', 'auto_day_rollover'))
);
