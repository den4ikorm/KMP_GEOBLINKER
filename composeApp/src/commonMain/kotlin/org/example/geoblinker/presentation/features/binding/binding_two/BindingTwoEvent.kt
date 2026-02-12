package org.example.geoblinker.presentation.features.binding/binding_two




/**
 * События для второго шага привязки
 */
sealed class BindingTwoEvent {
    data object NavigateBack : BindingTwoEvent()
    data object ClearError : BindingTwoEvent()
}
