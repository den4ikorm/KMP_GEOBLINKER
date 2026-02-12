package org.example.geoblinker.presentation.features.settings/delete_account


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для удаления аккаунта
 */
class DeleteAccountSettingsViewModel : BaseViewModel<DeleteAccountSettingsState, DeleteAccountSettingsEvent, DeleteAccountSettingsEffect>(
    initialState = DeleteAccountSettingsState()
) {


    override fun handleEvent(event: DeleteAccountSettingsEvent) {
        when (event) {
            is DeleteAccountSettingsEvent.NavigateBack -> sendEffect(DeleteAccountSettingsEffect.NavigateBack)
            is DeleteAccountSettingsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
