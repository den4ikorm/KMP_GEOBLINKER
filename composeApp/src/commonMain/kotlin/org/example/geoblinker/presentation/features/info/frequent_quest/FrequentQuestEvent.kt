package org.example.geoblinker.presentation.features.info/frequent_quest




/**
 * События для конкретного вопроса
 */
sealed class FrequentQuestEvent {
    data object NavigateBack : FrequentQuestEvent()
    data object ClearError : FrequentQuestEvent()
}
