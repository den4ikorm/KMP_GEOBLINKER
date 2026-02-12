package org.example.geoblinker.presentation.features.map_screen


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.example.geoblinker.core.base.BaseViewModel
import org.example.geoblinker.domain.repositories.Repository
import org.example.geoblinker.domain.usecases.SyncDevicesUseCase


/**
 * ViewModel для MapScreen
 * ВСЯ логика карты находится здесь
 */
class MapViewModel(
    private val syncDevicesUseCase: SyncDevicesUseCase,
    private val repository: Repository
) : BaseViewModel<MapState, MapEvent, MapEffect>() {
    
    
    override val initialState = MapState()
    
    
    init {
        loadDevices()
        checkEmptyDevices()
    }
    
    
    override fun handleEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnDeviceSelected -> handleDeviceSelection(event.device)
            is MapEvent.OnUserLocationUpdated -> handleLocationUpdate(event.lat, event.lng)
            is MapEvent.OnThemeChanged -> handleThemeChange(event.theme)
            is MapEvent.OnSearchDevice -> handleSearch(event.query)
            is MapEvent.OnSearchPopupToggle -> toggleSearchPopup()
            is MapEvent.OnFollowModeToggle -> toggleFollowMode()
            is MapEvent.OnRefreshDevices -> refreshDevices()
            is MapEvent.OnRequestLocation -> handleLocationRequest()
            is MapEvent.OnZoomIn -> handleZoomIn()
            is MapEvent.OnZoomOut -> handleZoomOut()
            is MapEvent.OnEmptyDevicesPopupDismiss -> dismissEmptyDevicesPopup()
            is MapEvent.OnNavigateToBinding -> navigateToBinding()
        }
    }
    
    
    /**
     * Загрузка устройств из репозитория
     */
    private fun loadDevices() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            
            repository.getAllDevices()
                .catch { e ->
                    updateState { copy(isLoading = false, error = e.message) }
                    sendEffect(MapEffect.ShowError(e.message ?: "Ошибка загрузки"))
                }
                .collect { devices ->
                    updateState { 
                        copy(
                            devices = devices.filter { device ->
                                // Фильтруем только устройства с валидными координатами
                                isValidCoordinates(device.lat, device.lng)
                            },
                            isLoading = false
                        ) 
                    }
                }
        }
    }
    
    
    /**
     * Проверка пустых устройств для показа попапа
     */
    private fun checkEmptyDevices() {
        viewModelScope.launch {
            delay(2000) // Задержка для UI
            val devices = state.value.devices
            if (devices.isEmpty()) {
                updateState { copy(showEmptyDevicesPopup = true) }
            }
        }
    }
    
    
    /**
     * Выбор устройства на карте
     */
    private fun handleDeviceSelection(device: org.example.geoblinker.domain.models.Devices?) {
        updateState { copy(selectedDevice = device) }
        
        
        device?.let { dev ->
            if (isValidCoordinates(dev.lat, dev.lng)) {
                sendEffect(MapEffect.ZoomToDevice(dev.lat, dev.lng))
            }
        }
    }
    
    
    /**
     * Обновление локации пользователя
     */
    private fun handleLocationUpdate(lat: Double, lng: Double) {
        updateState { copy(userLocation = lat to lng) }
    }
    
    
    /**
     * Смена темы карты
     */
    private fun handleThemeChange(theme: MapTheme) {
        updateState { copy(mapTheme = theme) }
    }
    
    
    /**
     * Поиск устройства
     */
    private fun handleSearch(query: String) {
        val devices = state.value.devices
        val found = devices.find { device ->
            device.name.contains(query, ignoreCase = true) ||
            device.imei.contains(query, ignoreCase = true)
        }
        
        
        if (found != null) {
            updateState { 
                copy(
                    selectedDevice = found,
                    searchQuery = query,
                    showSearchPopup = false
                ) 
            }
            sendEffect(MapEffect.ZoomToDevice(found.lat, found.lng))
        } else {
            sendEffect(MapEffect.ShowError("Устройство не найдено"))
        }
    }
    
    
    /**
     * Переключение попапа поиска
     */
    private fun toggleSearchPopup() {
        updateState { copy(showSearchPopup = !showSearchPopup) }
    }
    
    
    /**
     * Режим слежения за устройством
     */
    private fun toggleFollowMode() {
        val newMode = !state.value.isFollowMode
        updateState { copy(isFollowMode = newMode) }
        
        
        if (newMode) {
            state.value.selectedDevice?.let { device ->
                if (isValidCoordinates(device.lat, device.lng)) {
                    sendEffect(MapEffect.ZoomToDevice(device.lat, device.lng))
                }
            }
        }
    }
    
    
    /**
     * Синхронизация устройств с сервером
     */
    private fun refreshDevices() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            
            val result = syncDevicesUseCase.execute()
            
            
            updateState { copy(isLoading = false) }
            
            
            if (result.isFailure) {
                val error = result.exceptionOrNull()?.message ?: "Ошибка синхронизации"
                sendEffect(MapEffect.ShowError(error))
            }
        }
    }
    
    
    /**
     * Запрос локации пользователя
     */
    private fun handleLocationRequest() {
        sendEffect(MapEffect.RequestLocationPermission)
    }
    
    
    /**
     * Увеличение зума
     */
    private fun handleZoomIn() {
        val currentZoom = state.value.zoomLevel
        updateState { copy(zoomLevel = (currentZoom + 1).coerceAtMost(20f)) }
    }
    
    
    /**
     * Уменьшение зума
     */
    private fun handleZoomOut() {
        val currentZoom = state.value.zoomLevel
        updateState { copy(zoomLevel = (currentZoom - 1).coerceAtLeast(5f)) }
    }
    
    
    /**
     * Закрытие попапа пустых устройств
     */
    private fun dismissEmptyDevicesPopup() {
        updateState { copy(showEmptyDevicesPopup = false) }
    }
    
    
    /**
     * Навигация на экран привязки
     */
    private fun navigateToBinding() {
        updateState { copy(showEmptyDevicesPopup = false) }
        sendEffect(MapEffect.NavigateToBinding)
    }
    
    
    /**
     * Проверка валидности координат
     */
    private fun isValidCoordinates(lat: Double, lng: Double): Boolean {
        return lat != -999999999.9 && lng != -999999999.9 &&
               lat in -90.0..90.0 && lng in -180.0..180.0
    }
}
