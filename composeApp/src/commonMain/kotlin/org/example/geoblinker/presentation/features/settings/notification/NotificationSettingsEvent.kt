package org.example.geoblinker.presentation.features.settings/notification




/**
 * События для настроек уведомлений
 */
sealed class NotificationSettingsEvent {
    data object NavigateBack : NotificationSettingsEvent()
    data object ClearError : NotificationSettingsEvent()
}
