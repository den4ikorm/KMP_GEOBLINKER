package org.example.geoblinker.presentation.features.map_screen


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Devices


/**
 * MVI Contract для карты
 */


data class MapState(
    val devices: List<Devices> = emptyList(),
    val selectedDevice: Devices? = null,
    val userLocation: Pair<Double, Double>? = null,
    val isLoading: Boolean = false,
    val isFollowMode: Boolean = false,
    val mapTheme: MapTheme = MapTheme.LIGHT,
    val error: String? = null
)


sealed class MapEvent {
    data class OnDeviceSelected(val device: Devices?) : MapEvent()
    data class OnUserLocationUpdated(val lat: Double, val lng: Double) : MapEvent()
    data class OnThemeChanged(val theme: MapTheme) : MapEvent()
    data class OnSearchDevice(val query: String) : MapEvent()
    object OnFollowModeToggle : MapEvent()
    object OnRefreshDevices : MapEvent()
}


sealed class MapEffect {
    data class ShowError(val message: String) : MapEffect()
    data class ZoomToDevice(val device: Devices) : MapEffect()
    data class ZoomToLocation(val lat: Double, val lng: Double) : MapEffect()
}


enum class MapTheme {
    LIGHT,
    DARK,
    SATELLITE
}
