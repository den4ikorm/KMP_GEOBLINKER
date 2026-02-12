package org.example.geoblinker.presentation.features.info/about_company


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для информации о компании
 */
class AboutCompanyViewModel : BaseViewModel<AboutCompanyState, AboutCompanyEvent, AboutCompanyEffect>(
    initialState = AboutCompanyState()
) {


    override fun handleEvent(event: AboutCompanyEvent) {
        when (event) {
            is AboutCompanyEvent.NavigateBack -> sendEffect(AboutCompanyEffect.NavigateBack)
            is AboutCompanyEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
