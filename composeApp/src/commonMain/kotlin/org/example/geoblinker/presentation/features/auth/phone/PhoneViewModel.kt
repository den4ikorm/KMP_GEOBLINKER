package org.example.geoblinker.presentation.features.auth/phone


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для ввода телефона
 */
class PhoneViewModel : BaseViewModel<PhoneState, PhoneEvent, PhoneEffect>(
    initialState = PhoneState()
) {


    override fun handleEvent(event: PhoneEvent) {
        when (event) {
            is PhoneEvent.NavigateBack -> sendEffect(PhoneEffect.NavigateBack)
            is PhoneEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
