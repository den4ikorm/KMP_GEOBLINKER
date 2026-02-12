#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> 1. Урезаем аппетиты Java до критического минимума..."
cat << 'PROPS' > gradle.properties
org.gradle.jvmargs=-Xmx1536m -XX:MaxMetaspaceSize=512m
org.gradle.daemon=false
org.gradle.parallel=false
org.gradle.cached=false
android.useAndroidX=true
android.nonTransitiveRClass=true
PROPS

echo ">>> 2. Упрощаем воркфлоу (убираем лишние флаги)..."
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
        ./gradlew assembleDebug --no-daemon --no-build-cache --stacktrace
YML

echo ">>> 3. Принудительный пуш..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"
git add .
git commit -m "Build: Drastic memory reduction for survival"
git push origin main --force

echo ">>> ГОТОВО! Сейчас мы дали Gradle всего 1.5 ГБ. Это мало, но это может спасти от OOM Killer."
