#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> 1. Исправляем корневой build.gradle.kts..."
cat << 'KTS' > build.gradle.kts
plugins {
    // Используем правильные ID и версии
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
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
KTS

echo ">>> 2. Проверяем composeApp/build.gradle.kts на наличие старого ID..."
# SQLDelight сменил ID с com.squareup на app.cash
if [ -f "composeApp/build.gradle.kts" ]; then
    sed -i 's/com.squareup.sqldelight/app.cash.sqldelight/g' composeApp/build.gradle.kts
fi

echo ">>> 3. Пуш в GitHub..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

git add .
git commit -m "Fix: Update SQLDelight plugin ID and repository"
git push origin main --force

echo ">>> ГОТОВО! Сейчас билд на GitHub должен пройти стадию плагинов."
