package org.example.geoblinker.presentation.features.device_list_signal


import androidx.compose.material3.*
import androidx.compose.runtime.*


/**
 * Side-effects для экрана истории сигналов
 */
sealed class DeviceListSignalEffect {
    /**
     * Навигация назад
     */
    data object NavigateBack : DeviceListSignalEffect()
    
    
    /**
     * Показать сообщение об ошибке
     */
    data class ShowError(val message: String) : DeviceListSignalEffect()
    
    
    /**
     * Показать уведомление
     */
    data class ShowMessage(val message: String) : DeviceListSignalEffect()
}
