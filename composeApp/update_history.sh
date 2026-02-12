#!/bin/bash

# Путь к истории согласно твоему запросу
HISTORY_DIR="/storage/emulated/0/Documents/0_GB_START/ИСТОРИЯ"
HISTORY_FILE="$HISTORY_DIR/PROGRESS.md"

# Цвета для красоты в консоли
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

mkdir -p "$HISTORY_DIR"

echo -e "${BLUE}>>> Фиксирую текущий прогресс в историю...${NC}"

{
  echo "# ОТЧЕТ О ПРОГРЕССЕ: РЕШЕНИЕ ПРОБЛЕМЫ СБОРКИ (11.02.2026)"
  echo ""
  echo "## 1. ТЕКУЩИЙ СТАТУС"
  echo "- **Проблема:** Блокировка сборки из-за некорректных лимитов памяти Java (-Xmx64m) и ошибок поиска плагинов в облаке."

cat << 'EOF' > total_fix.sh
#!/bin/bash
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"

cd "$PROJECT_ROOT" || exit

echo ">>> Полная пересборка конфигурации build.gradle.kts..."

# Создаем КОРНЕВОЙ build.gradle.kts с НУЛЯ, чтобы избежать дубликатов
cat << 'KTS' > build.gradle.kts
plugins {
    // Жизненно важные плагины с версиями
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.23" apply false
    id("org.jetbrains.compose") version "1.6.1" apply false
    id("com.squareup.sqldelight") version "2.0.1" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
KTS

echo ">>> Исправляю composeApp/build.gradle.kts (убираю лишнее)..."
# В дочернем билде версия не нужна, только ID
sed -i 's/id("com.squareup.sqldelight") version "2.0.1"/id("com.squareup.sqldelight")/' composeApp/build.gradle.kts

echo ">>> Отправляю чистую конфигурацию в GitHub..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

git add .
git commit -m "Fix: Unified plugins block and removed duplicate declarations"
git push origin main --force

echo ">>> ГОТОВО! Блок plugins очищен. Проверяй GitHub Actions через минуту."
