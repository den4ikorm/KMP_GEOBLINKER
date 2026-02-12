package org.example.geoblinker.presentation.features.device_list_signal


import androidx.compose.material3.*
import androidx.compose.runtime.*


/**
 * События для экрана истории сигналов
 */
sealed class DeviceListSignalEvent {
    /**
     * Загрузить сигналы для устройства
     */
    data class LoadSignals(val deviceId: String) : DeviceListSignalEvent()
    
    
    /**
     * Изменить фильтр
     */
    data class ChangeFilter(val filter: SignalFilter) : DeviceListSignalEvent()
    
    
    /**
     * Обновить список (pull-to-refresh)
     */
    data object Refresh : DeviceListSignalEvent()
    
    
    /**
     * Назад
     */
    data object NavigateBack : DeviceListSignalEvent()
    
    
    /**
     * Очистить ошибку
     */
    data object ClearError : DeviceListSignalEvent()
}
