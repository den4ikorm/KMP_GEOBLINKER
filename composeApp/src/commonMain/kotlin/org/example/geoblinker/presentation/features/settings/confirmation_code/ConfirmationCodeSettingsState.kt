package org.example.geoblinker.presentation.features.settings/confirmation_code




/**
 * State для подтверждения кода
 */
data class ConfirmationCodeSettingsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
