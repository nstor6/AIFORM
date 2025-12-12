# AIFORM 🧠🏋️‍♂️

AIFORM es una aplicación Android en desarrollo cuyo objetivo es centralizar en un solo lugar la **dieta**, el **entrenamiento**, el **peso corporal** y la **suplementación diaria**, permitiendo registrar el progreso de forma clara y estructurada.

La aplicación está desarrollada en **Kotlin** con **Jetpack Compose**, siguiendo una arquitectura modular pensada para crecer por fases.

> 🔄 Estado actual: **Fase 1 – MVP en construcción**  
> Incluye estructura base, navegación, registro de peso, modelos de datos y persistencia local.

---

## 🎯 Objetivos del proyecto

- Unificar en una sola app:
  - Dieta y checklists diarios.
  - Entrenamientos (series, repeticiones, peso y descansos).
  - Registro e historial de peso corporal.
  - Suplementación y hábitos diarios.
- Aplicar buenas prácticas reales de desarrollo Android:
  - UI declarativa con Jetpack Compose.
  - Arquitectura por capas.
  - Persistencia local con DataStore.
  - Gestión clara del estado y navegación.

---

## ✅ Funcionalidades actuales (Fase 1)

- Navegación principal entre pantallas con Jetpack Compose.
- Registro de peso corporal.
- Historial básico de pesos persistido localmente.
- Temporizador para descansos entre series.
- Modelos de datos para entrenamiento y peso.
- Base de arquitectura preparada para ampliaciones.

---

## 🧱 Tecnologías utilizadas

- **Kotlin**
- **Jetpack Compose**
- **DataStore Preferences**
- **Navigation Compose**
- **Gradle (KTS)**
- **Android Studio**

---

## 🗂 Estructura del proyecto

```
AIFORM/
 ├── data/
 │    ├── datastore/
 │    └── model/
 ├── ui/
 │    ├── screens/
 │    ├── components/
 │    └── navigation/
 ├── utils/
 └── MainActivity.kt
```

---

## 🚀 Roadmap

### Fase 1 — MVP (actual)
- [x] Estructura base del proyecto
- [x] Navegación
- [x] Registro de peso
- [x] Historial básico
- [x] Temporizador
- [ ] Mejora de UX
- [ ] Refactor y limpieza de código

### Fase 2 — Expansión
- [ ] Gestión completa de dieta (menús, macros, checklists)
- [ ] Gestión de suplementación
- [ ] Historial avanzado de entrenamientos
- [ ] Gráficas y métricas de progreso
- [ ] Exportación de datos (MD / CSV)

### Fase 3 — Futuro
- [ ] Integración de IA para sugerencias personalizadas
- [ ] Sincronización en la nube
- [ ] Perfiles y multiusuario

---

## ▶️ Ejecución del proyecto

```bash
git clone https://github.com/nstor6/AIFORM.git
```

Abrir en Android Studio, sincronizar Gradle y ejecutar en un dispositivo o emulador.

---
