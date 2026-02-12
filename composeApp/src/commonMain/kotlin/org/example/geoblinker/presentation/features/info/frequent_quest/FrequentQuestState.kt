package org.example.geoblinker.presentation.features.info/frequent_quest




/**
 * State для конкретного вопроса
 */
data class FrequentQuestState(
    val isLoading: Boolean = false,
    val error: String? = null
)
