package org.example.geoblinker.presentation.features.device/device_detach_one




/**
 * State для первого шага отвязки устройства
 */
data class DeviceDetachOneState(
    val isLoading: Boolean = false,
    val error: String? = null
)
