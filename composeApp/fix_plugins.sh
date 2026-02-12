#!/bin/bash
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"

cd "$PROJECT_ROOT" || exit

echo ">>> Исправляю корневой build.gradle.kts (добавляю SQLDelight)..."
# Добавляем плагин SQLDelight в блок plugins корневого файла, если его там нет
# Используем версию 2.0.1 (стабильная для Kotlin 1.9+)
sed -i '1s/^/plugins {\n    id("com.squareup.sqldelight") version "2.0.1" apply false\n}\n/' build.gradle.kts

echo ">>> Проверяю настройки репозиториев в settings.gradle.kts..."
# Убеждаемся, что есть mavenCentral
if ! grep -q "mavenCentral()" settings.gradle.kts; then
    sed -i '1s/^/pluginManagement {\n    repositories {\n        google()\n        mavenCentral()\n        gradlePluginPortal()\n    }\n}\n/' settings.gradle.kts
fi

echo ">>> Отправляю исправления в GitHub..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

git add .
git commit -m "Fix: Add SQLDelight version to root project and update pluginManagement"
git push origin main --force

echo ">>> ГОТОВО! Теперь облако найдет плагин. Проверяй Actions."
