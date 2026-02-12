package org.example.geoblinker.presentation.features.device_list_signal


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Signal


/**
 * State для экрана истории сигналов устройства
 */
data class DeviceListSignalState(
    val isLoading: Boolean = false,
    val deviceId: String = "",
    val deviceName: String = "",
    val signals: List<Signal> = emptyList(),
    val selectedFilter: SignalFilter = SignalFilter.All,
    val groupedSignals: Map<String, List<SignalItem>> = emptyMap(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)


/**
 * Фильтры для сигналов
 */
enum class SignalFilter(val displayName: String) {
    All("Все"),
    Location("Локация"),
    Alert("Тревога"),
    Battery("Батарея"),
    Speed("Скорость")
}


/**
 * Элемент сигнала для отображения
 */
data class SignalItem(
    val id: String,
    val type: String,
    val description: String,
    val timestamp: Long,
    val timeFormatted: String,
    val dateFormatted: String,
    val lat: Double,
    val lng: Double,
    val speed: Double,
    val iconType: SignalIconType,
    val locationText: String
)


/**
 * Типы иконок для сигналов
 */
enum class SignalIconType {
    Location,
    Alert,
    Battery,
    Speed,
    Info
}
