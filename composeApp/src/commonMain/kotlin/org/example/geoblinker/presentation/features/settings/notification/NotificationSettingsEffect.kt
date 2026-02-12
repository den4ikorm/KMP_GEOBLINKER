package org.example.geoblinker.presentation.features.settings/notification




/**
 * Side-effects для настроек уведомлений
 */
sealed class NotificationSettingsEffect {
    data object NavigateBack : NotificationSettingsEffect()
    data class ShowError(val message: String) : NotificationSettingsEffect()
    data class ShowMessage(val message: String) : NotificationSettingsEffect()
}
