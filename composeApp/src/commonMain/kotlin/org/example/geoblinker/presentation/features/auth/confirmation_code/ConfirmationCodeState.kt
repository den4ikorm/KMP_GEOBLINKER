package org.example.geoblinker.presentation.features.auth/confirmation_code




/**
 * State для подтверждения кода
 */
data class ConfirmationCodeState(
    val isLoading: Boolean = false,
    val error: String? = null
)
