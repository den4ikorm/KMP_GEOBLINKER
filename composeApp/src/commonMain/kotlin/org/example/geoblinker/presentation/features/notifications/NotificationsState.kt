package org.example.geoblinker.presentation.features.notifications


import androidx.compose.material3.*
import androidx.compose.runtime.*


/**
 * State для уведомлений
 */
data class NotificationsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
