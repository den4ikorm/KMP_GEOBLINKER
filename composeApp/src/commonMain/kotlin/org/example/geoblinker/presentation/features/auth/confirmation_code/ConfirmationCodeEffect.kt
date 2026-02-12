package org.example.geoblinker.presentation.features.auth/confirmation_code




/**
 * Side-effects для подтверждения кода
 */
sealed class ConfirmationCodeEffect {
    data object NavigateBack : ConfirmationCodeEffect()
    data class ShowError(val message: String) : ConfirmationCodeEffect()
    data class ShowMessage(val message: String) : ConfirmationCodeEffect()
}
