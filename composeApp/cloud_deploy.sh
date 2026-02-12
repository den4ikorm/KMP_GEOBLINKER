#!/bin/bash
# 1. Настройка переменных
GH_USER="den4ikorm"
GH_EMAIL="olyaalekseevna83@gmail.com"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"

# 2. Создаем директорию для GitHub Actions, если её нет
mkdir -p "$PROJECT_ROOT/.github/workflows"

# 3. Создаем файл конфигурации сборки в облаке
cat << 'GITHUB' > "$PROJECT_ROOT/.github/workflows/android_build.yml"
name: Android Build
on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Build with Gradle
        run: ./gradlew assembleDebug

      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: app-debug
          path: composeApp/build/outputs/apk/debug/*.apk
GITHUB

# 4. Настройка Git и пуш
cd "$PROJECT_ROOT"
git config --global --add safe.directory "$PROJECT_ROOT"
git config --global user.email "$GH_EMAIL"
git config --global user.name "$GH_USER"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

echo "Отправка кода в облако для сборки..."
git add .
git commit -m "Setup GitHub Actions for cloud build"
git push origin main --force

echo "ГОТОВО! Зайди на https://github.com/den4ikorm/KMP_GEOBLINKER/actions"
echo "Там началась автоматическая сборка твоего APK."
