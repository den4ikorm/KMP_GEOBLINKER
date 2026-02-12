#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> 1. Удаление всех скрытых настроек..."
find . -name "gradle.properties" -type f -delete

echo ">>> 2. Создание единого сверхлегкого конфига..."
cat << 'PROPS' > gradle.properties
# Минимализм для облака
org.gradle.jvmargs=-Xmx2048m
org.gradle.daemon=false
org.gradle.parallel=false
org.gradle.vfs.watch=false
android.useAndroidX=true
android.nonTransitiveRClass=true
PROPS

echo ">>> 3. Обновление воркфлоу (убираем всё лишнее)..."
mkdir -p .github/workflows
cat << 'YML' > .github/workflows/android-ci.yml
name: Android CI
on:
  push:
    branches: [ main ]
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
    
    - name: Build
      run: |
        chmod +x gradlew
        ./gradlew assembleDebug --stacktrace
YML

echo ">>> 4. Принудительный пуш..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"
git add .
git commit -m "Fix: Final attempt to kill background daemons and reset properties"
git push origin main --force

echo ">>> ГОТОВО! Если и это не поможет, значит ошибка сидит глубоко в зависимостях composeApp."
