package org.example.geoblinker.presentation.features.device_list_signal


import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.geoblinker.core.base.BaseViewModel
import org.example.geoblinker.domain.repositories.Repository


/**
 * ViewModel для экрана истории сигналов устройства
 */
class DeviceListSignalViewModel(
    private val repository: Repository
) : BaseViewModel<DeviceListSignalState, DeviceListSignalEvent, DeviceListSignalEffect>(
    initialState = DeviceListSignalState()
) {


    override fun handleEvent(event: DeviceListSignalEvent) {
        when (event) {
            is DeviceListSignalEvent.LoadSignals -> loadSignals(event.deviceId)
            is DeviceListSignalEvent.ChangeFilter -> changeFilter(event.filter)
            is DeviceListSignalEvent.Refresh -> refreshSignals()
            is DeviceListSignalEvent.NavigateBack -> sendEffect(DeviceListSignalEffect.NavigateBack)
            is DeviceListSignalEvent.ClearError -> clearError()
        }
    }


    private fun loadSignals(deviceId: String) {
        launchIO {
            _state.update { it.copy(isLoading = true, deviceId = deviceId) }
            
            
            try {
                // Загружаем сигналы из репозитория
                val signals = repository.getDeviceSignals(deviceId)
                
                
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        signals = signals,
                        groupedSignals = groupSignalsByDate(signals, currentState.selectedFilter)
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                sendEffect(DeviceListSignalEffect.ShowError(e.message ?: "Ошибка загрузки сигналов"))
            }
        }
    }


    private fun changeFilter(filter: SignalFilter) {
        _state.update { currentState ->
            currentState.copy(
                selectedFilter = filter,
                groupedSignals = groupSignalsByDate(currentState.signals, filter)
            )
        }
    }


    private fun refreshSignals() {
        val deviceId = _state.value.deviceId
        if (deviceId.isNotEmpty()) {
            launchIO {
                _state.update { it.copy(isRefreshing = true) }
                
                
                try {
                    val signals = repository.getDeviceSignals(deviceId)
                    
                    
                    _state.update { currentState ->
                        currentState.copy(
                            isRefreshing = false,
                            signals = signals,
                            groupedSignals = groupSignalsByDate(signals, currentState.selectedFilter)
                        )
                    }
                    sendEffect(DeviceListSignalEffect.ShowMessage("Обновлено"))
                } catch (e: Exception) {
                    _state.update { it.copy(isRefreshing = false, error = e.message) }
                    sendEffect(DeviceListSignalEffect.ShowError(e.message ?: "Ошибка обновления"))
                }
            }
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }


    /**
     * Группировка сигналов по дате с учетом фильтра
     */
    private fun groupSignalsByDate(
        signals: List<org.example.geoblinker.domain.models.Signal>,
        filter: SignalFilter
    ): Map<String, List<SignalItem>> {
        // Фильтруем сигналы
        val filteredSignals = when (filter) {
            SignalFilter.All -> signals
            else -> signals.filter { it.type == filter.displayName }
        }
        
        
        // Преобразуем в SignalItem и группируем по дате
        return filteredSignals
            .map { signal -> signal.toSignalItem() }
            .groupBy { it.dateFormatted }
            .toSortedMap(compareByDescending { it }) // Сортируем даты в обратном порядке
    }


    /**
     * Преобразование Signal в SignalItem для отображения
     */
    private fun org.example.geoblinker.domain.models.Signal.toSignalItem(): SignalItem {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        
        
        val timeFormatted = "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
        val dateFormatted = "${dateTime.dayOfMonth}.${dateTime.monthNumber}.${dateTime.year}"
        
        
        val iconType = when (type) {
            "Location", "Локация" -> SignalIconType.Location
            "Alert", "Тревога" -> SignalIconType.Alert
            "Battery", "Батарея" -> SignalIconType.Battery
            "Speed", "Скорость" -> SignalIconType.Speed
            else -> SignalIconType.Info
        }
        
        
        val locationText = if (lat != 0.0 && lng != 0.0) {
            "${String.format("%.4f", lat)}, ${String.format("%.4f", lng)}"
        } else ""
        
        
        return SignalItem(
            id = timestamp.toString(), // TODO: использовать реальный ID
            type = type,
            description = description,
            timestamp = timestamp,
            timeFormatted = timeFormatted,
            dateFormatted = dateFormatted,
            lat = lat,
            lng = lng,
            speed = speed,
            iconType = iconType,
            locationText = locationText
        )
    }
}
