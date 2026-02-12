package org.example.geoblinker.presentation.viewmodels.states


import org.example.geoblinker.domain.models.Devices


/**
 * State для MapViewModel
 */
data class MapState(
    val devices: List<Devices> = emptyList(),
    val selectedDevice: Devices? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val mapCenter: Pair<Double, Double>? = null, // lat, lng
    val zoomLevel: Float = 15f,
    val showTraffic: Boolean = false,
    val filterByType: String? = null
)


/**
 * События для карты
 */
sealed class MapEvent {
    data object LoadDevices : MapEvent()
    data class SelectDevice(val deviceId: String) : MapEvent()
    data object DeselectDevice : MapEvent()
    data class UpdateMapCenter(val lat: Double, val lng: Double) : MapEvent()
    data class UpdateZoom(val zoom: Float) : MapEvent()
    data class ToggleTraffic(val show: Boolean) : MapEvent()
    data class FilterByType(val type: String?) : MapEvent()
    data object Refresh : MapEvent()
}
