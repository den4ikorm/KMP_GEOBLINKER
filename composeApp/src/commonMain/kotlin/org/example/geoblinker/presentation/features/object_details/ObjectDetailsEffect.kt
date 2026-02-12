package org.example.geoblinker.presentation.features.object_details


import androidx.compose.material3.*
import androidx.compose.runtime.*


/**
 * Side-effects для ObjectDetailsScreen
 */
sealed class ObjectDetailsEffect {
    // Навигация
    data object NavigateBack : ObjectDetailsEffect()
    
    
    // Сообщения
    data class ShowSuccess(val message: String) : ObjectDetailsEffect()
    data class ShowError(val message: String) : ObjectDetailsEffect()
}
