package org.example.geoblinker.presentation.features.settings/email




/**
 * Side-effects для изменения email
 */
sealed class EmailSettingsEffect {
    data object NavigateBack : EmailSettingsEffect()
    data class ShowError(val message: String) : EmailSettingsEffect()
    data class ShowMessage(val message: String) : EmailSettingsEffect()
}
