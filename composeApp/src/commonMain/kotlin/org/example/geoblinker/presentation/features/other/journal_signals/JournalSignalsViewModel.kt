package org.example.geoblinker.presentation.features.other/journal_signals


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для журнала сигналов
 */
class JournalSignalsViewModel : BaseViewModel<JournalSignalsState, JournalSignalsEvent, JournalSignalsEffect>(
    initialState = JournalSignalsState()
) {


    override fun handleEvent(event: JournalSignalsEvent) {
        when (event) {
            is JournalSignalsEvent.NavigateBack -> sendEffect(JournalSignalsEffect.NavigateBack)
            is JournalSignalsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
