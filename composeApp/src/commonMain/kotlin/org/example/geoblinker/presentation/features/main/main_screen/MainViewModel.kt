package org.example.geoblinker.presentation.features.main/main_screen


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для главного экрана
 */
class MainViewModel : BaseViewModel<MainState, MainEvent, MainEffect>(
    initialState = MainState()
) {


    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.NavigateBack -> sendEffect(MainEffect.NavigateBack)
            is MainEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
