package org.example.geoblinker.presentation.features.device/device_two




/**
 * Side-effects для второго шага привязки устройства
 */
sealed class DeviceTwoEffect {
    data object NavigateBack : DeviceTwoEffect()
    data class ShowError(val message: String) : DeviceTwoEffect()
    data class ShowMessage(val message: String) : DeviceTwoEffect()
}
