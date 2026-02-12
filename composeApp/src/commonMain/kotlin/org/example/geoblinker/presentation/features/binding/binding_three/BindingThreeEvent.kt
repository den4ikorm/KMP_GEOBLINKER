package org.example.geoblinker.presentation.features.binding/binding_three




/**
 * События для третьего шага привязки
 */
sealed class BindingThreeEvent {
    data object NavigateBack : BindingThreeEvent()
    data object ClearError : BindingThreeEvent()
}
