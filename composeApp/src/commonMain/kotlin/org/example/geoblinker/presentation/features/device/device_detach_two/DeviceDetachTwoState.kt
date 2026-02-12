package org.example.geoblinker.presentation.features.device/device_detach_two




/**
 * State для второго шага отвязки устройства
 */
data class DeviceDetachTwoState(
    val isLoading: Boolean = false,
    val error: String? = null
)
