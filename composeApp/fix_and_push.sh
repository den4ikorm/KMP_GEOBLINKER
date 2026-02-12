#!/bin/bash
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"

cd "$PROJECT_ROOT"

echo ">>> Исправляю gradle.properties (убираю кавычки в jvmargs)..."
# Эта команда ищет строку с -Xmx и убирает кавычки, если они есть
sed -i 's/"-Xmx\([0-9a-zA-Z]*\)"/-Xmx\1/g' gradle.properties
# Также проверим стандартную строку org.gradle.jvmargs
sed -i 's/org.gradle.jvmargs=-Xmx2048m/org.gradle.jvmargs=-Xmx2048m/g' gradle.properties

echo ">>> Настраиваю Git..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

echo ">>> Отправляю исправления в облако..."
git add gradle.properties
git commit -m "Fix: remove quotes from jvmargs to fix ClassNotFoundException"
git push origin main

echo ">>> ГОТОВО! Теперь проверь GitHub Actions снова."
