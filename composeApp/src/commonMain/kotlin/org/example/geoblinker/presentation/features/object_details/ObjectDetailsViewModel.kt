package org.example.geoblinker.presentation.features.object_details


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.example.geoblinker.core.base.BaseViewModel
import org.example.geoblinker.domain.repositories.Repository


/**
 * ViewModel для ObjectDetailsScreen
 * ВСЯ логика экрана деталей здесь
 */
class ObjectDetailsViewModel(
    private val repository: Repository
) : BaseViewModel<ObjectDetailsState, ObjectDetailsEvent, ObjectDetailsEffect>() {
    
    
    override val initialState = ObjectDetailsState()
    
    
    override fun handleEvent(event: ObjectDetailsEvent) {
        when (event) {
            is ObjectDetailsEvent.OnLoadDevice -> loadDevice(event.deviceId)
            is ObjectDetailsEvent.OnBackClicked -> navigateBack()
            is ObjectDetailsEvent.OnEditClicked -> startEditing()
            is ObjectDetailsEvent.OnNameChanged -> updateName(event.name)
            is ObjectDetailsEvent.OnIconChanged -> updateIcon(event.iconId)
            is ObjectDetailsEvent.OnDeleteClicked -> showDeleteDialog()
            is ObjectDetailsEvent.OnDeleteConfirmed -> deleteDevice()
            is ObjectDetailsEvent.OnDeleteDialogDismissed -> hideDeleteDialog()
            is ObjectDetailsEvent.OnSaveClicked -> saveChanges()
            is ObjectDetailsEvent.OnRetry -> retryLoad()
        }
    }
    
    
    /**
     * Загрузка устройства по ID
     */
    private fun loadDevice(deviceId: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            
            try {
                val device = repository.getDeviceById(deviceId).firstOrNull()
                
                
                if (device != null) {
                    updateState { 
                        copy(
                            device = device,
                            isLoading = false
                        ) 
                    }
                } else {
                    updateState { 
                        copy(
                            isLoading = false,
                            error = "Устройство не найдено"
                        ) 
                    }
                    sendEffect(ObjectDetailsEffect.ShowError("Устройство не найдено"))
                }
            } catch (e: Exception) {
                updateState { 
                    copy(
                        isLoading = false,
                        error = e.message
                    ) 
                }
                sendEffect(ObjectDetailsEffect.ShowError(e.message ?: "Ошибка загрузки"))
            }
        }
    }
    
    
    /**
     * Навигация назад
     */
    private fun navigateBack() {
        sendEffect(ObjectDetailsEffect.NavigateBack)
    }
    
    
    /**
     * Начало редактирования
     */
    private fun startEditing() {
        updateState { copy(isEditing = true) }
    }
    
    
    /**
     * Обновление имени
     */
    private fun updateName(name: String) {
        val currentDevice = state.value.device ?: return
        updateState { 
            copy(device = currentDevice.copy(name = name))
        }
    }
    
    
    /**
     * Обновление иконки
     */
    private fun updateIcon(iconId: String) {
        val currentDevice = state.value.device ?: return
        updateState { 
            copy(device = currentDevice.copy(icon = iconId))
        }
    }
    
    
    /**
     * Показать диалог удаления
     */
    private fun showDeleteDialog() {
        updateState { copy(showDeleteDialog = true) }
    }
    
    
    /**
     * Скрыть диалог удаления
     */
    private fun hideDeleteDialog() {
        updateState { copy(showDeleteDialog = false) }
    }
    
    
    /**
     * Удаление устройства
     */
    private fun deleteDevice() {
        val device = state.value.device ?: return
        
        
        viewModelScope.launch {
            updateState { 
                copy(
                    isLoading = true,
                    showDeleteDialog = false
                ) 
            }
            
            
            try {
                repository.deleteDevice(device.id)
                
                
                updateState { copy(isLoading = false) }
                sendEffect(ObjectDetailsEffect.ShowSuccess("Устройство удалено"))
                sendEffect(ObjectDetailsEffect.NavigateBack)
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                sendEffect(ObjectDetailsEffect.ShowError(e.message ?: "Ошибка удаления"))
            }
        }
    }
    
    
    /**
     * Сохранение изменений
     */
    private fun saveChanges() {
        val device = state.value.device ?: return
        
        
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            
            try {
                repository.updateDevice(device)
                
                
                updateState { 
                    copy(
                        isLoading = false,
                        isEditing = false
                    ) 
                }
                sendEffect(ObjectDetailsEffect.ShowSuccess("Изменения сохранены"))
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                sendEffect(ObjectDetailsEffect.ShowError(e.message ?: "Ошибка сохранения"))
            }
        }
    }
    
    
    /**
     * Повторная попытка загрузки
     */
    private fun retryLoad() {
        val device = state.value.device
        if (device != null) {
            loadDevice(device.id)
        }
    }
}
