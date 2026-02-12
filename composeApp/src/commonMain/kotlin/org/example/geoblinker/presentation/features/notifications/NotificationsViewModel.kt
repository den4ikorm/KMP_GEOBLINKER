package org.example.geoblinker.presentation.features.notifications


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class NotificationsViewModel : ViewModel() {
    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()
}


data class NotificationsState(
    val isLoading: Boolean = false,
    val notifications: List<String> = emptyList()
)
