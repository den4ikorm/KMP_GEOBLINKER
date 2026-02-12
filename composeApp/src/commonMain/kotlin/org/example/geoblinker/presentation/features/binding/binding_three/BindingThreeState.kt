package org.example.geoblinker.presentation.features.binding/binding_three




/**
 * State для третьего шага привязки
 */
data class BindingThreeState(
    val isLoading: Boolean = false,
    val error: String? = null
)
