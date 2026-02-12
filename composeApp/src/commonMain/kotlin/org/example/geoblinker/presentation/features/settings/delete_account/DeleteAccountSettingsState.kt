package org.example.geoblinker.presentation.features.settings/delete_account




/**
 * State для удаления аккаунта
 */
data class DeleteAccountSettingsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
