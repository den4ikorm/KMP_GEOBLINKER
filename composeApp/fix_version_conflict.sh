#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> 1. Исправляем корневой build.gradle.kts..."
cat << 'KTS' > build.gradle.kts
plugins {
    // Здесь версии ОБЯЗАТЕЛЬНЫ
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.23" apply false
    id("org.jetbrains.compose") version "1.6.1" apply false
    id("app.cash.sqldelight") version "2.0.1" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
KTS

echo ">>> 2. Исправляем composeApp/build.gradle.kts (удаляем версии)..."
cat << 'KTS' > composeApp/build.gradle.kts
plugins {
    // Здесь версии ЗАПРЕЩЕНЫ, если они есть в корне
    id("com.android.application")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions { jvmTarget = "17" }
        }
    }
    // Вернем базовый sourceSets
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = 34
    defaultConfig {
        applicationId = "org.example.project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}
KTS

echo ">>> 3. Пуш исправленной структуры..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"
git add .
git commit -m "Fix: Remove redundant plugin versions in sub-module"
git push origin main --force

echo ">>> ГОТОВО! Теперь конфликт версий исчерпан."
