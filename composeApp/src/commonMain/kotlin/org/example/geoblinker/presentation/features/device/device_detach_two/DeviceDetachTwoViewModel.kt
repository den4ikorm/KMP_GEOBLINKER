package org.example.geoblinker.presentation.features.device/device_detach_two


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для второго шага отвязки устройства
 */
class DeviceDetachTwoViewModel : BaseViewModel<DeviceDetachTwoState, DeviceDetachTwoEvent, DeviceDetachTwoEffect>(
    initialState = DeviceDetachTwoState()
) {


    override fun handleEvent(event: DeviceDetachTwoEvent) {
        when (event) {
            is DeviceDetachTwoEvent.NavigateBack -> sendEffect(DeviceDetachTwoEffect.NavigateBack)
            is DeviceDetachTwoEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
