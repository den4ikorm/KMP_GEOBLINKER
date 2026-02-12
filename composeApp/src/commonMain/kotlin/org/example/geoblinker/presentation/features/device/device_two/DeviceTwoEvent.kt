package org.example.geoblinker.presentation.features.device/device_two




/**
 * События для второго шага привязки устройства
 */
sealed class DeviceTwoEvent {
    data object NavigateBack : DeviceTwoEvent()
    data object ClearError : DeviceTwoEvent()
}
