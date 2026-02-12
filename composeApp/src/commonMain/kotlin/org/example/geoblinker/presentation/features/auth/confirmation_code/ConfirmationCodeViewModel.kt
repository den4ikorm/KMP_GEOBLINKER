package org.example.geoblinker.presentation.features.auth/confirmation_code


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для подтверждения кода
 */
class ConfirmationCodeViewModel : BaseViewModel<ConfirmationCodeState, ConfirmationCodeEvent, ConfirmationCodeEffect>(
    initialState = ConfirmationCodeState()
) {


    override fun handleEvent(event: ConfirmationCodeEvent) {
        when (event) {
            is ConfirmationCodeEvent.NavigateBack -> sendEffect(ConfirmationCodeEffect.NavigateBack)
            is ConfirmationCodeEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
