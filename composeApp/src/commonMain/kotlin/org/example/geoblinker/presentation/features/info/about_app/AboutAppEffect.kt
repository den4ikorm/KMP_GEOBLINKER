package org.example.geoblinker.presentation.features.info/about_app




/**
 * Side-effects для информации о приложении
 */
sealed class AboutAppEffect {
    data object NavigateBack : AboutAppEffect()
    data class ShowError(val message: String) : AboutAppEffect()
    data class ShowMessage(val message: String) : AboutAppEffect()
}
