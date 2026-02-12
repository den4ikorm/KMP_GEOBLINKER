#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> Обновляем воркфлоу с явным chmod..."
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
        cache: gradle
    
    - name: Fix Permissions
      run: chmod +x gradlew

    - name: Build APK
      run: ./gradlew assembleDebug --no-daemon --stacktrace
      env:
        GRADLE_OPTS: "-Xmx3072m -Dorg.gradle.daemon=false"
YML

echo ">>> Синхронизация с GitHub..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"
git add .
git commit -m "CI: Fix permission denied for gradlew"
git push origin main --force

echo ">>> ГОТОВО! Проверяй Actions. Теперь ошибка 126 должна исчезнуть."
