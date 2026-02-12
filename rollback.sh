#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"

cd "$PROJECT_ROOT" || exit

echo ">>> Возвращаемся в стабильное состояние..."
# Отменяем все незакоммиченные изменения и откатываем последний коммит с темой
git reset --hard HEAD~1
# Если мы успели сделать несколько коммитов с темой, можно откатиться еще дальше
# git reset --hard origin/main

echo ">>> Удаляем временные скрипты..."
rm -f global_cleanup.sh fix_code_errors.sh fix_viewmodel.sh get_error.sh

echo ">>> Проверяем статус..."
git status

echo ">>> ГОТОВО. Мы вернулись назад. Теперь будем лечить только конкретные файлы."
