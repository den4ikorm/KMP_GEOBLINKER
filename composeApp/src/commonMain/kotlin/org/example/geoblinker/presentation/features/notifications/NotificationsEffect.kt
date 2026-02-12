package org.example.geoblinker.presentation.features.notifications


import androidx.compose.material3.*
import androidx.compose.runtime.*


/**
 * Side-effects для уведомлений
 */
sealed class NotificationsEffect {
    data object NavigateBack : NotificationsEffect()
    data class ShowError(val message: String) : NotificationsEffect()
    data class ShowMessage(val message: String) : NotificationsEffect()
}
