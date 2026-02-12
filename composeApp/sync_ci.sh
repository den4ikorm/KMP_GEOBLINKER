#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> Очистка старых воркфлоу..."
rm -rf .github/workflows/*

echo ">>> Создание финального файла .github/workflows/android-ci.yml..."
mkdir -p .github/workflows
cat << 'YML' > .github/workflows/android-ci.yml
name: Android CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - name: Checkout code
      uses: actions/checkout@v4
      
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle
    
    - name: Fix gradlew permissions and JVM options
      run: |
        chmod +x gradlew
        # Исправляем JVM опции прямо в рантайме экшена
        sed -i 's/DEFAULT_JVM_OPTS=.*/DEFAULT_JVM_OPTS="-Xmx2048m -Xms512m"/' gradlew
        
    - name: Build with Gradle
      env:
        GRADLE_OPTS: "-Xmx2048m -XX:MaxMetaspaceSize=512m"
      run: ./gradlew assembleDebug --stacktrace
      
    - name: Upload APK
      uses: actions/upload-artifact@v4
      with:
        name: app-debug
        path: composeApp/build/outputs/apk/debug/*.apk
YML

echo ">>> Проверка ветки..."
CURRENT_BRANCH=$(git branch --show-current)
if [ "$CURRENT_BRANCH" != "main" ]; then
    git branch -M main
fi

echo ">>> Отправка изменений в GitHub..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

git add .
git commit -m "Fix: Final CI sync and path cleanup"
git push origin main --force

echo ">>> ГОТОВО! Теперь зайди на GitHub в раздел Actions."
echo ">>> Если видишь список, выбери 'Android CI' и нажми 'Run workflow'."
