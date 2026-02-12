package org.example.geoblinker.presentation.features.info/about_app


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для информации о приложении
 */
class AboutAppViewModel : BaseViewModel<AboutAppState, AboutAppEvent, AboutAppEffect>(
    initialState = AboutAppState()
) {


    override fun handleEvent(event: AboutAppEvent) {
        when (event) {
            is AboutAppEvent.NavigateBack -> sendEffect(AboutAppEffect.NavigateBack)
            is AboutAppEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
