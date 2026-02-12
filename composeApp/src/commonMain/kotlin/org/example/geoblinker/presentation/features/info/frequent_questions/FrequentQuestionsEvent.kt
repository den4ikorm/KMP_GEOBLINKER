package org.example.geoblinker.presentation.features.info/frequent_questions




/**
 * События для частых вопросов
 */
sealed class FrequentQuestionsEvent {
    data object NavigateBack : FrequentQuestionsEvent()
    data object ClearError : FrequentQuestionsEvent()
}
