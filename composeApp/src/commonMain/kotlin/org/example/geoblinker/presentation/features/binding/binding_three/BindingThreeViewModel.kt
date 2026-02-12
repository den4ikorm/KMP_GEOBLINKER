package org.example.geoblinker.presentation.features.binding/binding_three


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для третьего шага привязки
 */
class BindingThreeViewModel : BaseViewModel<BindingThreeState, BindingThreeEvent, BindingThreeEffect>(
    initialState = BindingThreeState()
) {


    override fun handleEvent(event: BindingThreeEvent) {
        when (event) {
            is BindingThreeEvent.NavigateBack -> sendEffect(BindingThreeEffect.NavigateBack)
            is BindingThreeEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
