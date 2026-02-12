package org.example.geoblinker.presentation.features.objects_list


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Devices


/**
 * Side-effects для ObjectsListScreen
 */
sealed class ObjectsListEffect {
    // Навигация
    data class NavigateToDeviceDetails(val device: Devices) : ObjectsListEffect()
    data object NavigateToBinding : ObjectsListEffect()
    
    
    // Ошибки
    data class ShowError(val message: String) : ObjectsListEffect()
    
    
    // Запрос разрешений
    data object RequestLocationPermission : ObjectsListEffect()
}
