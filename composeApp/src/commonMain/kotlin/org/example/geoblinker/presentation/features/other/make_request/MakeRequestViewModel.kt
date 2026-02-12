package org.example.geoblinker.presentation.features.other/make_request


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для создания заявки
 */
class MakeRequestViewModel : BaseViewModel<MakeRequestState, MakeRequestEvent, MakeRequestEffect>(
    initialState = MakeRequestState()
) {


    override fun handleEvent(event: MakeRequestEvent) {
        when (event) {
            is MakeRequestEvent.NavigateBack -> sendEffect(MakeRequestEffect.NavigateBack)
            is MakeRequestEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
