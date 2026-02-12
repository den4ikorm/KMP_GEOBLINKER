package org.example.geoblinker.presentation.features.map_screen


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Devices


/**
 * State для MapScreen
 * Полностью immutable и platform-safe
 */
data class MapState(
    val devices: List<Devices> = emptyList(),
    val selectedDevice: Devices? = null,
    val userLocation: Pair<Double, Double>? = null, // lat, lng
    val isLoading: Boolean = false,
    val isFollowMode: Boolean = false,
    val mapTheme: MapTheme = MapTheme.LIGHT,
    val zoomLevel: Float = 15f,
    val searchQuery: String = "",
    val showEmptyDevicesPopup: Boolean = false,
    val showSearchPopup: Boolean = false,
    val error: String? = null
)


/**
 * Темы карты
 */
enum class MapTheme {
    LIGHT,
    DARK,
    SATELLITE
}
