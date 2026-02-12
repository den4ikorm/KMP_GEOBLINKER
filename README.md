# 🌍 GeoBlinker KMP

Кросс-платформенное приложение для отслеживания GPS-трекеров на Kotlin Multiplatform.

## 🚀 Технологии

- **Kotlin Multiplatform** - общий код для Android и iOS
- **Compose Multiplatform** - единый UI
- **MVI/MVVM** - чистая архитектура
- **Coroutines & Flow** - асинхронность
- **Koin** - dependency injection

## 📱 Платформы

- ✅ Android
- ✅ iOS

## 🏗️ Архитектура

Проект следует принципам Clean Architecture:

```
presentation/
  features/
    screen_name/
      ScreenNameScreen.kt      # UI
      ScreenNameState.kt       # State
      ScreenNameEvent.kt       # Events
      ScreenNameEffect.kt      # Side-effects
      ScreenNameViewModel.kt   # Logic
```

## 🔧 Сборка

### Android
```bash
./gradlew assembleDebug
```

### iOS
```bash
cd iosApp
pod install
open iosApp.xcworkspace
```

## 📄 Лицензия

Proprietary

## 👨‍💻 Автор

[den4ikorm](https://github.com/den4ikorm)
