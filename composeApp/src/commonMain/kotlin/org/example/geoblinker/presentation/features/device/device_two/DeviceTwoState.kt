package org.example.geoblinker.presentation.features.device/device_two




/**
 * State для второго шага привязки устройства
 */
data class DeviceTwoState(
    val isLoading: Boolean = false,
    val error: String? = null
)
