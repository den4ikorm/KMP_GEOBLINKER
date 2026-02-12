package org.example.geoblinker.presentation.features.device/device_two


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для второго шага привязки устройства
 */
class DeviceTwoViewModel : BaseViewModel<DeviceTwoState, DeviceTwoEvent, DeviceTwoEffect>(
    initialState = DeviceTwoState()
) {


    override fun handleEvent(event: DeviceTwoEvent) {
        when (event) {
            is DeviceTwoEvent.NavigateBack -> sendEffect(DeviceTwoEffect.NavigateBack)
            is DeviceTwoEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
