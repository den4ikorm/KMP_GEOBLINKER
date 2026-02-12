package org.example.geoblinker.presentation.features.settings/confirmation_code




/**
 * События для подтверждения кода
 */
sealed class ConfirmationCodeSettingsEvent {
    data object NavigateBack : ConfirmationCodeSettingsEvent()
    data object ClearError : ConfirmationCodeSettingsEvent()
}
