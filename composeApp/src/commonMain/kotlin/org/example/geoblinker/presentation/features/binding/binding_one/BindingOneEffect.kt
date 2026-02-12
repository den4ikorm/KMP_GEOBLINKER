package org.example.geoblinker.presentation.features.binding/binding_one




/**
 * Side-effects для первого шага привязки
 */
sealed class BindingOneEffect {
    data object NavigateBack : BindingOneEffect()
    data class ShowError(val message: String) : BindingOneEffect()
    data class ShowMessage(val message: String) : BindingOneEffect()
}
