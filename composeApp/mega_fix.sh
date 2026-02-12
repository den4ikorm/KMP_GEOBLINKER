#!/bin/bash
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"

cd "$PROJECT_ROOT" || exit

echo ">>> Массовая очистка некорректных кавычек в jvmargs..."
# Ищем во всех файлах строки "-Xmx..." и меняем на -Xmx... без кавычек
find . -type f \( -name "*.properties" -o -name "*.kts" -o -name "*.sh" -o -name "gradlew" \) -exec sed -i 's/-Xmx2048m/-Xmx2048m/g' {} +
find . -type f \( -name "*.properties" -o -name "*.kts" -o -name "*.sh" -o -name "gradlew" \) -exec sed -i "s/-Xmx2048m/-Xmx2048m/g" {} +

echo ">>> Проверка файла gradlew..."
# Часто ошибка сидит в самом скрипте запуска gradlew
sed -i 's/DEFAULT_JVM_OPTS=.*/DEFAULT_JVM_OPTS="-Xmx2048m -Xms512m"/g' gradlew

echo ">>> Настройка Git и принудительный пуш..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

git add .
git commit -m "Fix: global removal of quoted jvmargs and gradlew fix"
git push origin main --force

echo ">>> ГОТОВО! Сейчас облако ТОЧНО должно скушать код. Проверяй Actions."
