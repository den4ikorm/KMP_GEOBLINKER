package org.example.geoblinker.presentation.features.settings/confirmation_code


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для подтверждения кода
 */
class ConfirmationCodeSettingsViewModel : BaseViewModel<ConfirmationCodeSettingsState, ConfirmationCodeSettingsEvent, ConfirmationCodeSettingsEffect>(
    initialState = ConfirmationCodeSettingsState()
) {


    override fun handleEvent(event: ConfirmationCodeSettingsEvent) {
        when (event) {
            is ConfirmationCodeSettingsEvent.NavigateBack -> sendEffect(ConfirmationCodeSettingsEffect.NavigateBack)
            is ConfirmationCodeSettingsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
