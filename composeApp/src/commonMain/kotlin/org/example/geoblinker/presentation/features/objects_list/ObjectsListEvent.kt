package org.example.geoblinker.presentation.features.objects_list


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Devices


/**
 * События для ObjectsListScreen
 * Описывают намерения пользователя
 */
sealed class ObjectsListEvent {
    // Выбор устройства
    data class OnDeviceClick(val device: Devices) : ObjectsListEvent()
    
    
    // Поиск
    data class OnSearchQueryChanged(val query: String) : ObjectsListEvent()
    
    
    // Сортировка
    data class OnSortTypeChanged(val sortType: SortType) : ObjectsListEvent()
    data object OnSortDialogToggle : ObjectsListEvent()
    
    
    // Обновление локации
    data class OnUserLocationUpdated(val lat: Double, val lng: Double) : ObjectsListEvent()
    
    
    // Обновление списка
    data object OnRefresh : ObjectsListEvent()
    
    
    // Навигация
    data object OnNavigateToBinding : ObjectsListEvent()
}
