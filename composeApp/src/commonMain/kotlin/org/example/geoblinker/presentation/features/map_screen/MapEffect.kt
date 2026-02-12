package org.example.geoblinker.presentation.features.map_screen


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Devices


/**
 * Side-effects для MapScreen
 * Используются для навигации и одноразовых событий
 */
sealed class MapEffect {
    // Ошибки
    data class ShowError(val message: String) : MapEffect()
    
    
    // Навигация к устройству на карте
    data class ZoomToDevice(val lat: Double, val lng: Double) : MapEffect()
    
    
    // Навигация к локации пользователя
    data class ZoomToUserLocation(val lat: Double, val lng: Double) : MapEffect()
    
    
    // Навигация на другие экраны
    data object NavigateToBinding : MapEffect()
    data class NavigateToDeviceDetails(val device: Devices) : MapEffect()
    
    
    // Запрос разрешений
    data object RequestLocationPermission : MapEffect()
}
