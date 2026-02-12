package org.example.geoblinker.presentation.features.device/device_detach_one




/**
 * Side-effects для первого шага отвязки устройства
 */
sealed class DeviceDetachOneEffect {
    data object NavigateBack : DeviceDetachOneEffect()
    data class ShowError(val message: String) : DeviceDetachOneEffect()
    data class ShowMessage(val message: String) : DeviceDetachOneEffect()
}
