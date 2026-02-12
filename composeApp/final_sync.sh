#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> 1. Очистка и создание правильного build.gradle.kts (Корень)..."
# Создаем чистый корневой файл
cat << 'KTS' > build.gradle.kts
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.23" apply false
    id("org.jetbrains.compose") version "1.6.1" apply false
    id("com.squareup.sqldelight") version "2.0.1" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
KTS

echo ">>> 2. Синхронизация GitHub Actions воркфлоу..."
mkdir -p .github/workflows
cat << 'YML' > .github/workflows/android-ci.yml
name: Android CI
on:
  push:
    branches: [ main, develop ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle
    
    - name: Fix permissions
      run: chmod +x gradlew

    - name: Build APK
      run: ./gradlew assembleDebug --stacktrace
      env:
        GRADLE_OPTS: "-Xmx2048m"

    - name: Upload APK
      uses: actions/upload-artifact@v4
      with:
        name: app-debug
        path: composeApp/build/outputs/apk/debug/*.apk
YML

echo ">>> 3. Принудительное обновление репозитория..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

git add .
git commit -m "Fix: Remove duplicate plugins block and sync CI"
git push origin main --force

echo ">>> ГОТОВО! Проверь вкладку Actions в GitHub."
