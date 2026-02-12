package org.example.geoblinker.presentation.features.other/chats


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для чатов
 */
class ChatsViewModel : BaseViewModel<ChatsState, ChatsEvent, ChatsEffect>(
    initialState = ChatsState()
) {


    override fun handleEvent(event: ChatsEvent) {
        when (event) {
            is ChatsEvent.NavigateBack -> sendEffect(ChatsEffect.NavigateBack)
            is ChatsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
