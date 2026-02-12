package org.example.geoblinker.presentation.features.map_screen


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Devices


/**
 * События для MapScreen
 * Описывают НАМЕРЕНИЯ пользователя, не действия
 */
sealed class MapEvent {
    // Выбор устройства
    data class OnDeviceSelected(val device: Devices?) : MapEvent()
    
    
    // Обновление локации пользователя
    data class OnUserLocationUpdated(val lat: Double, val lng: Double) : MapEvent()
    
    
    // Смена темы карты
    data class OnThemeChanged(val theme: MapTheme) : MapEvent()
    
    
    // Поиск устройства
    data class OnSearchDevice(val query: String) : MapEvent()
    data object OnSearchPopupToggle : MapEvent()
    
    
    // Режим слежения
    data object OnFollowModeToggle : MapEvent()
    
    
    // Обновление устройств
    data object OnRefreshDevices : MapEvent()
    
    
    // Запрос локации
    data object OnRequestLocation : MapEvent()
    
    
    // Управление зумом
    data object OnZoomIn : MapEvent()
    data object OnZoomOut : MapEvent()
    
    
    // Попап пустых устройств
    data object OnEmptyDevicesPopupDismiss : MapEvent()
    data object OnNavigateToBinding : MapEvent()
}
