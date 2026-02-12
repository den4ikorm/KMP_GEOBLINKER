package org.example.geoblinker.presentation.features.notifications


import androidx.compose.material3.*
import androidx.compose.runtime.*


/**
 * События для уведомлений
 */
sealed class NotificationsEvent {
    data object NavigateBack : NotificationsEvent()
    data object ClearError : NotificationsEvent()
}
