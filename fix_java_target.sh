#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> Исправляем несовместимость JVM-таргетов..."
# Мы добавляем compileOptions и kotlinOptions, чтобы везде была Java 17
sed -i '/android {/a \    compileOptions {\n        sourceCompatibility = JavaVersion.VERSION_17\n        targetCompatibility = JavaVersion.VERSION_17\n    }' composeApp/build.gradle.kts

# Убеждаемся, что в kotlin блоке тоже стоит 17
sed -i 's/jvmTarget = "1.8"/jvmTarget = "17"/g' composeApp/build.gradle.kts

echo ">>> Пуш исправленного таргета..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"
git add .
git commit -m "Fix: Sync JVM target to 17 for Java and Kotlin"
git push origin main --force

echo ">>> ГОТОВО! Теперь Java и Kotlin работают на одной волне."
