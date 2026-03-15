# UniCalc — Unit Converter & Calculator

A comprehensive unit converter, scientific calculator, and currency converter for Android, built with Kotlin and Jetpack Compose.

## Features

- **15 Unit Categories** — Length, Weight, Temperature, Area, Volume, Speed, Time, Digital Storage, Pressure, Energy, Power, Fuel Economy, Angle, Frequency, Cooking
- **Real-time Conversion** — Instant results as you type with all units shown simultaneously
- **Scientific Calculator** — Full expression evaluator with sin, cos, tan, log, ln, sqrt, power, factorial, pi, e, parentheses
- **Live Currency Converter** — 150+ currencies with live exchange rates (frankfurter.app API), offline caching
- **Profession Calculators:**
  - **Construction** — Concrete volume, brick count, paint coverage
  - **Electrical** — Ohm's law, power calculator, AWG wire gauge reference
  - **Cooking** — Recipe scaler with dynamic ingredient list
- **Conversion History** — Recent conversions with one-tap repeat
- **Favorites** — Pin most-used unit pairs for quick access
- **Home Screen Widget** — Quick converter widget (Glance)

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material3 (Material You dynamic colors)
- **Architecture:** MVVM (ViewModel + Repository)
- **DI:** Hilt
- **Database:** Room
- **Network:** Retrofit + OkHttp (currency API)
- **Widget:** Glance
- **Settings:** DataStore Preferences
- **Min SDK:** 26 | **Target SDK:** 35

## Building

```bash
./gradlew assembleDebug
```

## Screenshots

_Coming soon_

## License

All rights reserved.
