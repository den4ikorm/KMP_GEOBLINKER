package org.example.geoblinker.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class NotificationViewModel : ViewModel() {
    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()


    fun handleNotification(id: String) { // Явно указываем тип String (строка 126)
        viewModelScope.launch { // Обертываем в корутину (строка 127)
            processNotification(id)
        }
    }


    private suspend fun processNotification(id: String) {
        // Логика обработки
    }
}
data class NotificationState(val list: List<String> = emptyList())
