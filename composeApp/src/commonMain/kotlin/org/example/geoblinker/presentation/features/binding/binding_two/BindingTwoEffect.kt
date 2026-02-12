package org.example.geoblinker.presentation.features.binding/binding_two




/**
 * Side-effects для второго шага привязки
 */
sealed class BindingTwoEffect {
    data object NavigateBack : BindingTwoEffect()
    data class ShowError(val message: String) : BindingTwoEffect()
    data class ShowMessage(val message: String) : BindingTwoEffect()
}
