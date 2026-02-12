package org.example.geoblinker.presentation.features.settings/phone


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для изменения телефона
 */
class PhoneSettingsViewModel : BaseViewModel<PhoneSettingsState, PhoneSettingsEvent, PhoneSettingsEffect>(
    initialState = PhoneSettingsState()
) {


    override fun handleEvent(event: PhoneSettingsEvent) {
        when (event) {
            is PhoneSettingsEvent.NavigateBack -> sendEffect(PhoneSettingsEffect.NavigateBack)
            is PhoneSettingsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
