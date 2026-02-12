package org.example.geoblinker.presentation.features.settings/delete_account




/**
 * Side-effects для удаления аккаунта
 */
sealed class DeleteAccountSettingsEffect {
    data object NavigateBack : DeleteAccountSettingsEffect()
    data class ShowError(val message: String) : DeleteAccountSettingsEffect()
    data class ShowMessage(val message: String) : DeleteAccountSettingsEffect()
}
