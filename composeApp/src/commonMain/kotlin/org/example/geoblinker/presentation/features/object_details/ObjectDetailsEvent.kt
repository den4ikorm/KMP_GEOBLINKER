package org.example.geoblinker.presentation.features.object_details


import androidx.compose.material3.*
import androidx.compose.runtime.*


/**
 * События для ObjectDetailsScreen
 */
sealed class ObjectDetailsEvent {
    // Загрузка данных
    data class OnLoadDevice(val deviceId: String) : ObjectDetailsEvent()
    
    
    // Навигация
    data object OnBackClicked : ObjectDetailsEvent()
    data object OnEditClicked : ObjectDetailsEvent()
    
    
    // Обновление данных
    data class OnNameChanged(val name: String) : ObjectDetailsEvent()
    data class OnIconChanged(val iconId: String) : ObjectDetailsEvent()
    
    
    // Удаление
    data object OnDeleteClicked : ObjectDetailsEvent()
    data object OnDeleteConfirmed : ObjectDetailsEvent()
    data object OnDeleteDialogDismissed : ObjectDetailsEvent()
    
    
    // Сохранение
    data object OnSaveClicked : ObjectDetailsEvent()
    
    
    // Повторная попытка
    data object OnRetry : ObjectDetailsEvent()
}
