package org.example.geoblinker.presentation.features.settings/phone




/**
 * Side-effects для изменения телефона
 */
sealed class PhoneSettingsEffect {
    data object NavigateBack : PhoneSettingsEffect()
    data class ShowError(val message: String) : PhoneSettingsEffect()
    data class ShowMessage(val message: String) : PhoneSettingsEffect()
}
