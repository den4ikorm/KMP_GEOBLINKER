package org.example.geoblinker.presentation.features.device/device_detach_two




/**
 * Side-effects для второго шага отвязки устройства
 */
sealed class DeviceDetachTwoEffect {
    data object NavigateBack : DeviceDetachTwoEffect()
    data class ShowError(val message: String) : DeviceDetachTwoEffect()
    data class ShowMessage(val message: String) : DeviceDetachTwoEffect()
}
