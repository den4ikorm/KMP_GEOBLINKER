package org.example.geoblinker.presentation.features.info/frequent_quest


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для конкретного вопроса
 */
class FrequentQuestViewModel : BaseViewModel<FrequentQuestState, FrequentQuestEvent, FrequentQuestEffect>(
    initialState = FrequentQuestState()
) {


    override fun handleEvent(event: FrequentQuestEvent) {
        when (event) {
            is FrequentQuestEvent.NavigateBack -> sendEffect(FrequentQuestEffect.NavigateBack)
            is FrequentQuestEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
