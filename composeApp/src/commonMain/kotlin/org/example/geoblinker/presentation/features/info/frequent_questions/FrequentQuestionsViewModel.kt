package org.example.geoblinker.presentation.features.info/frequent_questions


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для частых вопросов
 */
class FrequentQuestionsViewModel : BaseViewModel<FrequentQuestionsState, FrequentQuestionsEvent, FrequentQuestionsEffect>(
    initialState = FrequentQuestionsState()
) {


    override fun handleEvent(event: FrequentQuestionsEvent) {
        when (event) {
            is FrequentQuestionsEvent.NavigateBack -> sendEffect(FrequentQuestionsEffect.NavigateBack)
            is FrequentQuestionsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
