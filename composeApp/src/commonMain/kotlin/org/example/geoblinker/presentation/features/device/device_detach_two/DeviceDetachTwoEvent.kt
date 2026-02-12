package org.example.geoblinker.presentation.features.device/device_detach_two




/**
 * События для второго шага отвязки устройства
 */
sealed class DeviceDetachTwoEvent {
    data object NavigateBack : DeviceDetachTwoEvent()
    data object ClearError : DeviceDetachTwoEvent()
}
