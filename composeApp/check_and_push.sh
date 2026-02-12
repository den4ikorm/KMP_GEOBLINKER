#!/bin/bash

# Цвета для вывода
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}Начинаю проверку проекта перед пушем...${NC}"

# 1. Очистка и сборка
echo "Сборка Android Debug APK..."
./gradlew clean assembleDebug

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Сборка прошла успешно!${NC}"
    
    # 2. Добавление всех изменений
    git add .
    
    # 3. Коммит с текущей датой и статусом
    timestamp=$(date +"%Y-%m-%d %H:%M")
    commit_msg="Auto-build success: $timestamp"
    git commit -m "$commit_msg"
    
    # 4. Пуш
    echo "Отправка в репозиторий..."
    git push origin main
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Проект успешно проверен и отправлен в GitHub!${NC}"
    else
        echo -e "${RED}Ошибка при пуше. Проверь SSH/интернет.${NC}"
    fi
else
    echo -e "${RED}Сборка упала. Пуш отменен. Исправь ошибки в коде.${NC}"
    exit 1
fi
