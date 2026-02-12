package org.example.geoblinker.presentation.features.device/device_three




/**
 * События для третьего шага привязки устройства
 */
sealed class DeviceThreeEvent {
    data object NavigateBack : DeviceThreeEvent()
    data object ClearError : DeviceThreeEvent()
}
