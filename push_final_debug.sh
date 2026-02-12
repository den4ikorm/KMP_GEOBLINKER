#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> Обновляем CI, чтобы он выплеснул ошибки компиляции..."
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
        ./gradlew assembleDebug --no-daemon --stacktrace || (echo "!!! ОШИБКА ОБНАРУЖЕНА !!!" && ./gradlew assembleDebug --no-daemon --quiet)
YML

git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"
git add .
git commit -m "CI: Force error output on failure"
git push origin main --force

echo ">>> ОТПРАВЛЕНО В ОБЛАКО!"
echo ">>> Через 3-4 минуты загляни в GitHub. В самом конце лога будет блок после слов '!!! ОШИБКА ОБНАРУЖЕНА !!!'."
