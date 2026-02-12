package org.example.geoblinker.presentation.features.device/device_detach_one




/**
 * События для первого шага отвязки устройства
 */
sealed class DeviceDetachOneEvent {
    data object NavigateBack : DeviceDetachOneEvent()
    data object ClearError : DeviceDetachOneEvent()
}
