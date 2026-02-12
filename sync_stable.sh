#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

echo ">>> 1. Синхронизируем облако со стабильной локальной версией..."
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"
# Форсированно возвращаем GitHub к нашей стабильной точке
git push origin main --force

echo ">>> 2. Исправляем ТОЛЬКО импорты в проблемных файлах (без смены темы)..."
# Мы просто добавим недостающие импорты, не трогая Material 2
FILES=("composeApp/src/commonMain/kotlin/org/example/geoblinker/presentation/features/map_screen/widgets/CustomDevicePopup.kt" "composeApp/src/commonMain/kotlin/org/example/geoblinker/presentation/features/map_screen/widgets/DevicePopupUpdated.kt")

for FILE in "${FILES[@]}"; do
    if [ -f "$FILE" ]; then
        sed -i '/package /a import androidx.compose.material.*\nimport androidx.compose.runtime.*\nimport androidx.compose.ui.*\nimport androidx.compose.foundation.layout.*' "$FILE"
    fi
done

echo ">>> 3. Отправляем точечные правки..."
git add .
git commit -m "Fix: Add missing imports to Map Widgets without changing theme"
git push origin main

echo ">>> ВСЁ. Теперь билд в облаке должен пойти чисто."
