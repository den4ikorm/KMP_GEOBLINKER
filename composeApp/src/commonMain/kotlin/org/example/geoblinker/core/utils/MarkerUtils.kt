package org.example.geoblinker.core.utils




/**
 * Утилиты для маркеров на карте
 * Из LEGACY_RESOURCES.md:295-312
 */
object MarkerUtils {
    
    
    /**
     * Получение имени файла маркера по ID
     * markerId: 0-14
     * 
     * Маркеры:
     * m_0 - круглый маркер
     * m_1 - автомобиль
     * m_2 - мотоцикл
     * m_3 - грузовик
     * m_4 - камион
     * m_5 - автобус
     * m_6 - бульдозер
     * m_7 - трактор
     * m_8 - прицеп
     * m_9 - человек
     * m_10 - ребенок
     * m_11 - кошка
     * m_12 - собака
     * m_13 - лошадь
     * m_14 - корова
     */
    fun getMarkerFilename(markerId: Long): String {
        return when (markerId) {
            in 0..14 -> "m_${markerId}.svg"
            else -> "m_0.svg" // Default круглый маркер
        }
    }
    
    
    /**
     * Получение описания маркера
     */
    fun getMarkerDescription(markerId: Long): String {
        return when (markerId) {
            0L -> "Маркер"
            1L -> "Автомобиль"
            2L -> "Мотоцикл"
            3L -> "Грузовик"
            4L -> "Камион"
            5L -> "Автобус"
            6L -> "Бульдозер"
            7L -> "Трактор"
            8L -> "Прицеп"
            9L -> "Человек"
            10L -> "Ребенок"
            11L -> "Кошка"
            12L -> "Собака"
            13L -> "Лошадь"
            14L -> "Корова"
            else -> "Неизвестно"
        }
    }
    
    
    /**
     * Проверка валидности координат
     */
    fun isValidCoordinates(lat: Double, lng: Double): Boolean {
        return lat in -90.0..90.0 && 
               lng in -180.0..180.0 &&
               lat != 0.0 && 
               lng != 0.0 &&
               lat != -999999999.9 &&
               lng != -999999999.9
    }
}
