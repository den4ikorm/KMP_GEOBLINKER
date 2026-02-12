package org.example.geoblinker.presentation.features.auth


import androidx.compose.material3.*
import androidx.compose.runtime.*


/**
 * MVI Contract для модуля авторизации
 */


data class AuthState(
    val phone: String = "",
    val code: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPhoneValid: Boolean = false,
    val isCodeValid: Boolean = false
)


sealed class AuthEvent {
    data class OnPhoneChanged(val phone: String) : AuthEvent()
    data class OnCodeChanged(val code: String) : AuthEvent()
    object OnSendCode : AuthEvent()
    object OnConfirmCode : AuthEvent()
    object OnClearError : AuthEvent()
}


sealed class AuthEffect {
    data class ShowError(val message: String) : AuthEffect()
    object NavigateToCodeScreen : AuthEffect()
    object NavigateToMain : AuthEffect()
}
