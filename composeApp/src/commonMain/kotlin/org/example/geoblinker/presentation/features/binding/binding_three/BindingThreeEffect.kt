package org.example.geoblinker.presentation.features.binding/binding_three




/**
 * Side-effects для третьего шага привязки
 */
sealed class BindingThreeEffect {
    data object NavigateBack : BindingThreeEffect()
    data class ShowError(val message: String) : BindingThreeEffect()
    data class ShowMessage(val message: String) : BindingThreeEffect()
}
