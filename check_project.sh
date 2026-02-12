#!/data/data/com.termux/files/usr/bin/bash

# ============================================================================
# ДИАГНОСТИКА ПРОЕКТА KMP_GEOBLINKER
# Показывает какие проблемы будут исправлены
# ============================================================================

PROJECT_DIR="${1:-$(pwd)}"

if [ ! -d "$PROJECT_DIR" ]; then
    echo "❌ Директория не найдена: $PROJECT_DIR"
    echo "Использование: bash $0 /path/to/KMP_GEOBLINKER-main"
    exit 1
fi

cd "$PROJECT_DIR"

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║          ДИАГНОСТИКА ПРОЕКТА KMP_GEOBLINKER                    ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo "📁 Проект: $(pwd)"
echo ""

# Проверка 1: Файлы с неправильным package/import
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔍 [1/6] Проверка порядка package/import"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

broken_files=$(find composeApp/src -name "*.kt" -type f -exec grep -l "^import.*androidx.navigation.compose" {} \; 2>/dev/null | wc -l)

if [ "$broken_files" -gt 0 ]; then
    echo "❌ Найдено файлов с неправильным порядком: $broken_files"
    echo ""
    echo "Примеры (первые 5):"
    find composeApp/src -name "*.kt" -type f -exec grep -l "^import.*androidx.navigation.compose" {} \; 2>/dev/null | head -5 | while read file; do
        echo "  • $file"
    done
else
    echo "✅ Все файлы в порядке"
fi
echo ""

# Проверка 2: Git merge конфликты
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔍 [2/6] Проверка Git merge конфликтов"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

conflict_files=$(find composeApp/src -name "*.kt" -type f -exec grep -l "<<<<<<< HEAD" {} \; 2>/dev/null)

if [ -n "$conflict_files" ]; then
    echo "❌ Найдены merge конфликты:"
    echo "$conflict_files" | while read file; do
        echo "  • $file"
    done
else
    echo "✅ Merge конфликты не найдены"
fi
echo ""

# Проверка 3: Expect/Actual для DatabaseDriverFactory
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔍 [3/6] Проверка expect/actual DatabaseDriverFactory"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

expect_files=0
android_files=0
ios_files=0

# commonMain (expect)
if [ -f "composeApp/src/commonMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" ]; then
    if grep -q "^expect class DatabaseDriverFactory" "composeApp/src/commonMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" 2>/dev/null; then
        expect_files=$((expect_files + 1))
    fi
fi

if [ -f "composeApp/src/commonMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" ]; then
    if grep -q "^expect class DatabaseDriverFactory" "composeApp/src/commonMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" 2>/dev/null; then
        expect_files=$((expect_files + 1))
    fi
fi

# androidMain (actual)
if [ -f "composeApp/src/androidMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" ]; then
    if grep -q "^actual class DatabaseDriverFactory" "composeApp/src/androidMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" 2>/dev/null; then
        android_files=$((android_files + 1))
    fi
fi

if [ -f "composeApp/src/androidMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" ]; then
    if grep -q "^actual class DatabaseDriverFactory" "composeApp/src/androidMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" 2>/dev/null; then
        android_files=$((android_files + 1))
    fi
fi

# iosMain (actual)
if [ -f "composeApp/src/iosMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" ]; then
    if grep -q "^actual class DatabaseDriverFactory" "composeApp/src/iosMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" 2>/dev/null; then
        ios_files=$((ios_files + 1))
    fi
fi

if [ -f "composeApp/src/iosMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" ]; then
    if grep -q "^actual class DatabaseDriverFactory" "composeApp/src/iosMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" 2>/dev/null; then
        ios_files=$((ios_files + 1))
    fi
fi

echo "Expect файлы (commonMain): $expect_files/2"
echo "Android actual файлы: $android_files/2"
echo "iOS actual файлы: $ios_files/2"

if [ "$expect_files" -eq 2 ] && [ "$android_files" -eq 2 ] && [ "$ios_files" -eq 2 ]; then
    echo "✅ Все expect/actual на месте"
else
    echo "❌ Требуется создание expect/actual файлов"
fi
echo ""

# Проверка 4: Platform expect/actual
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔍 [4/6] Проверка Platform expect/actual"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

platform_expect=0
platform_android=0
platform_ios=0

[ -f "composeApp/src/commonMain/kotlin/org/example/geoblinker/core/platform/Platform.kt" ] && platform_expect=1
[ -f "composeApp/src/androidMain/kotlin/org/example/geoblinker/core/platform/Platform.kt" ] && platform_android=1
[ -f "composeApp/src/iosMain/kotlin/org/example/geoblinker/core/platform/Platform.kt" ] && platform_ios=1

echo "Platform expect: $platform_expect/1"
echo "Platform Android actual: $platform_android/1"
echo "Platform iOS actual: $platform_ios/1"

if [ "$platform_expect" -eq 1 ] && [ "$platform_android" -eq 1 ] && [ "$platform_ios" -eq 1 ]; then
    echo "✅ Platform файлы на месте"
else
    echo "❌ Требуется создание Platform файлов"
fi
echo ""

# Проверка 5: build.gradle.kts
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔍 [5/6] Проверка build.gradle.kts"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ -f "composeApp/build.gradle.kts" ]; then
    if grep -q 'create("AppDatabase")' "composeApp/build.gradle.kts" 2>/dev/null; then
        echo "✅ SQLDelight правильно настроен (AppDatabase)"
    else
        if grep -q 'create("MyDatabase")' "composeApp/build.gradle.kts" 2>/dev/null; then
            echo "❌ SQLDelight использует MyDatabase вместо AppDatabase"
        else
            echo "⚠️  SQLDelight конфигурация не найдена"
        fi
    fi
    
    if grep -q "org.jetbrains.androidx.navigation:navigation-compose" "composeApp/build.gradle.kts" 2>/dev/null; then
        echo "✅ Navigation зависимость добавлена"
    else
        echo "❌ Navigation зависимость отсутствует"
    fi
    
    if grep -q "io.insert-koin:koin-core" "composeApp/build.gradle.kts" 2>/dev/null; then
        echo "✅ Koin зависимость добавлена"
    else
        echo "❌ Koin зависимость отсутствует"
    fi
else
    echo "❌ build.gradle.kts не найден"
fi
echo ""

# Проверка 6: Android файлы
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔍 [6/6] Проверка Android файлов"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ -f "composeApp/src/androidMain/kotlin/org/example/geoblinker/MainActivity.kt" ]; then
    if grep -q "import androidx.activity.ComponentActivity" "composeApp/src/androidMain/kotlin/org/example/geoblinker/MainActivity.kt" 2>/dev/null; then
        echo "✅ MainActivity импорты в порядке"
    else
        echo "❌ MainActivity требует исправления импортов"
    fi
else
    echo "⚠️  MainActivity не найден"
fi

if [ -f "composeApp/src/androidMain/kotlin/org/example/geoblinker/GeoBlinkerApp.kt" ]; then
    if grep -q "import org.koin.android.ext.koin.androidContext" "composeApp/src/androidMain/kotlin/org/example/geoblinker/GeoBlinkerApp.kt" 2>/dev/null; then
        echo "✅ GeoBlinkerApp импорты в порядке"
    else
        echo "❌ GeoBlinkerApp требует исправления импортов"
    fi
else
    echo "⚠️  GeoBlinkerApp не найден"
fi
echo ""

# Итоговая сводка
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                    ИТОГОВАЯ СВОДКА                             ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

total_issues=0

[ "$broken_files" -gt 0 ] && total_issues=$((total_issues + broken_files))
[ -n "$conflict_files" ] && total_issues=$((total_issues + 3))
[ "$expect_files" -ne 2 ] || [ "$android_files" -ne 2 ] || [ "$ios_files" -ne 2 ] && total_issues=$((total_issues + 6))
[ "$platform_expect" -ne 1 ] || [ "$platform_android" -ne 1 ] || [ "$platform_ios" -ne 1 ] && total_issues=$((total_issues + 3))

if [ "$total_issues" -gt 0 ]; then
    echo "❌ Найдено проблем: ~$total_issues"
    echo ""
    echo "🔧 Для исправления запустите:"
    echo "   bash fix_kmp_project.sh $PROJECT_DIR"
    echo ""
else
    echo "✅ Проект в отличном состоянии!"
    echo ""
    echo "🚀 Можно запускать сборку:"
    echo "   cd $PROJECT_DIR"
    echo "   ./gradlew assembleDebug"
    echo ""
fi

exit 0
