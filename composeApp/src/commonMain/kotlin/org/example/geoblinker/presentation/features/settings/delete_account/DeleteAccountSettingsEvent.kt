package org.example.geoblinker.presentation.features.settings/delete_account




/**
 * События для удаления аккаунта
 */
sealed class DeleteAccountSettingsEvent {
    data object NavigateBack : DeleteAccountSettingsEvent()
    data object ClearError : DeleteAccountSettingsEvent()
}
