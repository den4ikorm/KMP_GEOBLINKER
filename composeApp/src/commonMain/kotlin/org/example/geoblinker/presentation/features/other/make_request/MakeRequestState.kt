package org.example.geoblinker.presentation.features.other/make_request




/**
 * State для создания заявки
 */
data class MakeRequestState(
    val isLoading: Boolean = false,
    val error: String? = null
)
