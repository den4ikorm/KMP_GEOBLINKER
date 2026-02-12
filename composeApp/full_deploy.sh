#!/bin/bash
# 1. Настройка переменных
GH_USER="den4ikorm"
GH_EMAIL="olyaalekseevna83@gmail.com"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"

# Цвета
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}>>> Исправляю права Git и перехожу в корень...${NC}"
git config --global --add safe.directory "$PROJECT_ROOT"
git config --global --add safe.directory "$PROJECT_ROOT/composeApp"
cd "$PROJECT_ROOT" || exit

echo -e "${GREEN}>>> Настраиваю авторизацию...${NC}"
git config --global user.email "$GH_EMAIL"
git config --global user.name "$GH_USER"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"

echo -e "${GREEN}>>> Запускаю сборку Android через SH (обход Permission denied)...${NC}"
# Запускаем через sh, так как chmod +x в этой папке не работает
sh gradlew clean assembleDebug

if [ $? -eq 0 ]; then
    echo -e "${GREEN}>>> Сборка SUCCESS. Готовлю пуш...${NC}"
    git add .
    timestamp=$(date +"%Y-%m-%d %H:%M")
    git commit -m "Build & Push: $timestamp"
    git push origin main
    echo -e "${GREEN}>>> ВСЁ ГОТОВО! Проверь GitHub.${NC}"
else
    echo -e "${RED}>>> СБОРКА УПАЛА. Проверь ошибки в консоли выше.${NC}"
    exit 1
fi
