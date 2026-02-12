# 🔧 Инструкция по исправлению проекта KMP_GEOBLINKER

## 📋 Описание проблем

Ваш KMP (Kotlin Multiplatform) проект имеет следующие критические ошибки:

1. **Синтаксические ошибки**: Неправильный порядок `package` и `import` в 200+ файлах
2. **Git merge конфликты**: Незавершенные конфликты в `DatabaseDriverFactory.kt`
3. **Отсутствующие expect/actual**: Не созданы platform-specific реализации для базы данных
4. **Отсутствующие импорты**: В Android файлах (`MainActivity`, `GeoBlinkerApp`)
5. **Проблемы с SQLDelight**: Неправильная конфигурация в `build.gradle.kts`

## 🚀 Быстрый старт (Termux)

### Шаг 1: Скачайте скрипт

Скрипт `fix_kmp_project.sh` уже создан и готов к использованию.

### Шаг 2: Запустите исправление

```bash
# Перейдите в директорию со скриптом
cd /путь/к/папке/со/скриптом

# Дайте права на выполнение
chmod +x fix_kmp_project.sh

# Запустите скрипт (укажите путь к проекту)
bash fix_kmp_project.sh /путь/к/KMP_GEOBLINKER-main
```

### Шаг 3: Соберите проект

```bash
# Перейдите в проект
cd /путь/к/KMP_GEOBLINKER-main

# Дайте права на gradlew
chmod +x gradlew

# Запустите сборку
./gradlew assembleDebug
```

## 📝 Что делает скрипт

Скрипт выполняет 9 этапов исправления:

### [1/9] Исправление порядка package/import
- Находит все `.kt` файлы с неправильным порядком
- Перемещает `package` декларацию в начало файла
- Удаляет дублирующиеся импорты `androidx.navigation.compose`
- Сортирует импорты

**Было:**
```kotlin
import androidx.navigation.compose.*
import androidx.navigation.compose.*
package org.example.geoblinker.presentation

import org.example.geoblinker.domain.models.Devices
```

**Стало:**
```kotlin
package org.example.geoblinker.presentation

import org.example.geoblinker.domain.models.Devices
```

### [2/9] Разрешение Git merge конфликтов
- Создает чистые `expect` декларации для `DatabaseDriverFactory`
- Удаляет маркеры конфликтов `<<<<<<< HEAD`, `=======`, `>>>>>>>`

### [3/9] Android actual implementations
- Создает `actual class DatabaseDriverFactory` для Android
- Использует `AndroidSqliteDriver`
- Настраивает контекст приложения

### [4/9] iOS actual implementations
- Создает `actual class DatabaseDriverFactory` для iOS
- Использует `NativeSqliteDriver`

### [5/9] Исправление Android файлов
- Восстанавливает правильные импорты в `MainActivity.kt`
- Восстанавливает правильные импорты в `GeoBlinkerApp.kt`
- Добавляет Koin инициализацию

### [6/9] Platform expect/actual
- Создает `Platform` interface в commonMain
- Создает Android и iOS реализации

### [7/9] Обновление build.gradle.kts
- Исправляет конфигурацию SQLDelight
- Добавляет все необходимые зависимости:
  - Compose Material3
  - Navigation
  - Koin
  - Ktor
  - SQLDelight drivers

### [8/9] Проверка DI модулей
- Создает `appModule.kt` если отсутствует

### [9/9] Очистка build кэша
- Удаляет старые build директории
- Удаляет gradle кэш

## 🔍 Детальная диагностика

### Основные ошибки из логов

```
e: Syntax error: Expecting a top level declaration.
e: Syntax error: imports are only allowed in the beginning of file.
e: Unresolved reference 'AndroidSqliteDriver'.
e: Unresolved reference 'ComponentActivity'.
e: Unresolved reference 'koin'.
```

### Причины ошибок

1. **Syntax error**: В коде есть строки типа `import androidx.navigation.compose.*` перед `package` декларацией
2. **Unresolved reference**: Отсутствуют импорты в файлах или неправильная конфигурация gradle

## ⚙️ Ручное исправление (если скрипт не сработал)

### 1. Исправление package/import вручную

Для каждого файла `.kt`:

```bash
# Найти все проблемные файлы
find composeApp/src -name "*.kt" -exec grep -l "^import.*androidx.navigation.compose" {} \;
```

Для каждого файла:
1. Переместите `package` в начало
2. Удалите дублирующиеся импорты
3. Оставьте одну пустую строку между `package`, `imports` и кодом

### 2. Создание expect/actual для базы данных

**commonMain/database/DatabaseDriverFactory.kt:**
```kotlin
package org.example.geoblinker.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
```

**androidMain/database/DatabaseDriverFactory.kt:**
```kotlin
package org.example.geoblinker.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "geoblinker.db"
        )
    }
}
```

**iosMain/database/DatabaseDriverFactory.kt:**
```kotlin
package org.example.geoblinker.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = AppDatabase.Schema,
            name = "geoblinker.db"
        )
    }
}
```

### 3. Обновление build.gradle.kts

Ключевые изменения:
```kotlin
sqldelight {
    databases {
        create("AppDatabase") {  // Было: MyDatabase
            packageName.set("org.example.geoblinker.database")
        }
    }
}
```

## 🐛 Решение проблем

### Проблема: "Unresolved reference 'Boolean'"
**Решение**: Это генерируется SQLDelight. Пересоберите проект:
```bash
./gradlew clean generateCommonMainAppDatabaseInterface
```

### Проблема: "Gradle sync failed"
**Решение**: 
1. Убедитесь, что JDK 17 установлен
2. Очистите кэш: `rm -rf .gradle/ build/`
3. Sync again

### Проблема: Скрипт не запускается в Termux
**Решение**:
```bash
# Установите необходимые пакеты
pkg install git wget curl

# Проверьте права
ls -l fix_kmp_project.sh

# Должно быть: -rwxr-xr-x
# Если нет, выполните:
chmod +x fix_kmp_project.sh
```

## 📊 Структура expect/actual

```
composeApp/
├── src/
│   ├── commonMain/
│   │   └── kotlin/.../database/
│   │       └── DatabaseDriverFactory.kt  (expect)
│   ├── androidMain/
│   │   └── kotlin/.../database/
│   │       └── DatabaseDriverFactory.kt  (actual)
│   └── iosMain/
│       └── kotlin/.../database/
│           └── DatabaseDriverFactory.kt  (actual)
```

## ✅ Проверка успешности

После выполнения скрипта проверьте:

1. **Файлы исправлены**:
```bash
head -5 composeApp/src/commonMain/kotlin/org/example/geoblinker/App.kt
# Должно быть:
# package org.example.geoblinker
#
# import ...
```

2. **Expect/actual созданы**:
```bash
ls -la composeApp/src/*/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt
# Должно показать 3 файла: commonMain, androidMain, iosMain
```

3. **Build.gradle обновлен**:
```bash
grep "AppDatabase" composeApp/build.gradle.kts
# Должно вывести: create("AppDatabase")
```

## 🎯 Ожидаемый результат

После успешного выполнения:
```bash
./gradlew assembleDebug
```

Вы должны увидеть:
```
BUILD SUCCESSFUL in XXs
```

## 📞 Поддержка

Если возникли проблемы:

1. Запустите с подробным логом:
```bash
./gradlew assembleDebug --stacktrace --info > build.log 2>&1
```

2. Проверьте `build.log` на наличие конкретных ошибок

3. Убедитесь что все зависимости доступны (может потребоваться интернет)

## 📚 Дополнительная информация

- SQLDelight документация: https://cashapp.github.io/sqldelight/
- Kotlin Multiplatform: https://kotlinlang.org/docs/multiplatform.html
- Koin DI: https://insert-koin.io/

---

**Автор скрипта**: Claude
**Версия**: 1.0
**Дата**: 2026-02-12
