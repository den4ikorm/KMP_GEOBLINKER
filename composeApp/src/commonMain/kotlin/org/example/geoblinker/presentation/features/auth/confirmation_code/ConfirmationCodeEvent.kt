package org.example.geoblinker.presentation.features.auth/confirmation_code




/**
 * События для подтверждения кода
 */
sealed class ConfirmationCodeEvent {
    data object NavigateBack : ConfirmationCodeEvent()
    data object ClearError : ConfirmationCodeEvent()
}
