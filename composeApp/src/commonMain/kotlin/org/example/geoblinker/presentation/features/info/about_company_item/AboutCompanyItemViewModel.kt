package org.example.geoblinker.presentation.features.info/about_company_item


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для детальной информации о компании
 */
class AboutCompanyItemViewModel : BaseViewModel<AboutCompanyItemState, AboutCompanyItemEvent, AboutCompanyItemEffect>(
    initialState = AboutCompanyItemState()
) {


    override fun handleEvent(event: AboutCompanyItemEvent) {
        when (event) {
            is AboutCompanyItemEvent.NavigateBack -> sendEffect(AboutCompanyItemEffect.NavigateBack)
            is AboutCompanyItemEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
