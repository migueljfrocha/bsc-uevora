# SpaceHub - Android Application

An Android application for exploring NASA space imagery, developed for the Mobile Systems and Applications course at Universidade de Évora.

## About

**SpaceHub** is an Android application that brings space-related NASA content into a mobile experience. It allows users to view the Astronomy Picture of the Day (APOD), explore previous APOD entries, search NASA's image library, and view an interactive EPIC image sequence built from real Earth images captured by the DSCOVR mission.

The application was developed using the concepts from the Android Basics with Compose course, including composable UI, Material Design, navigation, app architecture, and internet connectivity.

## Technologies

- **Language:** Kotlin
- **Platform:** Android
- **UI:** Jetpack Compose, Material 3
- **Networking:** Retrofit
- **Images:** Coil
- **Navigation:** Navigation Compose
- **Build Tool:** Gradle
- **APIs:** NASA APOD, EPIC, and image library APIs

---

## How to Run

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- JDK compatible with the Android Gradle plugin
- Internet connection

### Steps

Import the project directly from the zip file, or use the folder below:

```bash
SpaceHub
```

Build it from the command line:

```bash
cd SpaceHub
./gradlew assembleDebug
```

On Windows:

```bash
cd SpaceHub
gradlew.bat assembleDebug
```

The project uses `DEMO_KEY` by default for the NASA API key. To use a personal key, create or edit `local.properties` and add:

```properties
NASA_API_KEY=your_key_here
```
---

## Grade

[![Grade](https://img.shields.io/badge/Grade-19.0%2F20.0-brightgreen)]()

*Mobile Systems and Applications - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)

---

## Additional Notes

NASA API requests may be limited when using the default `DEMO_KEY`.
