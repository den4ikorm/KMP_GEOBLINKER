package org.example.geoblinker.presentation.features.objects_list


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Devices


/**
 * State для ObjectsListScreen  
 * Полностью immutable и platform-safe
 */
data class ObjectsListState(
    val devices: List<Devices> = emptyList(),
    val filteredDevices: List<Devices> = emptyList(),
    val disconnectedDevices: List<Devices> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val sortType: SortType = SortType.BY_NAME,
    val userLocation: Pair<Double, Double>? = null, // lat, lng
    val error: String? = null
)


/**
 * Типы сортировки
 */
enum class SortType {
    BY_NAME,
    BY_DEVICE_TYPE,
    BY_DISTANCE,
    BY_BINDING_DATE,
    BY_SIGNAL_STRENGTH,
    BY_CHARGE_LEVEL
}
