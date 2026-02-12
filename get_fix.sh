#!/bin/bash
LOG="errors.txt"
if [ -f "$LOG" ]; then
    echo ">>> НАЙДЕНЫ СЛЕДУЮЩИЕ ОШИБКИ:"
    grep "e: file" "$LOG" | head -n 5
    
    echo ">>> КОНТЕКСТ ПЕРВОЙ ОШИБКИ:"
    FIRST_ERROR=$(grep -m 1 "e: file" "$LOG" | cut -d: -f2- | cut -d: -f1)
    LINE_NUMBER=$(grep -m 1 "e: file" "$LOG" | cut -d: -f3)
    
    if [ ! -z "$FIRST_ERROR" ]; then
        echo "Файл: $FIRST_ERROR"
        echo "Строка: $LINE_NUMBER"
        # Показываем 5 строк до и после ошибки
        START=$((LINE_NUMBER - 5))
        [ $START -lt 1 ] && START=1
        sed -n "${START},$((LINE_NUMBER + 5))p" "$FIRST_ERROR" | cat -n
    fi
else
    echo "Файл errors.txt не найден. Сначала запусти Шаг 1."
fi
