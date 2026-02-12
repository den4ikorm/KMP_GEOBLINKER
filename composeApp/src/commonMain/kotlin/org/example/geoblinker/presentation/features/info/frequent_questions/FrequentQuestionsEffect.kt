package org.example.geoblinker.presentation.features.info/frequent_questions




/**
 * Side-effects для частых вопросов
 */
sealed class FrequentQuestionsEffect {
    data object NavigateBack : FrequentQuestionsEffect()
    data class ShowError(val message: String) : FrequentQuestionsEffect()
    data class ShowMessage(val message: String) : FrequentQuestionsEffect()
}
