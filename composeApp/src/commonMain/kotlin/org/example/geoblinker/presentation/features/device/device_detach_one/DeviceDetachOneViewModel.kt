package org.example.geoblinker.presentation.features.device/device_detach_one


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для первого шага отвязки устройства
 */
class DeviceDetachOneViewModel : BaseViewModel<DeviceDetachOneState, DeviceDetachOneEvent, DeviceDetachOneEffect>(
    initialState = DeviceDetachOneState()
) {


    override fun handleEvent(event: DeviceDetachOneEvent) {
        when (event) {
            is DeviceDetachOneEvent.NavigateBack -> sendEffect(DeviceDetachOneEffect.NavigateBack)
            is DeviceDetachOneEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
