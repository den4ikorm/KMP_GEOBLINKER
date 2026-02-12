package org.example.geoblinker.presentation.features.binding/binding_two




/**
 * State для второго шага привязки
 */
data class BindingTwoState(
    val isLoading: Boolean = false,
    val error: String? = null
)
