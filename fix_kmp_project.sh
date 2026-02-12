#!/data/data/com.termux/files/usr/bin/bash

# ============================================================================
# ПОЛНЫЙ СКРИПТ ИСПРАВЛЕНИЯ KMP ПРОЕКТА GEOBLINKER ДЛЯ TERMUX
# ============================================================================
# Использование: bash fix_kmp_project.sh /path/to/KMP_GEOBLINKER-main
# ============================================================================

set -e

PROJECT_DIR="${1:-$(pwd)}"

if [ ! -d "$PROJECT_DIR" ]; then
    echo "❌ Ошибка: Директория не найдена: $PROJECT_DIR"
    echo "Использование: bash $0 /path/to/KMP_GEOBLINKER-main"
    exit 1
fi

cd "$PROJECT_DIR"
echo "📁 Работаем в: $(pwd)"
echo ""

# ============================================================================
# ФУНКЦИЯ 1: Исправление package/import во ВСЕХ файлах
# ============================================================================
fix_package_imports() {
    echo "🔧 [1/9] Исправление порядка package/import..."
    
    local count=0
    find composeApp/src -name "*.kt" -type f | while read -r file; do
        if head -5 "$file" | grep -q "^import.*androidx.navigation.compose"; then
            count=$((count + 1))
            
            # Временные файлы
            temp1="${file}.temp1"
            temp2="${file}.temp2"
            
            # Извлекаем package
            package_line=$(grep "^package " "$file" 2>/dev/null | head -1 || echo "")
            
            # Извлекаем уникальные импорты БЕЗ дублей androidx.navigation.compose
            grep "^import " "$file" 2>/dev/null | \
                grep -v "^import androidx.navigation.compose" | \
                sort -u > "$temp1" || true
            
            # Извлекаем код без package и import
            in_header=1
            while IFS= read -r line; do
                if [[ "$line" =~ ^package ]]; then
                    continue
                elif [[ "$line" =~ ^import ]]; then
                    continue
                elif [[ -z "$line" ]] && [[ $in_header -eq 1 ]]; then
                    continue
                else
                    in_header=0
                    echo "$line"
                fi
            done < "$file" > "$temp2"
            
            # Собираем файл правильно
            {
                echo "$package_line"
                echo ""
                cat "$temp1"
                echo ""
                cat "$temp2"
            } | awk 'NF > 0 || NR > 1 {print; blank=0} NF == 0 {if (!blank) print; blank=1}' > "${file}.new"
            
            mv "${file}.new" "$file"
            rm -f "$temp1" "$temp2"
            
            echo "  ✓ $file"
        fi
    done
    
    echo "  ✅ Package/import исправлены"
    echo ""
}

# ============================================================================
# ФУНКЦИЯ 2: Разрешение Git конфликтов
# ============================================================================
fix_merge_conflicts() {
    echo "🔧 [2/9] Разрешение Git merge конфликтов..."
    
    # core/database/DatabaseDriverFactory.kt (expect)
    cat > "composeApp/src/commonMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" << 'EOFDB1'
package org.example.geoblinker.core.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
EOFDB1
    
    # database/DatabaseDriverFactory.kt (expect)
    cat > "composeApp/src/commonMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" << 'EOFDB2'
package org.example.geoblinker.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
EOFDB2
    
    echo "  ✓ Expect файлы созданы"
    echo "  ✅ Конфликты разрешены"
    echo ""
}

# ============================================================================
# ФУНКЦИЯ 3: Android actual implementations
# ============================================================================
create_android_actuals() {
    echo "🔧 [3/9] Создание Android actual implementations..."
    
    mkdir -p "composeApp/src/androidMain/kotlin/org/example/geoblinker/core/database"
    mkdir -p "composeApp/src/androidMain/kotlin/org/example/geoblinker/database"
    
    # core/database
    cat > "composeApp/src/androidMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" << 'EOFAND1'
package org.example.geoblinker.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.example.geoblinker.database.AppDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "geoblinker.db"
        )
    }
}
EOFAND1
    
    # database
    cat > "composeApp/src/androidMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" << 'EOFAND2'
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
EOFAND2
    
    echo "  ✓ Android actuals созданы"
    echo "  ✅ Завершено"
    echo ""
}

# ============================================================================
# ФУНКЦИЯ 4: iOS actual implementations
# ============================================================================
create_ios_actuals() {
    echo "🔧 [4/9] Создание iOS actual implementations..."
    
    mkdir -p "composeApp/src/iosMain/kotlin/org/example/geoblinker/core/database"
    mkdir -p "composeApp/src/iosMain/kotlin/org/example/geoblinker/database"
    
    # core/database
    cat > "composeApp/src/iosMain/kotlin/org/example/geoblinker/core/database/DatabaseDriverFactory.kt" << 'EOFIOS1'
package org.example.geoblinker.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.example.geoblinker.database.AppDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = AppDatabase.Schema,
            name = "geoblinker.db"
        )
    }
}
EOFIOS1
    
    # database
    cat > "composeApp/src/iosMain/kotlin/org/example/geoblinker/database/DatabaseDriverFactory.kt" << 'EOFIOS2'
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
EOFIOS2
    
    echo "  ✓ iOS actuals созданы"
    echo "  ✅ Завершено"
    echo ""
}

# ============================================================================
# ФУНКЦИЯ 5: Исправление Android файлов
# ============================================================================
fix_android_files() {
    echo "🔧 [5/9] Исправление Android файлов..."
    
    # GeoBlinkerApp
    if [ -f "composeApp/src/androidMain/kotlin/org/example/geoblinker/GeoBlinkerApp.kt" ]; then
        cat > "composeApp/src/androidMain/kotlin/org/example/geoblinker/GeoBlinkerApp.kt" << 'EOFAPP'
package org.example.geoblinker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.example.geoblinker.di.appModule

class GeoBlinkerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GeoBlinkerApp)
            modules(appModule)
        }
    }
}
EOFAPP
        echo "  ✓ GeoBlinkerApp.kt"
    fi
    
    # MainActivity
    if [ -f "composeApp/src/androidMain/kotlin/org/example/geoblinker/MainActivity.kt" ]; then
        cat > "composeApp/src/androidMain/kotlin/org/example/geoblinker/MainActivity.kt" << 'EOFMAIN'
package org.example.geoblinker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
EOFMAIN
        echo "  ✓ MainActivity.kt"
    fi
    
    echo "  ✅ Android файлы исправлены"
    echo ""
}

# ============================================================================
# ФУНКЦИЯ 6: Platform expect/actual
# ============================================================================
create_platform_files() {
    echo "🔧 [6/9] Создание Platform expect/actual..."
    
    # expect
    mkdir -p "composeApp/src/commonMain/kotlin/org/example/geoblinker/core/platform"
    cat > "composeApp/src/commonMain/kotlin/org/example/geoblinker/core/platform/Platform.kt" << 'EOFPLAT'
package org.example.geoblinker.core.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
EOFPLAT
    
    # Android actual
    mkdir -p "composeApp/src/androidMain/kotlin/org/example/geoblinker/core/platform"
    cat > "composeApp/src/androidMain/kotlin/org/example/geoblinker/core/platform/Platform.kt" << 'EOFANDA'
package org.example.geoblinker.core.platform

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
EOFANDA
    
    # iOS actual
    mkdir -p "composeApp/src/iosMain/kotlin/org/example/geoblinker/core/platform"
    cat > "composeApp/src/iosMain/kotlin/org/example/geoblinker/core/platform/Platform.kt" << 'EOFIOSA'
package org.example.geoblinker.core.platform

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = 
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
EOFIOSA
    
    echo "  ✓ Platform файлы созданы"
    echo "  ✅ Завершено"
    echo ""
}

# ============================================================================
# ФУНКЦИЯ 7: Обновление build.gradle.kts
# ============================================================================
update_build_gradle() {
    echo "🔧 [7/9] Обновление build.gradle.kts..."
    
    cat > "composeApp/build.gradle.kts" << 'EOFGRADLE'
plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("app.cash.sqldelight") version "2.0.1"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
                
                implementation("io.insert-koin:koin-core:3.5.0")
                implementation("io.insert-koin:koin-compose:1.1.0")
                
                implementation("io.ktor:ktor-client-core:2.3.7")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
                
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
                
                implementation("app.cash.sqldelight:runtime:2.0.1")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")
            }
        }
        
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.8.2")
                implementation("androidx.core:core-ktx:1.12.0")
                implementation("io.insert-koin:koin-android:3.5.0")
                implementation("io.ktor:ktor-client-android:2.3.7")
                implementation("app.cash.sqldelight:android-driver:2.0.1")
            }
        }
        
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.7")
                implementation("app.cash.sqldelight:native-driver:2.0.1")
            }
        }
    }
}

android {
    namespace = "org.example.geoblinker"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "org.example.geoblinker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("org.example.geoblinker.database")
        }
    }
}
EOFGRADLE
    
    echo "  ✓ build.gradle.kts обновлен"
    echo "  ✅ Завершено"
    echo ""
}

# ============================================================================
# ФУНКЦИЯ 8: Добавление отсутствующих модулей DI
# ============================================================================
fix_di_modules() {
    echo "🔧 [8/9] Проверка DI модулей..."
    
    # Проверяем наличие appModule
    if [ ! -f "composeApp/src/commonMain/kotlin/org/example/geoblinker/di/appModule.kt" ]; then
        cat > "composeApp/src/commonMain/kotlin/org/example/geoblinker/di/appModule.kt" << 'EOFDI'
package org.example.geoblinker.di

import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    // Здесь будут зависимости
}
EOFDI
        echo "  ✓ Создан appModule.kt"
    fi
    
    echo "  ✅ DI модули проверены"
    echo ""
}

# ============================================================================
# ФУНКЦИЯ 9: Очистка
# ============================================================================
clean_build() {
    echo "🔧 [9/9] Очистка build директорий..."
    
    rm -rf build/ composeApp/build/ .gradle/ .kotlin/ 2>/dev/null || true
    
    echo "  ✅ Очищено"
    echo ""
}

# ============================================================================
# ГЛАВНАЯ ФУНКЦИЯ
# ============================================================================
main() {
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║   ПОЛНОЕ ИСПРАВЛЕНИЕ KMP ПРОЕКТА GEOBLINKER ДЛЯ TERMUX        ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
    echo ""
    echo "📍 Проект: $PROJECT_DIR"
    echo ""
    
    fix_package_imports
    fix_merge_conflicts
    create_android_actuals
    create_ios_actuals
    fix_android_files
    create_platform_files
    update_build_gradle
    fix_di_modules
    clean_build
    
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║                    ✅ ГОТОВО!                                  ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
    echo ""
    echo "📋 СЛЕДУЮЩИЕ ШАГИ:"
    echo ""
    echo "   1. Перейдите в проект:"
    echo "      cd $PROJECT_DIR"
    echo ""
    echo "   2. Дайте права на gradlew:"
    echo "      chmod +x gradlew"
    echo ""
    echo "   3. Запустите сборку:"
    echo "      ./gradlew assembleDebug"
    echo ""
    echo "💡 При ошибках используйте:"
    echo "   ./gradlew clean assembleDebug --stacktrace --info"
    echo ""
}

main
exit 0
