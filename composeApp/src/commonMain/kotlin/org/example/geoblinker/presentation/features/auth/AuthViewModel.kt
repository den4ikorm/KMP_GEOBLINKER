package org.example.geoblinker.presentation.features.auth


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.geoblinker.core.base.BaseViewModel
import org.example.geoblinker.core.session.SessionManager
import org.example.geoblinker.core.utils.Validators
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * ViewModel для авторизации
 * Реальная логика из LEGACY_RESOURCES.md
 */
class AuthViewModel(
    private val apiRepository: ApiRepository,
    private val sessionManager: SessionManager
) : BaseViewModel<AuthState, AuthEvent, AuthEffect>() {
    
    
    override val initialState = AuthState()
    
    
    init {
        // Проверка существующей сессии
        if (sessionManager.isAuthenticated()) {
            sendEffect(AuthEffect.NavigateToMain)
        }
    }
    
    
    override fun handleEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnPhoneChanged -> {
                val formatted = Validators.formatPhoneNumber(event.phone)
                updateState { 
                    copy(
                        phone = formatted,
                        isPhoneValid = Validators.validatePhone(formatted),
                        error = null
                    ) 
                }
            }
            
            
            is AuthEvent.OnCodeChanged -> {
                updateState { 
                    copy(
                        code = event.code,
                        isCodeValid = Validators.validateCode(event.code),
                        error = null
                    ) 
                }
            }
            
            
            is AuthEvent.OnSendCode -> sendCodeToPhone()
            is AuthEvent.OnConfirmCode -> confirmCode()
            is AuthEvent.OnClearError -> updateState { copy(error = null) }
        }
    }
    
    
    private fun sendCodeToPhone() {
        if (!state.value.isPhoneValid) {
            sendEffect(AuthEffect.ShowError("Неверный формат телефона"))
            return
        }
        
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            
            try {
                // API вызов: POST /register
                val response = apiRepository.register(
                    mapOf(
                        "phone" to state.value.phone.filter { it.isDigit() },
                        "service" to "geoblinker"
                    )
                )
                
                
                if (response.code == 200) {
                    sendEffect(AuthEffect.NavigateToCodeScreen)
                } else {
                    sendEffect(AuthEffect.ShowError(response.message ?: "Ошибка регистрации"))
                }
            } catch (e: Exception) {
                sendEffect(AuthEffect.ShowError("Ошибка сети: ${e.message}"))
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }
    
    
    private fun confirmCode() {
        if (!state.value.isCodeValid) {
            sendEffect(AuthEffect.ShowError("Код должен содержать 6 цифр"))
            return
        }
        
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            
            try {
                // API вызов: POST /auth
                val authResponse = apiRepository.auth(
                    mapOf(
                        "phone" to state.value.phone.filter { it.isDigit() },
                        "code" to state.value.code
                    )
                )
                
                
                // Сохранение сессии
                sessionManager.saveToken(authResponse.token)
                sessionManager.saveHash(authResponse.hash)
                
                
                sendEffect(AuthEffect.NavigateToMain)
                
                
            } catch (e: Exception) {
                sendEffect(AuthEffect.ShowError("Неверный код подтверждения"))
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }
}
