package org.example.geoblinker.domain.models


import kotlinx.serialization.Serializable


/**
 * Объединенная модель устройства из Main API + IMEI API
 * Используется для отображения на карте и в списках
 */
@Serializable
data class SyncedDevice(
    // === FROM MAIN API ===
    val id: String,                      // ID из основного API
    val name: String,                    // Название устройства
    val latitude: Double?,               // Широта (может быть null)
    val longitude: Double?,              // Долгота (может быть null)
    val carId: Long?,                    // ID автомобиля
    
    
    // === FROM IMEI API ===
    val imei: String?,                   // IMEI трекера
    val speed: Double?,                  // Скорость (км/ч)
    val altitude: Int?,                  // Высота (метры)
    val lastUpdate: Long?,               // Timestamp последнего обновления
    val gpsQuality: Int?,                // Качество GPS сигнала
    val battery: Int?,                   // Уровень батареи (%)
    
    
    // === MERGED DATA ===
    val markerType: String = "m_0",      // Тип маркера на карте
    val source: DeviceSource,            // Источник данных
    val isSynced: Boolean = false,       // Успешно ли синхронизировано
    val syncError: String? = null        // Ошибка синхронизации (если есть)
)


/**
 * Источник данных устройства
 */
@Serializable
enum class DeviceSource {
    MAIN_API,          // Только из основного API
    IMEI_API,          // Только из IMEI API
    SYNCED             // Объединенные данные
}


/**
 * Extension функции для удобства
 */
fun SyncedDevice.hasValidCoordinates(): Boolean {
    return latitude != null && longitude != null &&
           latitude in -90.0..90.0 &&
           longitude in -180.0..180.0
}


fun SyncedDevice.isMoving(): Boolean {
    return speed != null && speed > 1.0  // Движется если скорость > 1 км/ч
}


fun SyncedDevice.getDisplaySpeed(): String {
    return speed?.let { "${it.toInt()} км/ч" } ?: "—"
}


fun SyncedDevice.getDisplayLastUpdate(): String {
    if (lastUpdate == null) return "Нет данных"
    
    
    return try {
        val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(lastUpdate)
        val localDateTime = instant.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        
        
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
        val year = localDateTime.year
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        
        
        "$day.$month.$year $hour:$minute"
    } catch (e: Exception) {
        "Ошибка даты"
    }
}


fun SyncedDevice.getBatteryIcon(): String {
    return when {
        battery == null -> "🔋"
        battery >= 80 -> "🔋"
        battery >= 50 -> "🔋"
        battery >= 20 -> "🪫"
        else -> "🪫"
    }
}


fun SyncedDevice.getGpsQualityText(): String {
    return when (gpsQuality) {
        null -> "Нет данных"
        in 0..20 -> "Слабый"
        in 21..50 -> "Средний"
        in 51..80 -> "Хороший"
        else -> "Отличный"
    }
}
