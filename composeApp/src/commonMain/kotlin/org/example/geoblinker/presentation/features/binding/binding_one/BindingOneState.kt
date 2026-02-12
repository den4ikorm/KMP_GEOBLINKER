package org.example.geoblinker.presentation.features.binding/binding_one




/**
 * State для первого шага привязки
 */
data class BindingOneState(
    val isLoading: Boolean = false,
    val error: String? = null
)
