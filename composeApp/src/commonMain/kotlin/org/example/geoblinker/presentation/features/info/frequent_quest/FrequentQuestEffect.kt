package org.example.geoblinker.presentation.features.info/frequent_quest




/**
 * Side-effects для конкретного вопроса
 */
sealed class FrequentQuestEffect {
    data object NavigateBack : FrequentQuestEffect()
    data class ShowError(val message: String) : FrequentQuestEffect()
    data class ShowMessage(val message: String) : FrequentQuestEffect()
}
