package org.example.geoblinker.presentation.features.binding/binding_one




/**
 * События для первого шага привязки
 */
sealed class BindingOneEvent {
    data object NavigateBack : BindingOneEvent()
    data object ClearError : BindingOneEvent()
}
