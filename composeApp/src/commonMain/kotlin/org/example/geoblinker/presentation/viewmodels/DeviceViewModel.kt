package org.example.geoblinker.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.geoblinker.domain.models.Devices
import org.example.geoblinker.domain.repositories.Repository
import org.example.geoblinker.domain.usecases.CheckImeiUseCase
import org.example.geoblinker.domain.usecases.SyncDevicesUseCase
import org.example.geoblinker.presentation.viewmodels.states.DeviceState


/**
 * ViewModel для управления устройствами (REFACTORED)
 * Теперь использует реальные данные из БД и UseCases
 */
class DeviceViewModel(
    private val syncDevicesUseCase: SyncDevicesUseCase,
    private val checkImeiUseCase: CheckImeiUseCase,
    private val repository: Repository
) : ViewModel() {
    
    
    private val _state = MutableStateFlow(DeviceState())
    val state: StateFlow<DeviceState> = _state.asStateFlow()
    
    
    init {
        observeDevices()
        syncDevices()
    }
    
    
    /**
     * Подписка на изменения устройств из БД
     */
    private fun observeDevices() {
        viewModelScope.launch {
            repository.getAllDevices().collect { devices ->
                _state.update { it.copy(devices = devices) }
            }
        }
    }
    
    
    /**
     * Синхронизация устройств с сервером
     */
    fun syncDevices() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            
            syncDevicesUseCase.execute()
                .onSuccess {
                    _state.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            error = error.message ?: "Ошибка синхронизации"
                        ) 
                    }
                }
        }
    }
    
    
    /**
     * Проверка и добавление IMEI
     */
    fun checkImei(imei: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            
            checkImeiUseCase.execute(imei)
                .onSuccess { simei ->
                    _state.update { it.copy(isLoading = false, error = null) }
                    onSuccess(simei)
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            error = error.message ?: "Ошибка проверки IMEI"
                        ) 
                    }
                }
        }
    }
    
    
    /**
     * Получение устройства по IMEI
     */
    fun getDeviceByImei(imei: String): Devices? {
        return state.value.devices.find { it.imei == imei }
    }
    
    
    /**
     * Фильтрация устройств по статусу подключения
     */
    fun getConnectedDevices(): List<Devices> {
        return state.value.devices.filter { it.isConnected == 1L }
    }
}
