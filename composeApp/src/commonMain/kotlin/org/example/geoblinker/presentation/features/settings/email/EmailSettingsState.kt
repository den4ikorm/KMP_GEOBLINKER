package org.example.geoblinker.presentation.features.settings/email




/**
 * State для изменения email
 */
data class EmailSettingsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
