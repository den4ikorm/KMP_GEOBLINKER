package org.example.geoblinker.presentation.features.info/about_app




/**
 * События для информации о приложении
 */
sealed class AboutAppEvent {
    data object NavigateBack : AboutAppEvent()
    data object ClearError : AboutAppEvent()
}
