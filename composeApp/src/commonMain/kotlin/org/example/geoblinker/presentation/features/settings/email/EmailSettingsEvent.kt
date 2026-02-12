package org.example.geoblinker.presentation.features.settings/email




/**
 * События для изменения email
 */
sealed class EmailSettingsEvent {
    data object NavigateBack : EmailSettingsEvent()
    data object ClearError : EmailSettingsEvent()
}
