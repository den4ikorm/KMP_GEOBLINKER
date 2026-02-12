package org.example.geoblinker.presentation.features.device/device_three




/**
 * Side-effects для третьего шага привязки устройства
 */
sealed class DeviceThreeEffect {
    data object NavigateBack : DeviceThreeEffect()
    data class ShowError(val message: String) : DeviceThreeEffect()
    data class ShowMessage(val message: String) : DeviceThreeEffect()
}
