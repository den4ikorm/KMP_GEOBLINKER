package org.example.geoblinker.presentation.features.binding/binding_two


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для второго шага привязки
 */
class BindingTwoViewModel : BaseViewModel<BindingTwoState, BindingTwoEvent, BindingTwoEffect>(
    initialState = BindingTwoState()
) {


    override fun handleEvent(event: BindingTwoEvent) {
        when (event) {
            is BindingTwoEvent.NavigateBack -> sendEffect(BindingTwoEffect.NavigateBack)
            is BindingTwoEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
