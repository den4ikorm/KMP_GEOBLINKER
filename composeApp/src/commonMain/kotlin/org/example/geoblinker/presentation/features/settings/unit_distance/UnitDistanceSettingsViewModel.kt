package org.example.geoblinker.presentation.features.settings/unit_distance


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для единиц измерения
 */
class UnitDistanceSettingsViewModel : BaseViewModel<UnitDistanceSettingsState, UnitDistanceSettingsEvent, UnitDistanceSettingsEffect>(
    initialState = UnitDistanceSettingsState()
) {


    override fun handleEvent(event: UnitDistanceSettingsEvent) {
        when (event) {
            is UnitDistanceSettingsEvent.NavigateBack -> sendEffect(UnitDistanceSettingsEffect.NavigateBack)
            is UnitDistanceSettingsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
