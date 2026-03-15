# UniCalc — Unit Converter & Calculator

A comprehensive unit converter, scientific calculator, and currency converter for Android.

## Features

### Unit Converter
- **19 categories**: Length, Weight, Temperature, Area, Volume, Speed, Time, Digital Storage, Pressure, Energy, Power, Fuel Economy, Angle, Frequency, Cooking, Torque, Density, Force, Illuminance
- Instant real-time conversion as you type
- Show all unit results simultaneously
- Swap input/output units
- Favorites for quick access
- Conversion history with one-tap repeat

### Scientific Calculator
- Standard: +, -, ×, ÷, %, parentheses
- Scientific: sin, cos, tan, log, ln, sqrt, power, factorial, pi, e
- Full expression evaluator (recursive descent parser)
- Calculation history with bottom sheet

### Currency Converter
- Live exchange rates via frankfurter.app API (free, no key)
- 25+ currencies with flag emoji + search
- Offline mode with cached rates (1-hour TTL)
- Popular currencies quick access

### Profession Calculators
- **Construction**: Concrete volume, brick count, paint coverage
- **Electrical**: Ohm's law (V=IR), power calculator (P=VI), AWG wire gauge reference
- **Cooking**: Recipe scaler with dynamic ingredient list
- **Health**: BMI calculator with color-coded categories
- **Finance**: Tip calculator (bill split), Loan/EMI calculator

### User Experience
- Onboarding screen (3-slide first launch)
- Copy result on long press
- Number formatting with locale-aware display
- Home screen widget (Glance)
- Material3 + dynamic colors, Light/Dark mode
- Responsive tablet layouts (adaptive grids, side-by-side calculator)
- In-app language selector (14 languages)
- AdMob banner ad

### Internationalization
14 languages: English, Spanish, French, German, Hindi, Arabic, Malay, Marathi, Tamil, Malayalam, Telugu, Kannada, Gujarati, Punjabi

## Tech Stack
Kotlin, Jetpack Compose, Material3, Hilt, Room, Retrofit, OkHttp, Glance, DataStore, AdMob

**Min SDK:** 26 | **Target SDK:** 35

## Building
```bash
./gradlew assembleDebug
```
