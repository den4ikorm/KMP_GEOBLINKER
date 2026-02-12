package org.example.geoblinker.core.utils




/**
 * Конвертеры из LEGACY_RESOURCES.md
 */
object Converters {
    
    
    /**
     * Координаты приходят в микроградусах (умножены на 10^6)
     * LEGACY: DeviceViewModel.kt:168-169
     */
    fun microdegreesToDegrees(microdegrees: Long): Double {
        return microdegrees / 1_000_000.0
    }
    
    
    /**
     * Скорость приходит в км/ч, конвертируется в м/с
     * LEGACY: DeviceViewModel.kt:170
     */
    fun kmhToMs(speedKmh: Double): Double {
        return speedKmh / 3.6
    }
    
    
    fun msToKmh(speedMs: Double): Double {
        return speedMs * 3.6
    }
}
