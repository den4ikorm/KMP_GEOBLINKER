package org.example.geoblinker.presentation.features.device/device_three


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для третьего шага привязки устройства
 */
class DeviceThreeViewModel : BaseViewModel<DeviceThreeState, DeviceThreeEvent, DeviceThreeEffect>(
    initialState = DeviceThreeState()
) {


    override fun handleEvent(event: DeviceThreeEvent) {
        when (event) {
            is DeviceThreeEvent.NavigateBack -> sendEffect(DeviceThreeEffect.NavigateBack)
            is DeviceThreeEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
