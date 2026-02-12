package org.example.geoblinker.presentation.features.info/frequent_questions




/**
 * State для частых вопросов
 */
data class FrequentQuestionsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
