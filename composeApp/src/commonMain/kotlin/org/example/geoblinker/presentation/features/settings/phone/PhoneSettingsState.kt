package org.example.geoblinker.presentation.features.settings/phone




/**
 * State для изменения телефона
 */
data class PhoneSettingsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
