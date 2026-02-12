package org.example.geoblinker.presentation.viewmodels.states


import org.example.geoblinker.domain.models.Devices


/**
 * State для DeviceViewModel
 * Содержит список устройств, состояние загрузки и ошибки
 */
data class DeviceState(
    val devices: List<Devices> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedDevice: Devices? = null,
    val isRefreshing: Boolean = false
)
