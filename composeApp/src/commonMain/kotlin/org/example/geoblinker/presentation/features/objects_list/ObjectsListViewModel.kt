package org.example.geoblinker.presentation.features.objects_list


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import kotlin.math.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.example.geoblinker.core.base.BaseViewModel
import org.example.geoblinker.domain.models.Devices
import org.example.geoblinker.domain.repositories.Repository
import org.example.geoblinker.domain.usecases.SyncDevicesUseCase


/**
 * ViewModel для ObjectsListScreen
 * ВСЯ логика списка устройств здесь
 */
class ObjectsListViewModel(
    private val syncDevicesUseCase: SyncDevicesUseCase,
    private val repository: Repository
) : BaseViewModel<ObjectsListState, ObjectsListEvent, ObjectsListEffect>() {
    
    
    override val initialState = ObjectsListState()
    
    
    init {
        loadDevices()
    }
    
    
    override fun handleEvent(event: ObjectsListEvent) {
        when (event) {
            is ObjectsListEvent.OnDeviceClick -> handleDeviceClick(event.device)
            is ObjectsListEvent.OnSearchQueryChanged -> handleSearch(event.query)
            is ObjectsListEvent.OnSortTypeChanged -> handleSortChange(event.sortType)
            is ObjectsListEvent.OnSortDialogToggle -> {} // Handled in UI
            is ObjectsListEvent.OnUserLocationUpdated -> handleLocationUpdate(event.lat, event.lng)
            is ObjectsListEvent.OnRefresh -> refreshDevices()
            is ObjectsListEvent.OnNavigateToBinding -> navigateToBinding()
        }
    }
    
    
    /**
     * Загрузка устройств
     */
    private fun loadDevices() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            
            repository.getAllDevices()
                .catch { e ->
                    updateState { copy(isLoading = false, error = e.message) }
                    sendEffect(ObjectsListEffect.ShowError(e.message ?: "Ошибка загрузки"))
                }
                .collect { devices ->
                    updateState { 
                        copy(
                            devices = devices,
                            isLoading = false
                        ) 
                    }
                    applyFiltersAndSort()
                }
        }
    }
    
    
    /**
     * Обновление устройств с сервера
     */
    private fun refreshDevices() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            
            val result = syncDevicesUseCase.execute()
            
            
            updateState { copy(isLoading = false) }
            
            
            if (result.isFailure) {
                val error = result.exceptionOrNull()?.message ?: "Ошибка синхронизации"
                sendEffect(ObjectsListEffect.ShowError(error))
            }
        }
    }
    
    
    /**
     * Клик по устройству
     */
    private fun handleDeviceClick(device: Devices) {
        sendEffect(ObjectsListEffect.NavigateToDeviceDetails(device))
    }
    
    
    /**
     * Поиск устройств
     */
    private fun handleSearch(query: String) {
        updateState { copy(searchQuery = query) }
        applyFiltersAndSort()
    }
    
    
    /**
     * Изменение типа сортировки
     */
    private fun handleSortChange(sortType: SortType) {
        updateState { copy(sortType = sortType) }
        applyFiltersAndSort()
    }
    
    
    /**
     * Обновление локации пользователя
     */
    private fun handleLocationUpdate(lat: Double, lng: Double) {
        updateState { copy(userLocation = lat to lng) }
        
        
        // Пересортировать если сортировка по расстоянию
        if (state.value.sortType == SortType.BY_DISTANCE) {
            applyFiltersAndSort()
        }
    }
    
    
    /**
     * Навигация на экран привязки
     */
    private fun navigateToBinding() {
        sendEffect(ObjectsListEffect.NavigateToBinding)
    }
    
    
    /**
     * Применение фильтров и сортировки
     * ВСЯ логика фильтрации и сортировки в ViewModel!
     */
    private fun applyFiltersAndSort() {
        val currentState = state.value
        val devices = currentState.devices
        val query = currentState.searchQuery
        
        
        // Разделяем на подключенные и отключенные
        val connected = devices.filter { it.isConnected }
        val disconnected = devices.filter { !it.isConnected }
        
        
        // Фильтруем по поиску
        val filteredConnected = if (query.isNotEmpty()) {
            connected.filter { device ->
                device.name.contains(query, ignoreCase = true) ||
                device.imei.contains(query, ignoreCase = true)
            }
        } else {
            connected
        }
        
        
        val filteredDisconnected = if (query.isNotEmpty()) {
            disconnected.filter { device ->
                device.imei.contains(query, ignoreCase = true)
            }
        } else {
            disconnected
        }
        
        
        // Сортируем
        val sorted = sortDevices(filteredConnected, currentState.sortType, currentState.userLocation)
        
        
        updateState {
            copy(
                filteredDevices = sorted,
                disconnectedDevices = filteredDisconnected
            )
        }
    }
    
    
    /**
     * Сортировка устройств
     */
    private fun sortDevices(
        devices: List<Devices>,
        sortType: SortType,
        userLocation: Pair<Double, Double>?
    ): List<Devices> {
        return when (sortType) {
            SortType.BY_NAME -> devices.sortedBy { it.name }
            
            
            SortType.BY_DEVICE_TYPE -> devices.sortedBy { it.type }
            
            
            SortType.BY_DISTANCE -> {
                if (userLocation != null) {
                    devices.sortedBy { device ->
                        calculateDistance(
                            userLocation.first, userLocation.second,
                            device.lat, device.lng
                        )
                    }
                } else {
                    devices // Если нет локации, не сортируем
                }
            }
            
            
            SortType.BY_BINDING_DATE -> devices.sortedByDescending { it.bindingTime }
            
            
            SortType.BY_SIGNAL_STRENGTH -> devices.sortedByDescending { it.signalStrength }
            
            
            SortType.BY_CHARGE_LEVEL -> devices.sortedByDescending { it.batteryLevel }
        }
    }
    
    
    /**
     * Вычисление расстояния между двумя точками (Haversine formula)
     * Результат в километрах
     */
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Радиус Земли в км
        
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        
        return R * c
    }
}
