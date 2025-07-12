# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is **ChukChukHaksa** (척척학사), a Kotlin Multiplatform (KMP) project targeting Android and iOS. It's a university timetable and course evaluation service for Suwon University students, built with Compose Multiplatform and following Clean Architecture with MVI pattern.

## Build Commands

### Core Development Commands

- **Build project**: `./gradlew build`
- **Clean build**: `./gradlew clean build`
- **Run Android app**: `./gradlew :composeApp:installDebug`
- **Run tests**: `./gradlew test`
- **Code quality**: `./gradlew detekt`

### Platform-Specific Commands

- **Android release**: `./gradlew :composeApp:bundleRelease`
- **iOS build**: Open `iosApp/iosApp.xcodeproj` in Xcode

## Architecture

### Clean Architecture with MVI

The project follows Clean Architecture principles with MVI (Model-View-Intent) pattern:

- **Presentation Layer**: Jetpack Compose UI with ViewModels using MVI pattern
- **Domain Layer**: Use cases and repository interfaces
- **Data Layer**: Repository implementations, data sources (local/remote)

### Key Architectural Components

- **MVI Store**: Custom MVI implementation located in `common/ui/MviStore.kt`
- **Navigation**: Jetpack Navigation Compose with type-safe navigation
- **Dependency Injection**: Koin for DI across platforms
- **State Management**: Compose state with immutable data structures (kotlinx-immutable)

### Directory Structure

```
composeApp/src/
├── commonMain/kotlin/com/chukchukhaksa/mobile/
│   ├── common/           # Shared utilities, UI components, design system
│   ├── data/            # Repository implementations, data mapping
│   ├── domain/          # Use cases, repository interfaces, models
│   ├── local/           # Room database, DataStore, local data sources
│   ├── remote/          # Firebase, network data sources
│   ├── presentation/    # Screens, ViewModels, navigation
│   └── di/              # Dependency injection modules
├── androidMain/         # Android-specific implementations
├── iosMain/             # iOS-specific implementations
└── commonTest/          # Shared tests
```

### Key Technologies

- **UI**: Compose Multiplatform with Material 3
- **WebView**: Compose WebView Multiplatform for embedded web content
- **Database**: Room with SQLite, platform-specific factories
- **Networking**: Firebase Realtime Database
- **Storage**: DataStore Preferences
- **Async**: Kotlin Coroutines and Flow
- **Testing**: Kotlin Test, Coroutines Test

## Development Guidelines

### MVI Pattern Usage

Each feature follows MVI pattern with:
- `Contract.kt`: State, SideEffect, and Intent definitions
- `ViewModel.kt`: Business logic with MviStore
- `Screen.kt`: Compose UI components

Example MVI flow:
```kotlin
// State updates
mviStore.intent { state -> state.copy(loading = true) }

// Side effects  
mviStore.sideEffect(SideEffect.ShowToast("Message"))
```

### Navigation

- Type-safe navigation with argument passing
- Bottom navigation with two main tabs: Timetable and WebView
- Navigation graphs defined per feature in `navigation/` directories
- Navigation handled in `MainNavigator.kt`
- State preservation across tab switches using `saveState = true` and `restoreState = true`

### WebView Implementation

- Uses `compose-webview-multiplatform` library for cross-platform WebView
- WebView state preserved across navigation with `rememberWebViewState`
- WebView only shows on main tab screen (hidden on sub-screens)
- Default URL: `https://www.suwon.ac.kr`

### Dependency Injection

- Module-based Koin configuration in `di/InitKoin.kt`
- Platform-specific modules for Android/iOS differences
- Repository and DataSource modules separated by feature

### Database Schema Management

Room database schemas are versioned in `/schemas` directory. Migration files located in `local/database/*/migration/`.

### Code Quality

- Detekt configuration in `detekt-config.yml` with Compose-specific rules
- Indentation: 2 spaces (configured in detekt)
- Trailing commas enforced on declaration and call sites

### Testing

- Unit tests in `commonTest/`
- Focus on domain layer use case testing
- Coroutines testing with `kotlinx-coroutines-test`

### Firebase Configuration

- Android: `google-services.json` in `composeApp/`
- iOS: `GoogleService-Info.plist` in `iosApp/iosApp/`

### Platform-Specific Implementations

Use expect/actual pattern for platform differences:
- Database factories
- DataStore creation  
- Platform info (version, debug flags)
- UI effects (status bar colors)

## Key Files

- `App.kt`: Main application entry point with navigation setup
- `MainContract.kt`/`MainViewModel.kt`: Global app state management
- `InitKoin.kt`: Dependency injection configuration
- `libs.versions.toml`: Centralized dependency management