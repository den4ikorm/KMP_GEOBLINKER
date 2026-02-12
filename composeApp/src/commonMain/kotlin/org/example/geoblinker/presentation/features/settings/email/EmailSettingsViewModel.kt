package org.example.geoblinker.presentation.features.settings/email


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для изменения email
 */
class EmailSettingsViewModel : BaseViewModel<EmailSettingsState, EmailSettingsEvent, EmailSettingsEffect>(
    initialState = EmailSettingsState()
) {


    override fun handleEvent(event: EmailSettingsEvent) {
        when (event) {
            is EmailSettingsEvent.NavigateBack -> sendEffect(EmailSettingsEffect.NavigateBack)
            is EmailSettingsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
