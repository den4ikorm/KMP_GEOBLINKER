package org.example.geoblinker.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()


    fun updateProfile() {
        viewModelScope.launch {
            // Исправляем ошибку строки 249 — вызов теперь в корутине
            performUpdate() 
        }
    }


    private suspend fun performUpdate() {
        // Логика обновления
    }
}
data class ProfileState(val name: String = "", val isLoading: Boolean = false)
