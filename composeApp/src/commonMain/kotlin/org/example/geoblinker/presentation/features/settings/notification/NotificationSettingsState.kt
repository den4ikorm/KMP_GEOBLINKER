package org.example.geoblinker.presentation.features.settings/notification




/**
 * State для настроек уведомлений
 */
data class NotificationSettingsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
