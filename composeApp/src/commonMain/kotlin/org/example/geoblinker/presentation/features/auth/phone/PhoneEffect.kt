package org.example.geoblinker.presentation.features.auth/phone




/**
 * Side-effects для ввода телефона
 */
sealed class PhoneEffect {
    data object NavigateBack : PhoneEffect()
    data class ShowError(val message: String) : PhoneEffect()
    data class ShowMessage(val message: String) : PhoneEffect()
}
