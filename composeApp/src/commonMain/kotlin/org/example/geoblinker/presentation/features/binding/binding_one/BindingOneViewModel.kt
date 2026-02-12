package org.example.geoblinker.presentation.features.binding/binding_one


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для первого шага привязки
 */
class BindingOneViewModel : BaseViewModel<BindingOneState, BindingOneEvent, BindingOneEffect>(
    initialState = BindingOneState()
) {


    override fun handleEvent(event: BindingOneEvent) {
        when (event) {
            is BindingOneEvent.NavigateBack -> sendEffect(BindingOneEffect.NavigateBack)
            is BindingOneEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
