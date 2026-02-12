package org.example.geoblinker.presentation.features.auth/phone




/**
 * State для ввода телефона
 */
data class PhoneState(
    val isLoading: Boolean = false,
    val error: String? = null
)
