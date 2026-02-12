package org.example.geoblinker.presentation.features.settings/confirmation_code




/**
 * Side-effects для подтверждения кода
 */
sealed class ConfirmationCodeSettingsEffect {
    data object NavigateBack : ConfirmationCodeSettingsEffect()
    data class ShowError(val message: String) : ConfirmationCodeSettingsEffect()
    data class ShowMessage(val message: String) : ConfirmationCodeSettingsEffect()
}
