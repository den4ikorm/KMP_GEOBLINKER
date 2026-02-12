package org.example.geoblinker.presentation.features.settings/phone




/**
 * События для изменения телефона
 */
sealed class PhoneSettingsEvent {
    data object NavigateBack : PhoneSettingsEvent()
    data object ClearError : PhoneSettingsEvent()
}
